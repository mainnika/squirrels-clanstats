package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.core.Analytics;
import ru.mainnika.squirrels.clanstats.net.packets.Client;
import ru.mainnika.squirrels.clanstats.net.packets.Server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

public abstract class Receiver
{
	private static final Logger log;
	private static final HashMap<Server, Method> handlers;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
		handlers = new HashMap<>();
	}

	private Connection io;

	public Receiver(Connection connection)
	{
		this.io = connection;
		this.io.setReceiver(this);
	}

	public abstract void onConnect();

	public abstract void onDisconnect();

	public void onPacket(Packet packet)
	{
		short id = packet.getId();
		Server format = Server.getById(id);
		Method method = handlers.get(format);

		try
		{
			method.invoke(packet);
		} catch (Exception e)
		{
			if (method == null)
			{
				log.warning("No handler for packet " + format.toString());
				return;
			}

			log.warning("Something wrong with handler " + method.toString() + "(" + e.getMessage() + ")");
		}
	}

	public void sendPacket(Client format, Object... args)
	{
		try
		{
			this.io.send(format, args);
		} catch (IOException e)
		{
			log.warning("IO error " + e.getMessage());
		}
	}

	public static void on(Server format, String method)
	{
		try
		{
			handlers.put(format, Analytics.class.getMethod(method, Packet.class));
		} catch (NoSuchMethodException e)
		{
			log.warning("Can not register handler " + method + " (" + e.getMessage() + ")");
		}
	}

}
