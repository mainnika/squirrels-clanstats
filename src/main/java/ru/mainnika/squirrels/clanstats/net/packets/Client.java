package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.client.Guard;
import ru.mainnika.squirrels.clanstats.net.packets.client.Hello;
import ru.mainnika.squirrels.clanstats.net.packets.client.Login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum Client
{
	HELLO(9, Hello.class),
	GUARD(30, Guard.class),
	LOGIN(10, Login.class);

	private static HashMap<Integer, Client> _client;

	static
	{
		Client._client = new HashMap<>();

		for (Client packet : Client.values())
			Client._client.put(packet.id, packet);
	}

	private int id;
	private Class<? extends ClientPacket> specialize;

	Client(int id, Class<? extends ClientPacket> mask)
	{
		this.id = id;
		this.specialize = mask;
	}

	public int id()
	{
		return this.id;
	}

	public Class<? extends ClientPacket> specialize()
	{
		return this.specialize;
	}

	public String toString()
	{
		return "Client packet " + this.id + " " + this.getClass().getName();
	}

	public static Client getClassById(int id)
	{
		return Client._client.get(id);
	}

	public static int getIdByClass(Class<? extends ClientPacket> specialize) throws IOException
	{
		for (Map.Entry<Integer, Client> packet : Client._client.entrySet())
		{
			if (packet.getValue().specialize != specialize)
			{
				continue;
			}

			return packet.getKey();
		}

		throw new IOException("Packet is not registered");
	}
}
