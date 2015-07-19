package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;
import ru.mainnika.squirrels.clanstats.net.Receiver;
import ru.mainnika.squirrels.clanstats.net.packets.ClanInfo;
import ru.mainnika.squirrels.clanstats.net.packets.Client;
import ru.mainnika.squirrels.clanstats.net.packets.PlayerInfo;
import ru.mainnika.squirrels.clanstats.net.packets.Server;
import ru.mainnika.squirrels.clanstats.utils.GuardSolver;

import java.io.IOException;
import java.util.logging.Logger;

public class Analytics extends Receiver
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());

		on(Server.HELLO, "onHello");
		on(Server.GUARD, "onGuard");
		on(Server.LOGIN, "onLogin");
		on(Server.INFO, "onInfo");
		on(Server.INFO_NET, "onInfo");
		on(Server.CLAN_INFO, "onClanInfo");
	}

	private Credentials credentials;

	public Analytics(Connection connection, Credentials credentials)
	{
		super(connection);

		this.credentials = credentials;
	}

	public void onConnect()
	{
		this.sendPacket(Client.HELLO);
	}

	public void onDisconnect()
	{
	}

	public void onHello(Packet packet)
	{
	}

	public void onLogin(Packet packet)
	{
		byte status = packet.getByte(0);

		log.info("Received login with status " + status);

		this.requestClan(116837, 100621);
	}

	public void onGuard(Packet packet) throws IOException
	{
		log.info("Received guard");

		byte[] inflatedRaw = packet.getArray(0);

		if (inflatedRaw.length > 0)
		{
			byte[] deflatedRaw = GuardSolver.deflate(inflatedRaw, 0, inflatedRaw.length);

			String deflatedTask = new String(deflatedRaw);
			String response = GuardSolver.solve(deflatedTask);

			log.info("Guard response " + response);

			this.sendPacket(Client.GUARD, response);
		}

		this.sendPacket(Client.LOGIN, this.credentials.uid(), this.credentials.type(), this.credentials.auth(), 0, 0);
	}

	public void onInfo(Packet packet)
	{
		byte[] raw = packet.getArray(0);
		int mask = packet.getInt(1);

		Group info = PlayerInfo.get(raw, mask);
	}

	public void onClanInfo(Packet packet)
	{
		byte[] raw = packet.getArray(0);
		int mask = packet.getInt(1);

		Group info = ClanInfo.get(raw, mask);
	}

	public void requestPlayer(byte type, long... uid)
	{
		this.sendPacket(Client.REQUEST_NET, Group.make(uid), type, 0xFFFFFFFF);
	}

	public void requestClan(int... uid)
	{
		this.sendPacket(Client.CLAN_REQUEST, Group.make(uid), 0xFFFFFFFF);
	}
}
