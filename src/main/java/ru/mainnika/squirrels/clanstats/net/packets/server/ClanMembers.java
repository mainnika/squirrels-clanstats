package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class ClanMembers extends ServerPacket
{
	public Integer clanId;
	public Group<Integer> members;

	@Override
	public ClanMembers read(ByteBuffer buffer)
	{
		this.clanId = readI(buffer);
		this.members = new Group<>((int) 0).read(buffer);

		return this;
	}
}
