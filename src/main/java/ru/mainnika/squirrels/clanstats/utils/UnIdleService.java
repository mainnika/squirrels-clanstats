package ru.mainnika.squirrels.clanstats.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class UnIdleService implements Runnable
{
	private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

	private static final String instanceUrl = "http://sqclanstats.azurewebsites.net/ping";
//	private static final String instanceUrl = "http://localhost:8080/clan/ping";

	private static final Logger log;

	private static UnIdleService instance = null;
	private static ScheduledFuture task = null;

	static
	{
		log = Logger.getLogger(UnIdleService.class.getName());
	}

	private AtomicBoolean isIdle;

	public static UnIdleService create()
	{
		synchronized (service)
		{
			if (instance != null)
				return instance;

			instance = new UnIdleService();
			task = service.scheduleAtFixedRate(instance, 1, 1, TimeUnit.MINUTES);

			return instance;
		}
	}

	public static void destroy()
	{
		synchronized (service)
		{
			task.cancel(true);

			instance = null;
			task = null;
		}
	}

	private UnIdleService()
	{
		log.info("UnIdle service created");

		this.isIdle = new AtomicBoolean(false);
	}

	@Override
	public void run()
	{
		this.isIdle.set(true);

		log.info("Checking Idle status");

		int counter = 0;

		while (this.isIdle.get())
		{
			log.info(String.format("Try ping #%d", ++counter));

			HttpURLConnection connection;

			try
			{
				URL url = new URL(instanceUrl + "?" + Math.random());

				connection = (HttpURLConnection) url.openConnection();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
				{
					String error = String.format("Can not ping, status code %d", connection.getResponseCode());

					log.info(error);
					throw new IOException(error);
				}

				connection.disconnect();
			} catch (Exception ignored)
			{
			}
		}
	}

	public void ping()
	{
		this.isIdle.set(false);

		log.info("Received ping");
	}
}
