package ru.mainnika.squirrels.clanstats;

import ru.mainnika.squirrels.clanstats.core.Analytics;
import ru.mainnika.squirrels.clanstats.core.Credentials;
import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.utils.UnIdleService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener
{
	private Connection net;
	private Credentials cred;
	private Analytics analytics;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		this.net = Connection.create("88.212.207.7", 22227);
		this.cred = new Credentials(8479389, 0, "292e587617848804a15d6347ed80b1f6");
		this.analytics = new Analytics(net, cred);

		this.net.start();

		UnIdleService.create();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		this.net.stop();

		UnIdleService.destroy();
	}
}
