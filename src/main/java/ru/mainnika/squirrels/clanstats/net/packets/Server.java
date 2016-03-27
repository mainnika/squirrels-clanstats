package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.server.*;

import java.util.HashMap;

public enum Server
{
	HELLO(1, Hello.class),
	GUARD(2, Guard.class),
	LOGIN(7, Login.class),
	INFO(8, PlayerInfo.class),
	INFO_NET(9, PlayerInfo.class),
	CHAT_MESSAGE(58, ChatMessage.class),
	CLAN_INFO(102, ClanInfo.class),
	CLAN_BALANCE(104, ClanBalance.class),
	CLAN_MEMBERS(106, ClanMembers.class);

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
