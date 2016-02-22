package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;
import ru.mainnika.squirrels.clanstats.net.packets.Server;
import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

	public void onPacket(byte[] data)
	{
		ByteBuffer wrapped_data = ByteBuffer.wrap(data);
		wrapped_data.order(ByteOrder.LITTLE_ENDIAN);

		int id = wrapped_data.getShort();

		Server format = Server.getById(id);

		if (format == null)
		{
			log.warning("Received unknown packet with type " + id);
			return;
		}

		Method method = null;
		Class<ServerPacket> specialize = null;
		ServerPacket packet = null;

		try
		{
			specialize = format.specialize();
			packet = specialize.getConstructor(ByteBuffer.class).newInstance(wrapped_data);
			method = this.getClass().getMethod("onPacket", specialize);

			method.invoke(this, packet);

		} catch (NoSuchMethodException ignored)
		{
			log.warning("No handler for packet " + format.toString());

		} catch (Exception other)
		{
			log.warning("Something wrong with handler (" + other.getMessage() + ")");
		}
	}

	public void sendPacket(ClientPacket packet)
	{
		try
		{
			this.io.send(packet.build());

		} catch (IOException e)
		{
			log.warning("IO error " + e.getMessage());
		}
	}
}
