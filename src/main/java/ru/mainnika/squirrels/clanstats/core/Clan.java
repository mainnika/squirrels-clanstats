package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.packets.server.ClanInfo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clan
{
	private Integer id;
	private Integer leaderId;
	private Byte level;
	private Integer experience;
	private Integer coins;
	private Integer nuts;

	private String name;
	private String photo;
	private String emblem;

	private HashMap<Integer, Map.Entry<Integer, Integer>> statsDaily;
	private ArrayList<Integer> players;

	private Clan()
	{
	}

	public int id()
	{
		return this.id;
	}

	public int leaderId()
	{
		return this.leaderId;
	}

	public int level()
	{
		return this.level;
	}

	public int experience()
	{
		return this.experience;
	}

	public int coins()
	{
		return this.coins;
	}

	public int nuts()
	{
		return this.nuts;
	}

	public String name()
	{
		return this.name;
	}

	public String photo()
	{
		return this.photo;
	}

	public HashMap<Integer, Map.Entry<Integer, Integer>> stats()
	{
		if (this.statsDaily == null)
		{
			return null;
		}

		return (HashMap) this.statsDaily.clone();
	}

	public String emblem()
	{
		return this.emblem;
	}

	public void setBalance(int coins, int nuts)
	{
		this.coins = coins;
		this.nuts = nuts;
	}

	public void setPlayers(Group players)
	{
		this.players = new ArrayList<>(players.size());

		for (int i = 0; i < players.size(); i++)
		{
			Integer playerId = players.getGroup(i).getInt(0);
			this.players.add(playerId);
		}
	}

	public void fromOther(Clan other)
	{
		this.id = other.id;
		this.leaderId = other.leaderId;
		this.level = other.level;
		this.experience = other.experience;
		this.name = other.name;
		this.photo = other.photo;
		this.emblem = other.emblem;

		if (other.statsDaily != null)
		{
			this.statsDaily = (HashMap) other.statsDaily.clone();
		}

		if (other.players != null)
		{
			this.players = (ArrayList) other.players.clone();
		}
	}

	public static Clan createFromInfo(ClanInfo.Info info)
	{
		Clan clan = new Clan();

		clan.id = info.id();
		clan.leaderId = info.leaderId();
		clan.level = info.level();
		clan.experience = info.experience();

		clan.name = info.name();
		clan.photo = info.photo();
		clan.emblem = info.emblem();

		clan.statsDaily = new HashMap<>();
		Group stats = info.stats();
		for (int i = 0; i < stats.size(); i++)
		{
			Group element = stats.getGroup(i);
			clan.statsDaily.put(element.getInt(0), new AbstractMap.SimpleEntry<>(element.getInt(1), element.getInt(2)));
		}

		ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance().add(clan);

		return clan;
	}
}
