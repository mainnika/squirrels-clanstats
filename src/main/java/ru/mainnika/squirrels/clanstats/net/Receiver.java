package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.$.Handler;
import ru.mainnika.squirrels.clanstats.net.packets.Client;
import ru.mainnika.squirrels.clanstats.net.packets.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public abstract class Receiver
{
	private static final Logger log;
	private static final HashMap<Server, Handler> handlers;

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
		Handler method = handlers.get(format);

		try
		{
			method.handle(this, packet);
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

	public static void on(Server format, Handler method)
	{
		handlers.put(format, method);
	}

}
