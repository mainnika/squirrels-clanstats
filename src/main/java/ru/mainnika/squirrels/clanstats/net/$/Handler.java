package ru.mainnika.squirrels.clanstats.net.$;

import ru.mainnika.squirrels.clanstats.net.Packet;

public interface Handler
{
	void handle(Receiver receiver, Packet packet) throws Exception;
}
