package ru.mainnika.squirrels.urban;

import ru.mainnika.squirrels.urban.net.Server;
import ru.mainnika.squirrels.urban.utils.Timers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener {

    public static final String VERSION_MAJ = "0";
    public static final String VERSION_MIN = "50";

    private Server server;

    public static String getVersion() {
        return "CORE: " + VERSION_MAJ + "." + VERSION_MIN + "\n" + "JAVA: " + System.getProperty("java.vm.name") + " "
                + System.getProperty("java.runtime.version") + "\n" + "VENDOR: " + System.getProperty("java.vm.vendor")
                + "\n" + "OS: " + System.getProperty("os.name") + "\n" + "ARCH: " + System.getProperty("os.arch") + "\n"
                + "ZONE: " + System.getProperty("user.timezone") + "\n";
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Timers.create();

        this.server = new Server();
        this.server.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Timers.destroy();
    }
}
