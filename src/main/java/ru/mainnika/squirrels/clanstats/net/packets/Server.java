package ru.mainnika.squirrels.clanstats.net.packets;

import java.util.HashMap;

public enum Server
{
	HELLO(1, ""),
	GUARD(2, "A"),
	ADMIN_INFO(3, "IIIIIII[BB][BB]BBII[B]I[BI]B[BBBI]WWB"),
	ADMIN_MESSAGE(4, "S"),
	LOGIN(5, "B,IIIBBSIIII[B]I[W]BB[BI][BBB]I"),
	INFO(6, "AI"),
	INFO_NET(7, "AI"),
	ROOM(8, "BB[I]B"),
	ONLINE(9, "[I]"),
	RATINGS_COUNT(10, "[I]"),
	RATING(11, "BI[II]"),
	PLAY_WITH(12, "B,BBI"),
	BAN(13, "IBBII"),
	BALANCE(14, "IIB"),
	FEATHERS(15, "BB"),
	ENERGY_LIMITS(16, "II"),
	ENERGY(17, "IB"),
	MANA(18, "IB"),
	EXPERIENCE(19, "IB"),
	SHAMAN_EXPERIENCE(20, "IB"),
	FRIENDS(21, "[IB]"),
	FRIENDS_ONLINE(22, "[BB]"),
	EVENTS(23, "[LBIII]"),
	BUY(24, "BIIIIII"),
	INVITE(25, "BI"),
	RETURNED_FRIEND(26, "[IB]"),
	DAILY_QUESTS(27, "[BBBI]"),
	LATENCY(28, ""),
	AWARD_COMPLETE(29, "WB"),
	RENAME(30, "B"),
	XSOLLA_SIGNATURE(31, "S"),
	SMILES(32, "[B]"),
	BIRTHDAY(33, ""),
	CLOSEOUTS(34, "WI[W]"),
	OFFERS(35, "B"),
	TEMPORARY_CLOTHES(36, "[WI][W[BI]]"),
	DAILY_BONUS_DATA(37, "BB"),
	DAILY_BONUS_AWARD(38, "B,BW"),
	GOLDEN_CUP_INFO(39, "II"),
	STORAGE_INFO(40, "[BA]"),
	FLAGS(41, "[BB]"),
	TRAINING(42, "[BB]"),
	PROMO_BONUS(43, "B,BW"),
	GIFTS(44, "[IBII]"),
	GIFTS_TARGET(45, "[I]"),
	GIFTS_ACCEPT(46, "BI,B"),
	CHAT_MESSAGE(47, "BIS"),
	CHAT_HISTORY(48, "B[IS]"),
	COLLECTION_ASSEMBLE(49, "BBB"),
	COLLECTION_EXCHANGE(50, "BIIBB"),
	ROOM_JOIN(51, "IB"),
	ROOM_LEAVE(52, "I"),
	ROOM_REQUEST_WORLD(53, "I"),
	ROOM_ROUND(54, "B,WIBIWWWAI"),
	ROOM_PRIVATE(55, "BI"),
	ROUND_HERO(56, "IBFFFF,B"),
	ROUND_CAST_BEGIN(57, "IBS"),
	ROUND_CAST_END(58, "IBBB"),
	ROUND_NUT(59, "IB"),
	ROUND_HOLLOW(60, "BIB,WB"),
	ROUND_DIE(61, "IFFB,I"),
	ROUND_RESPAWN(62, "BIB"),
	ROUND_TEAM(63, "[I][I]"),
	ROUND_FRAGS(64, "II"),
	ROUND_SYNCHRONIZER(65, "I"),
	ROUND_SHAMAN(66, "[I][B]"),
	ROUND_BEASTS(67, "[B[I]]"),
	ROUND_SYNC(68, "I[WFFFFFF]"),
	ROUND_WORLD(69, "A"),
	ROUND_SKILL(70, "IBB,BI"),
	ROUND_SKILL_ACTION(71, "BII,I"),
	ROUND_SKILL_SHAMAN(72, "IBB"),
	ROUND_COMMAND(73, "IS"),
	ROUND_COMPENSATION(74, "BI"),
	ROUND_ELEMENT(75, "IBB"),
	ROUND_ELEMENTS(76, "[B]"),
	ROUND_SMILE(77, "IB"),
	ROUND_ZOMBIE(78, "IB,I"),
	MAPS_LIST(79, "BBB[IBIII]I"),
	MAPS_MAP(80, "IBIWWWAIIIIII"),
	MAPS_ID(81, "I,BB"),
	MAPS_CHECK(82, "[II]"),
	CLAN_STATE(83, "B,I"),
	CLAN_INFO(84, "AI"),
	CLAN_APPLICATION(85, "[II]"),
	CLAN_BALANCE(86, "II"),
	CLAN_TRANSACTION(87, "[IBIIII]"),
	CLAN_MEMBERS(88, "I[I]"),
	CLAN_PRIVATE_ROOMS(89, "[IBBBW]"),
	CLAN_JOIN(90, "I,I"),
	CLAN_LEAVE(91, "I"),
	CLAN_SUBSTITUTE(92, "[I],B"),
	CLAN_TOTEM_BONUS(93, "B,W"),
	DISCOUNT_BONUS(94, "[BI],B"),
	DISCOUNT_CLOTHES(95, "[W]"),
	DISCOUNT_USE(96, "BI"),
	BRANCHES(97, "BB"),
	TRANSFER(98, "B,II"),
	OLYMPIC_SCORES(99, "[BW]"),
	OLYMPIC_DAY(100, "BI"),
	OLYMPIC_PRIZES(101, "B[B]"),
	OLYMPIC_RATING(102, "B[IW]"),
	OLYMPIC_PLACES(103, "[W]"),
	REPLAY_REQUEST(104, ""),
	REPLAY_LIST(105, "B,[IIBBWBI]"),
	REPLAY_DATA(106, "B,IIABIWWWA"),
	REPLAY_TAG(107, "B,IB");

	private static HashMap<Short, Server> _server;

	static
	{
		Server._server = new HashMap<>();

		for (Server packet : Server.values())
			Server._server.put(packet.id(), packet);
	}

	private short id;
	private String mask;

	Server(int id, String mask)
	{
		this.id = (short)id;
		this.mask = mask;
	}

	public short id()
	{
		return this.id;
	}

	public String mask()
	{
		return this.mask;
	}

	public String toString()
	{
		return "Server packet " + this.id + " " + this.name() + " \"" + this.mask + "\"";
	}

	public static Server getById(short id)
	{
		return Server._server.get(id);
	}
}
