package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Guard extends ServerPacket
{
	private byte[] data;

	public Guard(ByteBuffer buffer) throws IOException
	{
		super();

		this.data = readA(buffer);
	}

	public byte[] getInflatedTask()
	{
		return this.data;
	}
}
