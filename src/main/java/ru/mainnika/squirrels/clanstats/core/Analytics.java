package ru.mainnika.squirrels.clanstats.core;

import ru.mainnika.squirrels.clanstats.net.Connection;
import ru.mainnika.squirrels.clanstats.net.Receiver;
import ru.mainnika.squirrels.clanstats.net.packets.client.Guard;
import ru.mainnika.squirrels.clanstats.net.packets.client.Hello;
import ru.mainnika.squirrels.clanstats.net.packets.client.Login;
import ru.mainnika.squirrels.clanstats.net.packets.server.PlayerInfo;
import ru.mainnika.squirrels.clanstats.utils.GuardSolver;
import ru.mainnika.squirrels.clanstats.utils.Timers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Analytics extends Receiver implements Timers.Task, DeferredRequests.DeferredRequirer
{
	private static final int FORCE_RECONNECT_TIME = 15;

	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());

//		ru.mainnika.squirrels.clanstats.analytics.Analytics.CLAN_MEMBERS_HOURLY.instance();
	}

	private int playerId;
	private int clanId;

	private int tick;

	private ChatBot chatBot;
	private Credentials credentials;
	private Player player;
	private Clan clan;
	private DeferredRequests deferredPlayers;

	public Analytics(Connection connection, Credentials credentials)
	{
		super(connection);

		this.credentials = credentials;
		this.deferredPlayers = new DeferredRequests(this);
		this.chatBot = new ChatBot(this);

		this.tick = 0;

		this.deferredPlayers.addWaiter(this.chatBot);
	}

	public void onConnect()
	{
		this.sendPacket(new Hello());
	}

	public void onDisconnect()
	{
		Timers.unsubscribe(this);
		this.player = null;
	}

	public void onPacket(ru.mainnika.squirrels.clanstats.net.packets.server.Hello packet)
	{
		log.info("HELLO");
	}

	public void onPacket(ru.mainnika.squirrels.clanstats.net.packets.server.Guard packet) throws IOException
	{
		log.info("Received guard");

		byte[] inflatedRaw = packet.task;

		if (inflatedRaw.length > 0)
		{
			byte[] deflatedRaw = GuardSolver.deflate(inflatedRaw, 0, inflatedRaw.length);

			String deflatedTask = new String(deflatedRaw);
			String response = GuardSolver.solve(deflatedTask);

			log.info("Guard response " + response);

			this.sendPacket(new Guard(response));
		}

		this.sendPacket(new Login(credentials.uid(), credentials.type(), credentials.oauth(), credentials.auth(), 0, 0));
	}

	public void onPacket(ru.mainnika.squirrels.clanstats.net.packets.server.Login packet) throws IOException
	{
		byte status = packet.status;

		log.info("Received login with status " + status);

		if (status != 0)
		{
			this.io.disconnect();
			return;
		}

		this.playerId = packet.innerId;

//		this.sendPacket(ClientParser.CHAT_ENTER);

		Timers.subscribe(this, 1, 1, TimeUnit.MINUTES);
	}

	public void onPacket(ru.mainnika.squirrels.clanstats.net.packets.server.PlayerInfo packet)
	{
		int count = packet.players.size();
		Player[] players = new Player[count];

		for (int i = 0; i < count; i++)
		{
			PlayerInfo.Player player = packet.players.get(i);
			players[i] = Player.createFromInfo(player);

			int clanId = player.clanId;

			if (clanId > 0)
			{
//				this.sendPacket(ClientParser.CLAN_REQUEST, Group.make(clanId), ClanInfo.FULL_MASK);
			}

			if (packet.isFull())
			{
				this.deferredPlayers.flow(players[i]);
			}
		}

		if (players.length == 0)
			return;

		this.playerReceived(players[0]);

		PlayersCache.getInstance().put(players);
	}

	public void onPacket(ru.mainnika.squirrels.clanstats.net.packets.server.ClanInfo packet)
	{
		int count = packet.clans.size();
		Clan[] clans = new Clan[count];

		for (int i = 0; i < count; i++)
		{
			clans[i] = Clan.createFromInfo(packet.clans.get(i));
		}

		if (clans.length == 0)
			return;

		this.clanReceived(clans[0]);

		ClansCache.getInstance().put(clans);
	}

//	public void onPacket(ClanBalance packet)
//	{
//		if (this.clan == null)
//		{
//			return;
//		}
//
//		int coins = packet.coins();
//		int nuts = packet.nuts();
//
//		this.clan.setBalance(coins, nuts);
//	}
//
//	public void onPacket(ClanMembers packet)
//	{
//		Clan clan = ClansCache.getInstance().get(packet.clanId());
//
//		if (clan == null)
//		{
//			return;
//		}
//
//		Group players = packet.members();
//		int[] playersForRequest = new int[players.size()];
//
//		for (int i = 0; i < players.size(); i++)
//		{
//			playersForRequest[i] = players.getGroup(i).getInt(0);
//		}
//
//		this.requestPlayers(playersForRequest);
//		clan.setPlayers(players);
//	}
//
//	public void onPacket(ChatMessage packet) throws IOException
//	{
//		ChatBot.ChatType chatType = ChatBot.ChatType.values()[packet.chatType()];
//		int playerId = packet.playerId();
//		String message = packet.message();
//
//		if (chatType != ChatBot.ChatType.CLAN)
//			return;
//
//		if (message.charAt(0) != '/')
//			return;
//
//		String[] arguments = message.substring(1).split(" ");
//		this.chatBot.request(arguments);
//	}

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
		log.info("Timer tick #" + this.tick);

		this.tick++;

		if (this.tick % Analytics.FORCE_RECONNECT_TIME == 0)
		{
			log.info("Force reconnect");
			this.io.disconnect();
			return;
		}

		if (this.clan != null)
		{
			this.requestClans(this.clan.id());
		}
	}

	public void requestPlayers(byte type, long... nid)
	{
//		this.sendPacket(ClientParser.REQUEST_NET, Group.make(nid), type, PlayerInfo.FULL_MASK);
	}

	public void requestPlayers(int... uids)
	{
//		this.sendPacket(ClientParser.REQUEST, Group.make(uids), PlayerInfo.FULL_MASK);
	}

	public void requestClans(int... uids)
	{
//		this.sendPacket(ClientParser.CLAN_REQUEST, Group.make(uids), ClanInfo.FULL_MASK);

		for (int uid : uids)
		{
//			this.sendPacket(ClientParser.CLAN_GET_MEMBERS, uid);
		}
	}

	public int getPlayerDeferred(int playerId)
	{
		return this.deferredPlayers.addRequest(playerId);
	}

	@Override
	public void deferredRequire(int waitingId)
	{
//		this.requestPlayers(waitingId);
	}

	public void clanChat(String message)
	{
//		this.sendPacket(ClientParser.CHAT_MESSAGE, (byte) 1, message);
	}

	public int clanId()
	{
		return this.clanId;
	}

	public int playerId()
	{
		return this.playerId;
	}
}
