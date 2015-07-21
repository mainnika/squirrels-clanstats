package ru.mainnika.squirrels.clanstats.utils;

/**
 * Created by mainn_000 on 7/21/2015.
 */
public class DateTime
{
	public static int getUnixtime()
	{
		return (int) (System.currentTimeMillis() / 1000L);
	}
}
