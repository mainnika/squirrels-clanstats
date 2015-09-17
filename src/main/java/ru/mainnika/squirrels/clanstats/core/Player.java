package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.packets.server.PlayerInfo;
import ru.mainnika.squirrels.clanstats.utils.DateTime;

public class Player
{
	private Integer id;
	private Long netId;
	private Byte netType;
	private String name;
	private String photo;
	private String profile;
	private Boolean isModerator;
	private Boolean isEditor;
	private Boolean isOnline;
	private Boolean isVip;
	private Integer squirrelExperience;
	private Integer shamanExperience;
	private Integer clanId;

	private int updated;

	private Player()
	{
	}

	public int id()
	{
		return this.id;
	}

	public long netId()
	{
		return this.netId;
	}

	public byte netType()
	{
		return this.netType;
	}

	public String name()
	{
		return this.name;
	}

	public String photo()
	{
		return this.photo;
	}

	public String profile()
	{
		return this.profile;
	}

	public boolean isModerator()
	{
		return this.isModerator;
	}

	public boolean isEditor()
	{
		return this.isEditor;
	}

	public boolean isOnline()
	{
		return this.isOnline;
	}

	public boolean isVip()
	{
		return this.isVip;
	}

	public int squirrelExperience()
	{
		return this.squirrelExperience;
	}

	public int shamanExperience()
	{
		return this.shamanExperience;
	}

	public int clanId()
	{
		return this.clanId;
	}

	public int updated()
	{
		return this.updated;
	}

	public void updateFromInfo(PlayerInfo.Info info)
	{
		if (this.id() != info.id())
			return;

		Long netId = info.netId();
		if (netId != null)
			this.netId = netId;

		Byte netType = info.netType();
		if (netType != null)
			this.netType = netType;

		String name = info.name();
		if (name != null)
			this.name = name;

		String photo = info.photo();
		if (photo != null)
			this.photo = photo;

		String profile = info.profile();
		if (profile != null)
			this.profile = profile;

		Boolean isModerator = info.isModerator();
		if (isModerator != null)
			this.isModerator = isModerator;

		Boolean isEditor = info.isEditor();
		if (isEditor != null)
			this.isEditor = isEditor;

		Boolean isOnline = info.isOnline();
		if (isOnline != null)
			this.isOnline = isOnline;

		Boolean isVip = info.isVip();
		if (isVip != null)
			this.isVip = isVip;

		Integer squirrelExperience = info.squirrelExperience();
		if (squirrelExperience != null)
			this.squirrelExperience = squirrelExperience;

		Integer shamanExperience = info.shamanExperience();
		if (shamanExperience != null)
			this.shamanExperience = shamanExperience;

		Integer clanId = info.clanId();
		if (clanId != null)
			this.clanId = clanId;

		this.updated = DateTime.getUnixtime();
	}

	public void updateFromPlayer(Player player)
	{
		if (this.id() != player.id)
			return;

		Long netId = player.netId;
		if (netId != null)
			this.netId = netId;

		Byte netType = player.netType;
		if (netType != null)
			this.netType = netType;

		String name = player.name;
		if (name != null)
			this.name = name;

		String photo = player.photo;
		if (photo != null)
			this.photo = photo;

		String profile = player.profile;
		if (profile != null)
			this.profile = profile;

		Boolean isModerator = player.isModerator;
		if (isModerator != null)
			this.isModerator = isModerator;

		Boolean isEditor = player.isEditor;
		if (isEditor != null)
			this.isEditor = isEditor;

		Boolean isOnline = player.isOnline;
		if (isOnline != null)
			this.isOnline = isOnline;

		Boolean isVip = player.isVip;
		if (isVip != null)
			this.isVip = isVip;

		Integer squirrelExperience = player.squirrelExperience;
		if (squirrelExperience != null)
			this.squirrelExperience = squirrelExperience;

		Integer shamanExperience = player.shamanExperience;
		if (shamanExperience != null)
			this.shamanExperience = shamanExperience;

		Integer clanId = player.clanId;
		if (clanId != null)
			this.clanId = clanId;

		this.updated = DateTime.getUnixtime();
	}

	public static Player createFromInfo(PlayerInfo.Info info)
	{
		Player player = new Player();

		player.id = info.id();

		player.netId = info.netId();
		player.netType = info.netType();
		player.name = info.name();
		player.photo = info.photo();
		player.profile = info.profile();
		player.isModerator = info.isModerator();
		player.isEditor = info.isEditor();
		player.isOnline = info.isOnline();
		player.isVip = info.isVip();
		player.squirrelExperience = info.squirrelExperience();
		player.shamanExperience = info.shamanExperience();
		player.clanId = info.clanId();

		player.updated = DateTime.getUnixtime();

		return player;
	}
}
