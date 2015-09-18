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
	EXPIRATIONS(19, "[BI]"),
	FRIENDS(20, "[IB]"),
	FRIENDS_ONLINE(21, "[BB]"),
	EVENTS(22, "[LBIII]"),
	BUY(23, "BIIIIII"),
	INVITE(24, "BI"),
	RETURNED_FRIEND(25, "[IB]"),
	DAILY_QUESTS(26, "[BBBI]"),
	LATENCY(27, ""),
	AWARD_COMPLETE(28, "WB"),
	RENAME(29, "B"),
	XSOLLA_SIGNATURE(30, "S"),
	SMILES(31, "[B]"),
	BIRTHDAY(32, ""),
	CLOSEOUTS(33, "WI[W]"),
	OFFERS(34, "B"),
	TEMPORARY_CLOTHES(35, "[WI][W[BI]]"),
	DAILY_BONUS_DATA(36, "BB"),
	DAILY_BONUS_AWARD(37, "B,BW"),
	GOLDEN_CUP_INFO(38, "II"),
	STORAGE_INFO(39, "[BA]"),
	FLAGS(40, "[BB]"),
	TRAINING(41, "[BB]"),
	PROMO_BONUS(42, "B,BW"),
	RATING_SCORES(43, "BI"),
	RATING_DIVISION(44, "BBI,I[I]"),
	RATING_TRANSITION(45, "BBI"),
	RATING_TOP(46, "B[I]"),
	GIFTS(47, "[IBII]"),
	GIFTS_TARGET(48, "[I]"),
	GIFTS_ACCEPT(49, "BI,B"),
	DEFERRED_BONUS(50, "[IBIIWWI]"),
	DEFERRED_BONUS_ACCEPT(51, "BI,BWWI"),
	CHAT_MESSAGE(52, "BIS", ChatMessage.class),
	CHAT_HISTORY(53, "B[IS]"),
	COLLECTION_ASSEMBLE(54, "BBB"),
	COLLECTION_EXCHANGE(55, "BIIBB"),
	ROOM_JOIN(56, "IB"),
	ROOM_LEAVE(57, "I"),
	ROOM_REQUEST_WORLD(58, "I"),
	ROOM_ROUND(59, "B,WIBIWAI"),
	ROOM_PRIVATE(60, "BI"),
	ROUND_HERO(61, "IBFFFF,B"),
	ROUND_CAST_BEGIN(62, "IBS"),
	ROUND_CAST_END(63, "IBBB"),
	ROUND_NUT(64, "IB"),
	ROUND_HOLLOW(65, "BIB,WB"),
	ROUND_DIE(66, "IFFB,I"),
	ROUND_RESPAWN(67, "BIB"),
	ROUND_TEAM(68, "[I][I]"),
	ROUND_FRAGS(69, "II"),
	ROUND_SYNCHRONIZER(70, "I"),
	ROUND_SHAMAN(71, "[I][B]"),
	ROUND_BEASTS(72, "[B[I]]"),
	ROUND_SYNC(73, "I[WFFFFFF]"),
	ROUND_WORLD(74, "A"),
	ROUND_SKILL(75, "IBB,BI"),
	ROUND_SKILL_ACTION(76, "BII,I"),
	ROUND_SKILL_SHAMAN(77, "IBB"),
	ROUND_COMMAND(78, "IS"),
	ROUND_COMPENSATION(79, "BI"),
	ROUND_ELEMENT(80, "IBB"),
	ROUND_ELEMENTS(81, "[B]"),
	ROUND_SMILE(82, "IB"),
	ROUND_ZOMBIE(83, "IB,I"),
	MAPS_LIST(84, "BBB[IBIII]I"),
	MAPS_MAP(85, "IBIWAIIIIII"),
	MAPS_ID(86, "I,BB"),
	MAPS_CHECK(87, "[II]"),
	CLAN_STATE(88, "B,I"),
	CLAN_INFO(89, "AI", ClanInfo.class),
	CLAN_APPLICATION(90, "[II]"),
	CLAN_BALANCE(91, "II", ClanBalance.class),
	CLAN_TRANSACTION(92, "[IBIIII]"),
	CLAN_MEMBERS(93, "I[I]", ClanMembers.class),
	CLAN_PRIVATE_ROOMS(94, "[IBBBW]"),
	CLAN_JOIN(95, "I,I"),
	CLAN_LEAVE(96, "I"),
	CLAN_SUBSTITUTE(97, "[I],B"),
	CLAN_TOTEM_BONUS(98, "B,W"),
	DISCOUNT_BONUS(99, "[BI],B"),
	DISCOUNT_CLOTHES(100, "[W]"),
	DISCOUNT_USE(101, "BI"),
	BRANCHES(102, "BB"),
	TRANSFER(103, "B,II");

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
