package ru.mainnika.squirrels.clanstats.net.packets;

import java.util.HashMap;

public enum Client
{
	HELLO(8, ""),
	GUARD(32, "S"),
	REQUEST(16,"[I]I"),
	REQUEST_NET(17,"[L]BI"),
	CLAN_REQUEST(95, "[I]I"),
	LOGIN(9, "LBSII");

	private static HashMap<Short, Client> _client;

	static
	{
		Client._client = new HashMap<>();

		for (Client packet : Client.values())
			Client._client.put(packet.id, packet);
	}

	private short id;
	private String mask;

	Client(int id, String mask)
	{
		this.id = (short)id;
		this.mask = mask;
	}

	public short id()
	{
		return this.id;
	}

	public String mask()
	{
		return this.mask;
	}

	public String toString()
	{
		return "Client packet " + this.id + " " + this.getClass().getName() + "\"" + this.mask + "\"";
	}

	public Client getById(short id)
	{
		return Client._client.get(id);
	}

}
