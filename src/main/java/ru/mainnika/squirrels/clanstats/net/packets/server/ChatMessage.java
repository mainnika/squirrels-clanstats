package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ChatMessage extends Packet
{
	public ChatMessage(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}

	public byte chatType()
	{
		return this.getByte(0);
	}

	public int playerId()
	{
		return this.getInt(1);
	}

	public String message()
	{
		return this.getString(2);
	}
}
