package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PlayerRequestNet extends ClientPacket
{
	private Group<Long> netIds;
	private Byte type;
	private Integer mask;

	public PlayerRequestNet(Integer mask, Byte type, Long... ids)
	{
		this.netIds = new Group<>(Arrays.asList(ids));
		this.type = type;
		this.mask = mask;
	}

	@Override
	public ByteBuffer build()
	{
		return joinBuffers(
			this.netIds.build(),
			writeB(this.type),
			writeI(this.mask)
		);
	}
}
