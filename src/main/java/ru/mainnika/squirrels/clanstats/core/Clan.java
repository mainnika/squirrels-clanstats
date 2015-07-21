package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Group;

import java.util.HashMap;
import java.util.Map;

public class Clan
{
	private int id;
	private int leaderId;
	private int level;
	private int experience;
	private int coins;
	private int nuts;

	private String name;
	private String photo;
	private String emblem;

	private HashMap<Integer, Map.Entry<Integer, Integer>> statsDaily;

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

	public String emblem()
	{
		return this.emblem;
	}

	public static Clan fromInfo(Group info)
	{
		Clan clan = new Clan();

		clan.id = info.getInt(0);
		clan.leaderId = info.getInt(5);
		clan.level = info.getByte(8);
		clan.experience = info.getInt(9);

		clan.name = info.getString(1);
		clan.photo = info.getString(2);
		clan.emblem = info.getString(3);

		return clan;
	}
}
