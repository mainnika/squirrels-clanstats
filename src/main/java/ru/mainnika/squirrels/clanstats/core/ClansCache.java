package ru.mainnika.squirrels.clanstats.core;

import java.util.concurrent.ConcurrentHashMap;

public class ClansCache
{

	private static final ClansCache instance;

	static
	{
		instance = new ClansCache();
	}

	public static ClansCache getInstance()
	{
		return instance;
	}

	private ConcurrentHashMap<Integer, Clan> clans;

	private ClansCache()
	{
		this.clans = new ConcurrentHashMap<>();
	}

	public synchronized void put(Clan... clans)
	{
		for (Clan clan : clans)
		{
			Clan old = this.clans.get(clan.id());

			if (old == null)
			{
				this.clans.put(clan.id(), clan);
				continue;
			}

			this.clans.replace(clan.id(), clan);
		}
	}

}
