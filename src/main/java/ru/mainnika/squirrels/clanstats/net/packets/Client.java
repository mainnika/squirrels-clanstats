package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.client.Hello;

import java.util.HashMap;

public enum Client
{
	HELLO(8, Hello.class);

	private static HashMap<Integer, Client> _client;

	static
	{
		Client._client = new HashMap<>();

		for (Client packet : Client.values())
			Client._client.put(packet.id, packet);
	}

	private int id;
	private Class specialize;

	Client(int id, Class mask)
	{
		this.id = id;
		this.specialize = mask;
	}

	public int id()
	{
		return this.id;
	}

	public Class specialize()
	{
		return this.specialize;
	}

	public String toString()
	{
		return "Client packet " + this.id + " " + this.getClass().getName();
	}

	public Client getById(int id)
	{
		return Client._client.get(id);
	}
}
