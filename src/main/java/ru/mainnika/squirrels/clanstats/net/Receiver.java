package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.packets.Client;
import ru.mainnika.squirrels.clanstats.net.packets.Server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

public abstract class Receiver<Consumer>
{
	private static final Logger log;
	private final HashMap<Server, Method> handlers;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	protected Connection io;

	public Receiver(Connection connection)
	{
		this.handlers = new HashMap<>();
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
			method.invoke((Consumer) this, packet);
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

	public void on(Server format, String method)
	{
		try
		{
			this.handlers.put(format, ((Consumer) this).getClass().getMethod(method, Packet.class));
		} catch (NoSuchMethodException e)
		{
			log.warning("Can not register handler " + method + " (" + e.getMessage() + ")");
		}
	}

}
