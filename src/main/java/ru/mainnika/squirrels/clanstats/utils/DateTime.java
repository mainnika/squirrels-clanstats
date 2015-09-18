package ru.mainnika.squirrels.clanstats.utils;

import java.util.Date;

public class DateTime
{
	public static int getUnixtime()
	{
		return (int) (System.currentTimeMillis() / 1000L);
	}

	public static int getUnixhour()
	{
		return getUnixtime() / 3600;
	}

	public static Date fromUnixhour(int unixhour)
	{
		return new Date((long) unixhour * 3600000);
	}
}
