package ru.mainnika.squirrels.urban.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class UnIdleService implements Timers.Task {

    private static final String instanceUrl = "http://sqclanstats.azurewebsites.net/ping";
    private static final Logger log;

    private static UnIdleService instance = null;

    static {
        log = Logger.getLogger(UnIdleService.class.getName());
    }

    private AtomicBoolean isIdle;

    private UnIdleService() {
        log.info("UnIdle service created");

        this.isIdle = new AtomicBoolean(false);
    }

    public static UnIdleService create() {
        synchronized (log) {
            if (instance != null) {
                return instance;
            }

            log.info("UnIdle service creating");

            instance = new UnIdleService();
            Timers.subscribe(instance, 1, 1, TimeUnit.MINUTES);

            return instance;
        }
    }

    public static void destroy() {
        log.info("UnIdle service destroying");

        synchronized (log) {
            instance.isIdle.set(false);
            instance = null;
        }
    }

    @Override
    public void onTimer() {
        this.isIdle.set(true);

        log.info("Checking Idle status");

        int counter = 0;

        while (this.isIdle.get()) {
            log.info(String.format("Try ping #%d", ++counter));

            HttpURLConnection connection;

            try {
                URL url = new URL(instanceUrl + "?" + Math.random());

                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    String error = String.format("Can not ping, status code %d", connection.getResponseCode());

                    log.info(error);
                    throw new IOException(error);
                }

                connection.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    public void ping() {
        this.isIdle.set(false);

        log.info("Received ping");
    }
}
