package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.packets.Packet;
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

		return (HashMap) this.statsDaily;
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

	public void setPlayers(Packet.Group<Integer> players)
	{
		this.players = players;
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

	public static Clan createFromInfo(ClanInfo.Clan info)
	{
		Clan clan = new Clan();

		clan.id = info.clanId;
		clan.leaderId = info.leaderId;
		clan.level = info.rank.level;
		clan.experience = info.rank.experience;

		clan.name = info.info.name;
		clan.photo = info.info.emblemBig;
		clan.emblem = info.info.emblemSmall;

		clan.statsDaily = new HashMap<>();
		Packet.Group<ClanInfo.Clan.Statisic> stats = info.statisics;
		for (ClanInfo.Clan.Statisic stat : stats)
		{
			clan.statsDaily.put(stat.innerId, new AbstractMap.SimpleEntry<>(stat.clanExp, stat.playerExp));
		}

		ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance().add(clan);

		return clan;
	}
}
