package ru.mainnika.squirrels.clanstats.utils;

import org.apache.commons.lang3.ArrayUtils;
import ru.mainnika.squirrels.clanstats.net.Group;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils
{
	public static Group asList(byte[] byteArray)
	{
		return new Group(Arrays.asList(ArrayUtils.toObject(byteArray)));
	}

	public static Group asList(short[] shortArray)
	{
		return new Group(Arrays.asList(ArrayUtils.toObject(shortArray)));
	}

	public static Group asList(int[] intArray)
	{
		return new Group(Arrays.asList(ArrayUtils.toObject(intArray)));
	}

	public static Group asList(long[] longArray)
	{
		return new Group(Arrays.asList(ArrayUtils.toObject(longArray)));
	}

	public static Group asList(Object... objectArray)
	{
		return new Group(Arrays.asList(objectArray));
	}

	public static Group asList(ArrayList... objectArray)
	{
		return new Group(Arrays.asList(objectArray));
	}

}
