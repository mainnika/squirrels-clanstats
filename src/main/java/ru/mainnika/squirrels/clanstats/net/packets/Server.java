package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.server.Guard;
import ru.mainnika.squirrels.clanstats.net.packets.server.Hello;
import ru.mainnika.squirrels.clanstats.net.packets.server.Info;
import ru.mainnika.squirrels.clanstats.net.packets.server.Login;

import java.util.HashMap;

public enum Server
{
	HELLO(1, Hello.class),
	GUARD(2, Guard.class),
	LOGIN(6, Login.class),
	INFO(7, Info.class),
	INFO_NET(8, Info.class);

	private static HashMap<Integer, Server> _server;

	static
	{
		Server._server = new HashMap<>();

		for (Server packet : Server.values())
			Server._server.put(packet.id, packet);
	}

	private int id;
	private Class<? extends ServerPacket> specialize;

	Server(int id, Class<? extends ServerPacket> specialize)
	{
		this.id = id;
		this.specialize = specialize;
	}

	public int id()
	{
		return this.id;
	}

	public Class<? extends ServerPacket> specialize()
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
