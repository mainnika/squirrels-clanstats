package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.packets.server.*;

import java.util.HashMap;

public enum ServerParser
{
	HELLO(1, ""),
	GUARD(2, "A"),
	ADMIN_INFO(3, "IIIIIII[BB][BB]BBII[B]I[BI]B[BBBI]WWB"),
	ADMIN_MESSAGE(4, "S"),
	LOGIN(5, "B,IIIBBSIIII[W]I[W][BI][BBB]I"),
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
	EXPIRATIONS(19, "[BBI]"),
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
	BUNDLES(43, "B[B][WI]"),
	RATING_SCORES(44, "BI"),
	RATING_DIVISION(45, "BBI,I[I]"),
	RATING_TRANSITION(46, "BBI"),
	RATING_TOP(47, "B[I]"),
	GIFTS(48, "[IBII]"),
	GIFTS_TARGET(49, "[I]"),
	GIFTS_ACCEPT(50, "BI,B"),
	DEFERRED_BONUS(51, "[IBIIWWI]"),
	DEFERRED_BONUS_ACCEPT(52, "BI,BWWI"),
	CHAT_MESSAGE(53, "BIS"),
	CHAT_HISTORY(54, "B[IS]"),
	COLLECTION_ASSEMBLE(55, "BBB"),
	COLLECTION_EXCHANGE(56, "BIIBB"),
	ROOM_JOIN(57, "IB"),
	ROOM_LEAVE(58, "I"),
	ROOM_REQUEST_WORLD(59, "I"),
	ROOM_ROUND(60, "B,WIBIWAI"),
	ROOM_PRIVATE(61, "BI"),
	ROUND_HERO(62, "IBFFFF,B"),
	ROUND_CAST_BEGIN(63, "IBS"),
	ROUND_CAST_END(64, "IBBB"),
	ROUND_NUT(65, "IB"),
	ROUND_HOLLOW(66, "BIB,WB"),
	ROUND_DIE(67, "IFFB,I"),
	ROUND_RESPAWN(68, "BIB"),
	ROUND_TEAM(69, "[I][I]"),
	ROUND_FRAGS(70, "II"),
	ROUND_SYNCHRONIZER(71, "I"),
	ROUND_SHAMAN(72, "[I][B]"),
	ROUND_BEASTS(73, "[B[I]]"),
	ROUND_SYNC(74, "I[WFFFFFF]"),
	ROUND_WORLD(75, "A"),
	ROUND_SKILL(76, "IBB,BI"),
	ROUND_SKILL_ACTION(77, "BII,I"),
	ROUND_SKILL_SHAMAN(78, "IBB"),
	ROUND_COMMAND(79, "IS"),
	ROUND_COMPENSATION(80, "BI"),
	ROUND_ELEMENT(81, "IBB"),
	ROUND_ELEMENTS(82, "[B]"),
	ROUND_SMILE(83, "IB"),
	ROUND_ZOMBIE(84, "IB,I"),
	MAPS_LIST(85, "BBB[IBIII]I"),
	MAPS_MAP(86, "IBIWAIIIIII"),
	MAPS_ID(87, "I,BB"),
	MAPS_CHECK(88, "[II]"),
	CLAN_STATE(89, "B,I"),
	CLAN_INFO(90, "AI"),
	CLAN_APPLICATION(91, "[II]"),
	CLAN_BALANCE(92, "II"),
	CLAN_TRANSACTION(93, "[IBIIII]"),
	CLAN_MEMBERS(94, "I[I]"),
	CLAN_PRIVATE_ROOMS(95, "[IBBBW]"),
	CLAN_JOIN(96, "I,I"),
	CLAN_LEAVE(97, "I"),
	CLAN_SUBSTITUTE(98, "[I],B"),
	CLAN_TOTEM_BONUS(99, "B,W"),
	DISCOUNT_BONUS(100, "[BI],B"),
	DISCOUNT_CLOTHES(101, "[W]"),
	DISCOUNT_USE(102, "BI"),
	BRANCHES(103, "BB"),
	TRANSFER(104, "B,II");

	private static HashMap<Short, ServerParser> _server;

	static
	{
		ServerParser._server = new HashMap<>();

		for (ServerParser packet : ServerParser.values())
			ServerParser._server.put(packet.id(), packet);

		HELLO.setSpecialize(Hello.class);
		GUARD.setSpecialize(Guard.class);
		LOGIN.setSpecialize(Login.class);
		INFO.setSpecialize(PlayerInfo.class);
		INFO_NET.setSpecialize(PlayerInfo.class);
		CHAT_MESSAGE.setSpecialize(ChatMessage.class);
		CLAN_INFO.setSpecialize(ClanInfo.class);
		CLAN_BALANCE.setSpecialize(ClanBalance.class);
		CLAN_MEMBERS.setSpecialize(ClanMembers.class);
	}

	private short id;
	private String mask;
	private Class specialize;

	ServerParser(int id, String mask)
	{
		this.id = (short) id;
		this.mask = mask;
		this.specialize = null;
	}

	private void setSpecialize(Class specializer)
	{
		this.specialize = specializer;
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
