package ru.mainnika.squirrels.clanstats.storage;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class Database
{
	public static class Guard
	{
		private Database instance;

		public Guard() throws SQLException
		{
			this.instance = available.pollFirst();

			if (this.instance == null)
			{
				log.info("Creating new Database instance");
				this.instance = new Database();
				return;
			}

			log.info("Getting existing Database instance");
		}

		public Database get() throws IOException
		{
			if (this.instance == null)
				throw new IOException("Guard already closed");

			return this.instance;
		}

		public void close() throws IOException
		{
			if (this.instance == null)
				throw new IOException("Guard already closed");

			available.addLast(this.instance);
			this.instance = null;
		}
	}

	private static final Logger log;
	private static final String url;
	private static final String user;
	private static final String password;

	private static final ConcurrentLinkedDeque<Database> available;

	static
	{
		log = Logger.getLogger(Savable.class.getName());

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ignored)
		{
		}

		user = "root";
		password = "";
		url = "jdbc:mysql://localhost:3306/sqclanbase";

		available = new ConcurrentLinkedDeque<>();
	}

	private Connection sql;

	private Database() throws SQLException
	{
		this.sql = DriverManager.getConnection(url, user, password);
	}

	public int test() throws SQLException
	{
		log.info("Exec test");

		String query = "SELECT COUNT(*) as `count` from `snapshots`";
		PreparedStatement statement = this.sql.prepareStatement(query);

		ResultSet result = statement.executeQuery();

		result.first();

		return result.getInt("count");
	}
}
