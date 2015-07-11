package ru.mainnika.squirrels.clanstats.net;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mainn_000 on 7/12/2015.
 */
public class Group extends ArrayList<Object>
{
	public Group()
	{
		super();
	}

	public Group(int capatity)
	{
		super(capatity);
	}

	public Group(Collection<?> init)
	{
		super(init);
	}

	public Group(Group other)
	{
		super(other);
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
}
