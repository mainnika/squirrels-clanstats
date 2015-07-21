package ru.mainnika.squirrels.clanstats.core;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PlayersCache
{

	private static final PlayersCache instance;

	static
	{
		instance = new PlayersCache();
	}

	public static PlayersCache getInstance()
	{
		return instance;
	}

	private ConcurrentHashMap<Integer, Player> players;

	private PlayersCache()
	{
		this.players = new ConcurrentHashMap<>();
	}

	public synchronized void put(Player... players)
	{
		for (Player player : players)
		{
			Player old = this.players.get(player.id());

			if (old == null)
			{
				this.players.put(player.id(), player);
				continue;
			}

			this.players.replace(player.id(), player);
		}
	}

}
