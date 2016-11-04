package ru.mainnika.squirrels.urban.storage;

import ru.mainnika.squirrels.urban.utils.Config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class Database {

    private static final Logger LOG;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final ConcurrentLinkedDeque<Database> available;

    static {
        LOG = Logger.getLogger(Savable.class.getName());

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignored) {
        }

        USER = Config.dbUser();
        PASSWORD = Config.dbPassword();
        URL = Config.dbUrl();

        available = new ConcurrentLinkedDeque<>();
    }

    private final Connection sql;

    private Database() throws SQLException {
        this.sql = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static class Guard {

        private Database instance;

        public Guard() throws SQLException {
            this.instance = available.pollFirst();

            if (this.instance == null) {
                LOG.info("Creating new Database instance");
                this.instance = new Database();
                return;
            }

            if (this.instance.sql.isClosed()) {
                LOG.info("Creating new Database instance");
                this.instance = new Database();
                return;
            }

            LOG.info("Getting existing Database instance");
        }

        public Database get() throws IOException {
            if (this.instance == null) {
                throw new IOException("Guard already closed");
            }

            return this.instance;
        }

        public void close() {
            if (this.instance == null) {
                return;
            }

            available.addLast(this.instance);
            this.instance = null;
        }
    }
}
