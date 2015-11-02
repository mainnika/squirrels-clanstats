package ru.mainnika.squirrels.clanstats;

import ru.mainnika.squirrels.clanstats.core.Analytics;
import ru.mainnika.squirrels.clanstats.core.Credentials;
import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.utils.Timers;
import ru.mainnika.squirrels.clanstats.utils.UnIdleService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener
{
	public static final String VERSION_MAJ = "0";
	public static final String VERSION_MIN = "41";

	private Connection net;
	private Credentials cred;
	private Analytics analytics;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		Timers.create();
		UnIdleService.create();

		this.net = Connection.create("88.212.207.7", 22236);
		this.cred = new Credentials(8479389, 0, "292e587617848804a15d6347ed80b1f6");
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
