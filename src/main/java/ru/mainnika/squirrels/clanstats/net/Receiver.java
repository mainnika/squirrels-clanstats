package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.packets.Client;
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
	private int nextId;

	public Receiver(Connection connection)
	{
		this.nextId = 0;
		this.io = connection;
		this.io.setReceiver(this);
	}

	public void sendPacket(ClientPacket packet)
	{

		byte[] raw = packet.build();

		ByteBuffer data = ByteBuffer.allocate(4 + 4 + 2 + raw.length);

		try
		{
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.putInt(4 + 2 + raw.length);
			data.putInt(nextId);
			data.putShort((short) Client.getIdByClass(packet.getClass()));
			data.put(raw);

			this.io.send(data.array());
			nextId++;

		} catch (IOException e)
		{
			log.warning("IO error " + e.getMessage());
		}
	}

	protected void onDisconnect_impl()
	{
		this.onDisconnect();
	}

	protected void onConnect_impl()
	{
		this.nextId = 0;
		this.onConnect();
	}

	protected void onPacket(byte[] data)
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

		Method method;
		Class<ServerPacket> specialize;
		ServerPacket packet;

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

	protected abstract void onConnect();

	protected abstract void onDisconnect();
}
