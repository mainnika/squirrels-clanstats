package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class Hello extends ClientPacket
{
	public Hello()
	{
	}

	@Override
	public ByteBuffer build()
	{
		return ByteBuffer.wrap(EMPTY_BUFFER);
	}
}
