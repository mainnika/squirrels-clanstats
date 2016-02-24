package ru.mainnika.squirrels.clanstats.net;

import ru.mainnika.squirrels.clanstats.net.packets.*;

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
		try
		{
			ByteBuffer data = packet.build();
			ByteBuffer header = Packet.joinBuffers(
				Packet.writeI(4 + 2 + data.capacity()),
				Packet.writeI(nextId),
				Packet.writeW((short) Client.getIdByClass(packet.getClass()))
			);

			this.io.send(Packet.joinBuffers(
				header,
				data
			));

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
//			log.warning("Received unknown packet with type " + id);
			return;
		}

		log.info("Received packet with type " + id);

		Method method;
		Class<? extends ServerPacket> specialize;
		ServerPacket packet;

		try
		{
			specialize = format.specialize();
			packet = specialize.getConstructor().newInstance();
			method = this.getClass().getMethod("onPacket", specialize);

			packet.read(wrapped_data);
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
