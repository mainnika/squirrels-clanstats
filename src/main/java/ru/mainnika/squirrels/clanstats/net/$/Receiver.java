package ru.mainnika.squirrels.clanstats.net.$;

import ru.mainnika.squirrels.clanstats.net.Packet;

public interface Receiver
{
	public void onPacket(Packet packet);

	public void onConnect();

	public void onDisconnect();
}
