package ru.mainnika.squirrels.clanstats.net;

import java.util.HashMap;

public enum Server
{
	HELLO(1, ""),
	GUARD(2, "S"),
	LOGIN(5, "B,IIIBBSIIII[B]I[W]BB[BI][BBB]I"),
	DATA(9, "I[I]");

	private static HashMap<Integer, Server> _server;

	static
	{
		Server._server = new HashMap<>();

		for (Server packet : Server.values())
			Server._server.put(packet.id(), packet);
	}

	private Integer id;
	private String mask;

	Server(Integer id, String mask)
	{
		this.id = id;
		this.mask = mask;
	}

	public Integer id()
	{
		return this.id;
	}

	public String mask()
	{
		return this.mask;
	}

	public String toString()
	{
		return "Server packet " + this.id + "(" + this.mask + ")";
	}

	public static Server getById(Integer id)
	{
		return Server._server.get(id);
	}
}
