package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ClanBalance extends Packet
{
	public ClanBalance(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}

	public int coins()
	{
		return this.getInt(0);
	}

	public int nuts()
	{
		return this.getInt(1);
	}
}
