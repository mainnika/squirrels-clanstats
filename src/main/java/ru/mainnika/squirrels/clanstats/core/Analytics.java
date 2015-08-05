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

		ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance();
	}

	private enum ChatType
	{
		ROOM,
		CLAN,
		COMMON,
		NEWBIE;
	}

	private int playerId;
	private int clanId;

	private ChatBot chatBot;
	private Credentials credentials;
	private Player player;
	private Clan clan;

	public Analytics(Connection connection, Credentials credentials)
	{
		super(connection);

		this.on(Server.HELLO, "onHello");
		this.on(Server.GUARD, "onGuard");
		this.on(Server.LOGIN, "onLogin");
		this.on(Server.INFO, "onPlayerInfo");
		this.on(Server.INFO_NET, "onPlayerInfo");
		this.on(Server.CLAN_INFO, "onClanInfo");
		this.on(Server.CLAN_BALANCE, "onClanBalance");
		this.on(Server.CLAN_MEMBERS, "onClanMembers");
		this.on(Server.CHAT_MESSAGE, "onChatMessage");

		this.credentials = credentials;

		this.chatBot = new ChatBot(this);
	}

	public void onConnect()
	{
		this.sendPacket(Client.HELLO);
	}

	public void onDisconnect()
	{
		Timers.unsubscribe(this);
		this.player = null;
	}

	@Override
	public void onTimer()
	{
		log.info("Timer tick!");

		if (this.clan != null)
		{
			this.requestClan(this.clan.id());
		}
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

		this.playerId = packet.getInt(1);

		this.sendPacket(Client.CHAT_ENTER);

		Timers.subscribe(this, 1, 1, TimeUnit.MINUTES);
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

		if (players.length == 0)
			return;

		this.playerReceived(players[0]);

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
		Clan[] clans = new Clan[info.size()];

		for (int i = 0; i < info.size(); i++)
		{
			Group element = info.getGroup(i);
			clans[i] = Clan.fromInfo(element);

			this.sendPacket(Client.CLAN_GET_MEMBERS, clans[i].id());
		}

		if (clans.length == 0)
			return;

		this.clanReceived(clans[0]);

		ClansCache.getInstance().put(clans);
	}

	public void onClanBalance(Packet packet)
	{
		if (this.clan == null)
		{
			return;
		}

		int coins = packet.getInt(0);
		int nuts = packet.getInt(1);

		this.clan.setBalance(coins, nuts);
	}

	public void onClanMembers(Packet packet)
	{
		Clan clan = ClansCache.getInstance().get(packet.getInt(0));

		if (clan == null)
		{
			return;
		}

		Group members = packet.getGroup(1);
		int[] membersArray = new int[members.size()];

		for (int i = 0; i < members.size(); i++)
		{
			membersArray[i] = members.getGroup(i).getInt(0);
		}

		this.requestPlayer(membersArray);
		clan.setPlayers(packet.getGroup(1));
	}

	public void onChatMessage(Packet packet) throws IOException
	{
		ChatType chatType = ChatType.values()[packet.getByte(0)];
		int playerId = packet.getInt(1);
		String message = packet.getString(2);

		if (chatType != ChatType.CLAN)
			return;

		if (message.charAt(0) != '/')
			return;

		this.chatBot.request(message.substring(1));
	}

	public void playerReceived(Player player)
	{
		if (this.playerId != player.id())
		{
			return;
		}

		this.player = player;
		this.clanId = player.clanId();
	}

	public void clanReceived(Clan clan)
	{
		if (this.clanId != clan.id())
		{
			return;
		}

		this.clan = clan;
	}

	public void requestPlayer(byte type, long... nid)
	{
		this.sendPacket(Client.REQUEST_NET, Group.make(nid), type, 0xFFFFFFFF);
	}

	public void requestPlayer(int... uid)
	{
		this.sendPacket(Client.REQUEST, Group.make(uid), 0xFFFFFFFF);
	}

	public void requestClan(int... uid)
	{
		this.sendPacket(Client.CLAN_REQUEST, Group.make(uid), 0xFFFFFFFF);
	}

	public void clanChat(String message)
	{
		this.sendPacket(Client.CHAT_MESSAGE, (byte)1, message);
	}
}
