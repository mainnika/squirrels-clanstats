package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.server.Hello;

import java.util.HashMap;

public enum Server
{
	HELLO(1, Hello.class);

	private static HashMap<Integer, Server> _server;

	static
	{
		Server._server = new HashMap<>();

		for (Server packet : Server.values())
			Server._server.put(packet.id, packet);
	}

	private int id;
	private Class specialize;

	Server(int id, Class specialize)
	{
		this.id = id;
		this.specialize = specialize;
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
		return "Server packet " + this.id + " " + this.name();
	}

	public static Server getById(int id)
	{
		return Server._server.get(id);
	}
}
