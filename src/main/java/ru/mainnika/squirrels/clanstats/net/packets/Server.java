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
	CHAT_MESSAGE(49, "BIS"),
	CHAT_HISTORY(50, "B[IS]"),
	COLLECTION_ASSEMBLE(51, "BBB"),
	COLLECTION_EXCHANGE(52, "BIIBB"),
	ROOM_JOIN(53, "IB"),
	ROOM_LEAVE(54, "I"),
	ROOM_REQUEST_WORLD(55, "I"),
	ROOM_ROUND(56, "B,WIBIWAI"),
	ROOM_PRIVATE(57, "BI"),
	ROUND_HERO(58, "IBFFFF,B"),
	ROUND_CAST_BEGIN(59, "IBS"),
	ROUND_CAST_END(60, "IBBB"),
	ROUND_NUT(61, "IB"),
	ROUND_HOLLOW(62, "BIB,WB"),
	ROUND_DIE(63, "IFFB,I"),
	ROUND_RESPAWN(64, "BIB"),
	ROUND_TEAM(65, "[I][I]"),
	ROUND_FRAGS(66, "II"),
	ROUND_SYNCHRONIZER(67, "I"),
	ROUND_SHAMAN(68, "[I][B]"),
	ROUND_BEASTS(69, "[B[I]]"),
	ROUND_SYNC(70, "I[WFFFFFF]"),
	ROUND_WORLD(71, "A"),
	ROUND_SKILL(72, "IBB,BI"),
	ROUND_SKILL_ACTION(73, "BII,I"),
	ROUND_SKILL_SHAMAN(74, "IBB"),
	ROUND_COMMAND(75, "IS"),
	ROUND_COMPENSATION(76, "BI"),
	ROUND_ELEMENT(77, "IBB"),
	ROUND_ELEMENTS(78, "[B]"),
	ROUND_SMILE(79, "IB"),
	ROUND_ZOMBIE(80, "IB,I"),
	MAPS_LIST(81, "BBB[IBIII]I"),
	MAPS_MAP(82, "IBIWAIIIIII"),
	MAPS_ID(83, "I,BB"),
	MAPS_CHECK(84, "[II]"),
	CLAN_STATE(85, "B,I"),
	CLAN_INFO(86, "AI"),
	CLAN_APPLICATION(87, "[II]"),
	CLAN_BALANCE(88, "II"),
	CLAN_TRANSACTION(89, "[IBIIII]"),
	CLAN_MEMBERS(90, "I[I]"),
	CLAN_PRIVATE_ROOMS(91, "[IBBBW]"),
	CLAN_JOIN(92, "I,I"),
	CLAN_LEAVE(93, "I"),
	CLAN_SUBSTITUTE(94, "[I],B"),
	CLAN_TOTEM_BONUS(95, "B,W"),
	DISCOUNT_BONUS(96, "[BI],B"),
	DISCOUNT_CLOTHES(97, "[W]"),
	DISCOUNT_USE(98, "BI"),
	BRANCHES(99, "BB"),
	TRANSFER(100, "B,II");

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
		this.id = (short) id;
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
