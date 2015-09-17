package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ClanMembers extends Packet
{
	public ClanMembers(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}

	public int clanId()
	{
		return this.getInt(0);
	}

	public Group members()
	{
		return this.getGroup(1);
	}
}
