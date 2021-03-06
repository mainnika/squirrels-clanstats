package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class ClanGetMembers extends ClientPacket
{
	private Integer clanId;

	public ClanGetMembers(Integer clanId)
	{
		this.clanId = clanId;
	}

	@Override
	public ByteBuffer build()
	{
		return writeI(this.clanId);
	}
}
