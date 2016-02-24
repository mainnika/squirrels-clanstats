package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class ChatEnter extends ClientPacket
{
	public ChatEnter()
	{
	}

	@Override
	public ByteBuffer build()
	{
		return ByteBuffer.wrap(EMPTY_BUFFER);
	}
}
