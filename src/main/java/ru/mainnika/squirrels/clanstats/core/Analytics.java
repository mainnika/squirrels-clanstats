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
import ru.mainnika.squirrels.clanstats.utils.Timers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Analytics extends Receiver<Analytics> implements Timers.Task
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	private Credentials credentials;

	public Analytics(Connection connection, Credentials credentials)
	{
		super(connection);

		this.on(Server.HELLO, "onHello");
		this.on(Server.GUARD, "onGuard");
		this.on(Server.LOGIN, "onLogin");
		this.on(Server.INFO, "onPlayerInfo");
		this.on(Server.INFO_NET, "onPlayerInfo");
		this.on(Server.CLAN_INFO, "onClanInfo");

		this.credentials = credentials;
	}

	public void onConnect()
	{
		this.sendPacket(Client.HELLO);
	}

	public void onDisconnect()
	{
		Timers.unsubscribe(this);
	}

	@Override
	public void onTimer()
	{
		log.info("Timer tick!");
	}

	public void onHello(Packet packet)
	{
	}

	public void onLogin(Packet packet) throws IOException
	{
		byte status = packet.getByte(0);

		log.info("Received login with status " + status);

		if (status != 0)
		{
			this.io.disconnect();
			return;
		}

		Timers.subscribe(this, 5, 5, TimeUnit.MINUTES);

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

	public void onPlayerInfo(Packet packet)
	{
		byte[] raw = packet.getArray(0);
		int mask = packet.getInt(1);
		boolean full = mask == -1;

		if (!full)
		{
			return;
		}

		Group info = PlayerInfo.get(raw, mask);
		Player[] players = new Player[info.size()];

		for (int i = 0; i < info.size(); i++)
		{
			Group element = info.getGroup(i);
			players[i] = Player.fromInfo(element);
		}

		PlayersCache.getInstance().put(players);
	}

	public void onClanInfo(Packet packet)
	{
		byte[] raw = packet.getArray(0);
		int mask = packet.getInt(1);
		boolean full = mask == -65537;

		if (!full)
		{
			return;
		}

		Group info = ClanInfo.get(raw, mask);
	}

	public void requestPlayer(byte type, long... nid)
	{
		this.sendPacket(Client.REQUEST_NET, Group.make(nid), type, 0xFFFFFFFF);
	}

	public void requestPlayer(long... uid)
	{
		this.sendPacket(Client.REQUEST, Group.make(uid), 0xFFFFFFFF);
	}

	public void requestClan(int... uid)
	{
		this.sendPacket(Client.CLAN_REQUEST, Group.make(uid), 0xFFFFFFFF);
	}
}
