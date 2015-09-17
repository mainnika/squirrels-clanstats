package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.packets.ClientParser;
import ru.mainnika.squirrels.clanstats.net.packets.ServerParser;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public abstract class Receiver
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	protected Connection io;

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
		ServerParser format = ServerParser.getById(id);
		Class specialize = format.specialize();
		Method method = null;

		try
		{
			method = this.getClass().getMethod("onPacket", specialize);
			method.invoke(this, packet);
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

	public void sendPacket(ClientParser format, Object... args)
	{
		try
		{
			this.io.send(format, args);
		} catch (IOException e)
		{
			log.warning("IO error " + e.getMessage());
		}
	}
}
