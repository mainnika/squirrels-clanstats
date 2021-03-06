package ru.mainnika.squirrels.clanstats;

import ru.mainnika.squirrels.clanstats.core.Analytics;
import ru.mainnika.squirrels.clanstats.core.Credentials;
import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.utils.Config;
import ru.mainnika.squirrels.clanstats.utils.Timers;
import ru.mainnika.squirrels.clanstats.utils.UnIdleService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener
{
	public static final String VERSION_MAJ = "0";
	public static final String VERSION_MIN = "50";

	private Connection net;
	private Credentials cred;
	private Analytics analytics;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		Timers.create();
		UnIdleService.create();

		this.net = Connection.create(Config.serverIp(), Config.serverPort());
		this.cred = new Credentials(Config.accountUid(), Config.accountType(), Config.accountKey());
		this.analytics = new Analytics(net, cred);

		this.net.start();

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		this.net.stop();

		UnIdleService.destroy();
		Timers.destroy();
	}

	public static String getVersion()
	{
		return "CORE: " + VERSION_MAJ + "." + VERSION_MIN + "\n"
			+ "JAVA: " + System.getProperty("java.vm.name") + " " + System.getProperty("java.runtime.version") + "\n"
			+ "VENDOR: " + System.getProperty("java.vm.vendor") + "\n"
			+ "OS: " + System.getProperty("os.name") + "\n"
			+ "ARCH: " + System.getProperty("os.arch") + "\n"
			+ "ZONE: " + System.getProperty("user.timezone") + "\n"
		;
	}
}
