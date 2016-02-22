package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

public class Hello extends ClientPacket
{
	public Hello()
	{
	}

	@Override
	public byte[] build()
	{
		return new byte[0];
	}
}
