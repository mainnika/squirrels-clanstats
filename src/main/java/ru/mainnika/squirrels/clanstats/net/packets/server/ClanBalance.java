package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class ClanBalance extends ServerPacket
{
	public Integer coins;
	public Integer nuts;

	@Override
	public ClanBalance read(ByteBuffer buffer)
	{
		this.coins = readI(buffer);
		this.nuts = readI(buffer);

		return this;
	}
}
