package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Receiver;
import ru.mainnika.squirrels.clanstats.net.packets.ClientParser;
import ru.mainnika.squirrels.clanstats.net.packets.server.*;
import ru.mainnika.squirrels.clanstats.utils.GuardSolver;
import ru.mainnika.squirrels.clanstats.utils.Timers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Analytics extends Receiver implements Timers.Task
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());

		ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance();
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

		this.credentials = credentials;
		this.chatBot = new ChatBot(this);
	}

	public void onConnect()
	{
		this.sendPacket(ClientParser.HELLO);
	}

	public void onDisconnect()
	{
		Timers.unsubscribe(this);
		this.player = null;
	}

	public void onPacket(Hello packet)
	{
		log.info("HELLO");
	}

	public void onPacket(Login packet) throws IOException
	{
		byte status = packet.getStatus();

		log.info("Received login with status " + status);

		if (status != 0)
		{
			this.io.disconnect();
			return;
		}

		this.playerId = packet.getPlayerId();

		this.sendPacket(ClientParser.CHAT_ENTER);

		Timers.subscribe(this, 1, 1, TimeUnit.MINUTES);
	}

	public void onPacket(Guard packet) throws IOException
	{
		log.info("Received guard");

		byte[] inflatedRaw = packet.getInflatedTask();

		if (inflatedRaw.length > 0)
		{
			byte[] deflatedRaw = GuardSolver.deflate(inflatedRaw, 0, inflatedRaw.length);

			String deflatedTask = new String(deflatedRaw);
			String response = GuardSolver.solve(deflatedTask);

			log.info("Guard response " + response);

			this.sendPacket(ClientParser.GUARD, response);
		}

		this.sendPacket(ClientParser.LOGIN, this.credentials.uid(), this.credentials.type(), this.credentials.auth(), 0, 0);
	}

	public void onPacket(PlayerInfo packet)
	{
		int count = packet.count();
		Player[] players = new Player[count];

		for (int i = 0; i < count; i++)
		{
			PlayerInfo.Info element = packet.getInfoElement(i);
			players[i] = Player.createFromInfo(element);

			int clanId = element.clanId();

			if (clanId == 0)
				continue;

			this.sendPacket(ClientParser.CLAN_REQUEST, Group.make(clanId), ClanInfo.FULL_MASK);
		}

		if (players.length == 0)
			return;

		this.playerReceived(players[0]);

		PlayersCache.getInstance().put(players);
	}

	public void onPacket(ClanInfo packet)
	{
		int count = packet.count();
		Clan[] clans = new Clan[count];

		for (int i = 0; i < count; i++)
		{
			ClanInfo.Info element = packet.getInfoElement(i);
			clans[i] = Clan.createFromInfo(element);
		}

		if (clans.length == 0)
			return;

		this.clanReceived(clans[0]);

		ClansCache.getInstance().put(clans);
	}

	public void onPacket(ClanBalance packet)
	{
		if (this.clan == null)
		{
			return;
		}

		int coins = packet.coins();
		int nuts = packet.nuts();

		this.clan.setBalance(coins, nuts);
	}

	public void onPacket(ClanMembers packet)
	{
		Clan clan = ClansCache.getInstance().get(packet.clanId());

		if (clan == null)
		{
			return;
		}

		Group players = packet.members();
		int[] playersForRequest = new int[players.size()];

		for (int i = 0; i < players.size(); i++)
		{
			playersForRequest[i] = players.getGroup(i).getInt(0);
		}

		this.requestPlayers(playersForRequest);
		clan.setPlayers(players);
	}

	public void onPacket(ChatMessage packet) throws IOException
	{
		ChatBot.ChatType chatType = ChatBot.ChatType.values()[packet.chatType()];
		int playerId = packet.playerId();
		String message = packet.message();

		if (chatType != ChatBot.ChatType.CLAN)
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

		this.requestClans(this.clanId);
	}

	public void clanReceived(Clan clan)
	{
		if (this.clanId != clan.id())
		{
			return;
		}

		this.clan = clan;
	}

	@Override
	public void onTimer()
	{
		log.info("Timer tick!");

		if (this.clan != null)
		{
			this.requestClans(this.clan.id());
		}
	}

	public void requestPlayers(byte type, long... nid)
	{
		this.sendPacket(ClientParser.REQUEST_NET, Group.make(nid), type, PlayerInfo.FULL_MASK);
	}

	public void requestPlayers(int... uids)
	{
		this.sendPacket(ClientParser.REQUEST, Group.make(uids), PlayerInfo.FULL_MASK);
	}

	public void requestClans(int... uids)
	{
		this.sendPacket(ClientParser.CLAN_REQUEST, Group.make(uids), ClanInfo.FULL_MASK);

		for (int uid : uids)
		{
			this.sendPacket(ClientParser.CLAN_GET_MEMBERS, uid);
		}
	}

	public void clanChat(String message)
	{
		this.sendPacket(ClientParser.CHAT_MESSAGE, (byte) 1, message);
	}
}
