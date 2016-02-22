package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class Guard extends ClientPacket
{
	private String guardResponse;

	public Guard(String guardResponse)
	{
		this.guardResponse = guardResponse;
	}

	@Override
	public byte[] build()
	{
		ByteBuffer data = joinBuffers(
			writeS(this.guardResponse)
		);

		return data.array();
	}
}
