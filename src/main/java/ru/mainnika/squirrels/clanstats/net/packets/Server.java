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
	DATA(9, "I[I]"),
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
	GIFTS(43, "[IBII]"),
	GIFTS_TARGET(44, "[I]"),
	GIFTS_ACCEPT(45, "BI,B"),
	CHAT_MESSAGE(46, "BIS"),
	CHAT_HISTORY(47, "B[IS]"),
	COLLECTION_ASSEMBLE(48, "BBB"),
	COLLECTION_EXCHANGE(49, "BIIBB"),
	ROOM_JOIN(50, "IB"),
	ROOM_LEAVE(51, "I"),
	ROOM_REQUEST_WORLD(52, "I"),
	ROOM_ROUND(53, "B,WIBIWWWAI"),
	ROOM_PRIVATE(54, "BI"),
	ROUND_HERO(55, "IBFFFF,B"),
	ROUND_CAST_BEGIN(56, "IBS"),
	ROUND_CAST_END(57, "IBBB"),
	ROUND_NUT(58, "IB"),
	ROUND_HOLLOW(59, "BIB,WB"),
	ROUND_DIE(60, "IFFB,I"),
	ROUND_RESPAWN(61, "BIB"),
	ROUND_TEAM(62, "[I][I]"),
	ROUND_FRAGS(63, "II"),
	ROUND_SYNCHRONIZER(64, "I"),
	ROUND_SHAMAN(65, "[I][B]"),
	ROUND_BEASTS(66, "[B[I]]"),
	ROUND_SYNC(67, "I[WFFFFFF]"),
	ROUND_WORLD(68, "A"),
	ROUND_SKILL(69, "IBB,BI"),
	ROUND_SKILL_ACTION(70, "BII,I"),
	ROUND_SKILL_SHAMAN(71, "IBB"),
	ROUND_COMMAND(72, "IS"),
	ROUND_COMPENSATION(73, "BI"),
	ROUND_ELEMENT(74, "IBB"),
	ROUND_ELEMENTS(75, "[B]"),
	ROUND_SMILE(76, "IB"),
	ROUND_ZOMBIE(77, "IB,I"),
	MAPS_LIST(78, "BBB[IBIII]I"),
	MAPS_MAP(79, "IBIWWWAIIIIII"),
	MAPS_ID(80, "I,BB"),
	MAPS_CHECK(81, "[II]"),
	CLAN_STATE(82, "B,I"),
	CLAN_INFO(83, "AI"),
	CLAN_APPLICATION(84, "[II]"),
	CLAN_BALANCE(85, "II"),
	CLAN_TRANSACTION(86, "[IBIIII]"),
	CLAN_MEMBERS(87, "I[I]"),
	CLAN_PRIVATE_ROOMS(88, "[IBBBW]"),
	CLAN_JOIN(89, "I,I"),
	CLAN_LEAVE(90, "I"),
	CLAN_SUBSTITUTE(91, "[I],B"),
	CLAN_TOTEM_BONUS(92, "B,W"),
	DISCOUNT_BONUS(93, "[BI],B"),
	DISCOUNT_CLOTHES(94, "[W]"),
	DISCOUNT_USE(95, "BI"),
	BRANCHES(96, "BB"),
	TRANSFER(97, "B,II"),
	OLYMPIC_SCORES(98, "[BW]"),
	OLYMPIC_DAY(99, "BI"),
	OLYMPIC_PRIZES(100, "B[B]"),
	OLYMPIC_RATING(101, "B[IW]"),
	OLYMPIC_PLACES(102, "[W]"),
	REPLAY_REQUEST(103, ""),
	REPLAY_LIST(104, "B,[IIBBWBI]"),
	REPLAY_DATA(105, "B,IIABIWWWA"),
	REPLAY_TAG(106, "B,IB");

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
