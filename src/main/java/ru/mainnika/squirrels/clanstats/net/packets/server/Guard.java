package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class Guard extends ServerPacket
{
	public byte[] task;

	@Override
	public Readable read(ByteBuffer buffer)
	{
		this.task = readA(buffer);

		return this;
	}
}
