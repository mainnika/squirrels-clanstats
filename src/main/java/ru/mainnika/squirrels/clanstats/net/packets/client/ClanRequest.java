package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClanRequest extends ClientPacket
{
	private Group<Integer> clanIds;
	private Integer mask;

	public ClanRequest(Integer mask, Integer... ids)
	{
		this.clanIds = new Group<>(Arrays.asList(ids));
		this.mask = mask;
	}

	@Override
	public ByteBuffer build()
	{
		return joinBuffers(
			this.clanIds.build(),
			writeI(this.mask)
		);
	}
}
