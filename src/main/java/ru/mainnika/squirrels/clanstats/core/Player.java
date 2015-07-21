package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

public class Player
{
	private int id;
	private long netId;
	private byte netType;
	private String name;
	private String photo;
	private String profile;
	private boolean isModerator;
	private boolean isEditor;
	private boolean isOnline;
	private boolean isVip;
	private int squirrelExperience;
	private int shamanExperience;
	private int clanId;

	private int updated;

	public int id()
	{
		return this.id;
	}

	public int updated()
	{
		return this.updated;
	}

	private Player()
	{
	}

	public static Player fromInfo(Group info)
	{
		Player player = new Player();

		player.id = info.getInt(0);
		player.netId = info.getLong(1);
		player.netType = info.getByte(2);

		player.name = info.getString(5);
		player.photo = info.getString(7);
		player.profile = info.getString(10);

		player.isModerator = info.getByte(3) > 0;
		player.isEditor = info.getByte(4) > 0;
		player.isOnline = info.getByte(8) > 0;
		player.isVip = info.getInt(34) > 0;

		player.squirrelExperience = info.getInt(22);
		player.shamanExperience = info.getInt(39);

		player.clanId = info.getInt(26);

		player.updated = DateTime.getUnixtime();

		return player;
	}
}
