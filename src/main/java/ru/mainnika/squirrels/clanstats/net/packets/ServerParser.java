package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.server.*;

import java.util.HashMap;

public enum ServerParser
{
	HELLO(1, "", Hello.class),
	GUARD(2, "A", Guard.class),
	ADMIN_INFO(3, "IIIIIII[BB][BB]BBII[B]I[BI]B[BBBI]WWB"),
	ADMIN_MESSAGE(4, "S"),
	LOGIN(5, "B,IIIBBSIIII[B]I[W]BB[BI][BBB]I", Login.class),
	INFO(6, "AI", PlayerInfo.class),
	INFO_NET(7, "AI", PlayerInfo.class),
	ROOM(8, "BB[I]B"),
	ONLINE(9, "[I]"),
	PLAY_WITH(10, "B,BBI"),
	BAN(11, "IBBII"),
	BALANCE(12, "IIB"),
	FEATHERS(13, "BB"),
	ENERGY_LIMITS(14, "II"),
	ENERGY(15, "IB"),
	MANA(16, "IB"),
	EXPERIENCE(17, "IB"),
	SHAMAN_EXPERIENCE(18, "IB"),
	FRIENDS(19, "[IB]"),
	FRIENDS_ONLINE(20, "[BB]"),
	EVENTS(21, "[LBIII]"),
	BUY(22, "BIIIIII"),
	INVITE(23, "BI"),
	RETURNED_FRIEND(24, "[IB]"),
	DAILY_QUESTS(25, "[BBBI]"),
	LATENCY(26, ""),
	AWARD_COMPLETE(27, "WB"),
	RENAME(28, "B"),
	XSOLLA_SIGNATURE(29, "S"),
	SMILES(30, "[B]"),
	BIRTHDAY(31, ""),
	CLOSEOUTS(32, "WI[W]"),
	OFFERS(33, "B"),
	TEMPORARY_CLOTHES(34, "[WI][W[BI]]"),
	DAILY_BONUS_DATA(35, "BB"),
	DAILY_BONUS_AWARD(36, "B,BW"),
	GOLDEN_CUP_INFO(37, "II"),
	STORAGE_INFO(38, "[BA]"),
	FLAGS(39, "[BB]"),
	TRAINING(40, "[BB]"),
	PROMO_BONUS(41, "B,BW"),
	RATING_SCORES(42, "BI"),
	RATING_DIVISION(43, "BBI,I[I]"),
	RATING_TRANSITION(44, "BBI"),
	RATING_TOP(45, "B[I]"),
	GIFTS(46, "[IBII]"),
	GIFTS_TARGET(47, "[I]"),
	GIFTS_ACCEPT(48, "BI,B"),
	DEFERRED_BONUS(49, "[IBIIWWI]"),
	DEFERRED_BONUS_ACCEPT(50, "BI,BWWI"),
	CHAT_MESSAGE(51, "BIS", ChatMessage.class),
	CHAT_HISTORY(52, "B[IS]"),
	COLLECTION_ASSEMBLE(53, "BBB"),
	COLLECTION_EXCHANGE(54, "BIIBB"),
	ROOM_JOIN(55, "IB"),
	ROOM_LEAVE(56, "I"),
	ROOM_REQUEST_WORLD(57, "I"),
	ROOM_ROUND(58, "B,WIBIWAI"),
	ROOM_PRIVATE(59, "BI"),
	ROUND_HERO(60, "IBFFFF,B"),
	ROUND_CAST_BEGIN(61, "IBS"),
	ROUND_CAST_END(62, "IBBB"),
	ROUND_NUT(63, "IB"),
	ROUND_HOLLOW(64, "BIB,WB"),
	ROUND_DIE(65, "IFFB,I"),
	ROUND_RESPAWN(66, "BIB"),
	ROUND_TEAM(67, "[I][I]"),
	ROUND_FRAGS(68, "II"),
	ROUND_SYNCHRONIZER(69, "I"),
	ROUND_SHAMAN(70, "[I][B]"),
	ROUND_BEASTS(71, "[B[I]]"),
	ROUND_SYNC(72, "I[WFFFFFF]"),
	ROUND_WORLD(73, "A"),
	ROUND_SKILL(74, "IBB,BI"),
	ROUND_SKILL_ACTION(75, "BII,I"),
	ROUND_SKILL_SHAMAN(76, "IBB"),
	ROUND_COMMAND(77, "IS"),
	ROUND_COMPENSATION(78, "BI"),
	ROUND_ELEMENT(79, "IBB"),
	ROUND_ELEMENTS(80, "[B]"),
	ROUND_SMILE(81, "IB"),
	ROUND_ZOMBIE(82, "IB,I"),
	MAPS_LIST(83, "BBB[IBIII]I"),
	MAPS_MAP(84, "IBIWAIIIIII"),
	MAPS_ID(85, "I,BB"),
	MAPS_CHECK(86, "[II]"),
	CLAN_STATE(87, "B,I"),
	CLAN_INFO(88, "AI", ClanInfo.class),
	CLAN_APPLICATION(89, "[II]"),
	CLAN_BALANCE(90, "II", ClanBalance.class),
	CLAN_TRANSACTION(91, "[IBIIII]"),
	CLAN_MEMBERS(92, "I[I]", ClanMembers.class),
	CLAN_PRIVATE_ROOMS(93, "[IBBBW]"),
	CLAN_JOIN(94, "I,I"),
	CLAN_LEAVE(95, "I"),
	CLAN_SUBSTITUTE(96, "[I],B"),
	CLAN_TOTEM_BONUS(97, "B,W"),
	DISCOUNT_BONUS(98, "[BI],B"),
	DISCOUNT_CLOTHES(99, "[W]"),
	DISCOUNT_USE(100, "BI"),
	BRANCHES(101, "BB"),
	TRANSFER(102, "B,II");

	private static HashMap<Short, ServerParser> _server;

	static
	{
		ServerParser._server = new HashMap<>();

		for (ServerParser packet : ServerParser.values())
			ServerParser._server.put(packet.id(), packet);
	}

	private short id;
	private String mask;
	private Class specialize;

	ServerParser(int id, String mask, Class specialize)
	{
		this.id = (short) id;
		this.mask = mask;
		this.specialize = specialize;
	}

	ServerParser(int id, String mask)
	{
		this.id = (short) id;
		this.mask = mask;
		this.specialize = null;
	}

	public short id()
	{
		return this.id;
	}

	public String mask()
	{
		return this.mask;
	}

	public Class specialize()
	{
		return this.specialize;
	}

	public String toString()
	{
		return "ServerParser packet " + this.id + " " + this.name() + " \"" + this.mask + "\"";
	}

	public static ServerParser getById(short id)
	{
		return ServerParser._server.get(id);
	}
}
