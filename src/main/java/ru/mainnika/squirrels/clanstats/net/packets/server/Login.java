package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Login extends Packet
{
	public Login(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}

	public byte getStatus()
	{
		return this.getByte(0);
	}

	public int getPlayerId()
	{
		return this.getInt(1);
	}
}
