package ru.mainnika.squirrels.clanstats.net;

import java.util.HashMap;

public enum Client
{
	HELLO(1, "B");

	private static HashMap<Integer, Client> _client;

	static
	{
		Client._client = new HashMap<>();

		for (Client packet : Client.values())
			Client._client.put(packet.id, packet);
	}

	private Integer id;
	private String mask;

	Client(Integer id, String mask)
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
		return "Client packet " + this.id + "(" + this.mask + ")";
	}

	public Client getById(Integer id)
	{
		return Client._client.get(id);
	}

}
