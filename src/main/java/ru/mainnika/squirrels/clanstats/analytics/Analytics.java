package ru.mainnika.squirrels.clanstats.analytics;

import ru.mainnika.squirrels.clanstats.storage.Database;

public enum Analytics
{
	CLAN_MEMBERS_HOURLY(0, new ClanMembersActivityHourly()),
	CLAN_BALANCE_HOURLY(1, new ClanBalanceHourly());

	private final int type;
	private final AnalyticSnapshot instance;

	Analytics(int type, AnalyticSnapshot instance)
	{
		this.type = type;
		this.instance = instance;

		Database.Guard guard = null;
		Database base = null;
		try
		{
			guard = new Database.Guard();
			base = guard.get();

			base.loadSnapshot(this);

		} catch (Exception ignored)
		{
		} finally
		{
			if (guard != null)
				guard.close();
		}
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
