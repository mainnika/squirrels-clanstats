package ru.mainnika.squirrels.clanstats.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Group extends ArrayList<Object>
{
	public Group()
	{
	}

	public Group(int capacity)
	{
		super(capacity);
	}

	public Group(Collection<?> collection)
	{
		super(collection);
	}

	public Byte getByte(int index)
	{
		return (Byte) this.get(index);
	}

	public Short getShort(int index)
	{
		return (Short) this.get(index);
	}

	public Integer getInt(int index)
	{
		return (Integer) this.get(index);
	}

	public Long getLong(int index)
	{
		return (Long) this.get(index);
	}

	public Group getGroup(int index)
	{
		return (Group) this.get(index);
	}

	public String getString(int index)
	{
		return (String) this.get(index);
	}

	public byte[] getArray(int index)
	{
		return (byte[]) this.get(index);
	}

	public boolean has(int index)
	{
		return this.size() > index;
	}

	public Group addReturn(Object object)
	{
		this.add(object);
		return this;
	}

	public static Group make(byte... byteArray)
	{
		Group result = new Group(byteArray.length);

		for (byte aByte : byteArray)
		{
			result.add((new Group(1)).add(aByte));
		}

		return result;
	}

	public static Group make(short... shortArray)
	{
		Group result = new Group(shortArray.length);

		for (short aByte : shortArray)
		{
			result.add((new Group(1)).add(aByte));
		}

		return result;
	}

	public static Group make(int... intArray)
	{
		Group result = new Group(intArray.length);

		for (int aByte : intArray)
		{
			result.add((new Group(1)).addReturn(aByte));
		}

		return result;
	}

	public static Group make(long... longArray)
	{
		Group result = new Group(longArray.length);

		for (long aByte : longArray)
		{
			result.add((new Group(1)).add(aByte));
		}

		return result;
	}

//      TODO:
//	public static Group make(Object... objectArray)
//	{
//		Group result = new Group(objectArray.length);
//
//		for (Object aByte : objectArray)
//		{
//			result.add((new Group(1)).add(aByte));
//		}
//
//		return result;
//	}

	public static Group element(Object... objectArray)
	{
		return new Group(Arrays.asList(objectArray));
	}

}
