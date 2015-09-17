package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Hello extends Packet
{
	public Hello(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}
}
