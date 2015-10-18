package ru.mainnika.squirrels.clanstats.analytics;

public enum Analytics
{
	CLAN_MEMBERS_HOURLY(0, new ClanMembersActivityHourly());

	private final int type;
	private final AnalyticSnapshot instance;

	Analytics(int type, AnalyticSnapshot instance)
	{
		this.type = type;
		this.instance = instance;
	}

	public int type()
	{
		return this.type;
	}

	public AnalyticSnapshot instance()
	{
		return this.instance;
	}
}
