package ru.mainnika.squirrels.clanstats.net.packets;

import java.util.HashMap;

public enum ClientParser
{
	ADMIN_REQUEST(1, "I"),
	ADMIN_REQUEST_CLAN(2, "I"),
	ADMIN_EDIT(3, "IBBSSSIIII[W][BB][BB]IBBBI[BBI][B][WB]I[BI]IB[BBBI]BWWB"),
	ADMIN_EDIT_CLAN(4, "ISSSSIIIIBII[BBI][BI]I"),
	ADMIN_CLOSE_CLAN(5, "I"),
	ADMIN_MESSAGE(6, "S"),
	ADMIN_CLEAR(7, "LB"),
	HELLO(8, ""),
	LOGIN(9, "LBBSII,S"),
	PLAY(10, "B,B"),
	PLAY_CANCEL(11, ""),
	PLAY_WITH(12, "I"),
	SPY_FOR(13, "I"),
	PLAY_ROOM(14, "I"),
	LEAVE(15, ""),
	REQUEST(16, "[I]I"),
	REQUEST_NET(17, "[L]BI"),
	INFO(18, "SBISSS,S"),
	REFILL(19, ""),
	BAN(20, "IBB"),
	BUY(21, "III,II"),
	PING(22, "B"),
	LATENCY(23, ",I"),
	COUNT(24, "I,L"),
	FLAGS_SET(25, "BB"),
	INVITE(26, "L"),
	WEAR(27, "[WB]"),
	CLEAR_TEMPORARY(28, "B"),
	ACHIEVEMENT(29, "BB,I"),
	GUARD(30, "S"),
	REPOST_NEWS(31, "B,W"),
	REQUEST_AWARD(32, ""),
	RENAME(33, "S"),
	SIGN_XSOLLA(34, "[SS]"),
	BIRTHDAY_CELEBRATE(35, ""),
	EVERY_DAY_BONUS_GET(36, ""),
	GOLDEN_CUP_REQUEST(37, "B"),
	STORAGE_SET(38, "BA"),
	TRAINING_SET(39, "BB"),
	REQUEST_CLOSEOUT(40, ""),
	PROMO_CODE(41, "S"),
	RATING_REQUEST(42, ""),
	RATING_REQUEST_TOP(43, "B"),
	SOCIAL_FRIENDS(44, "[L]"),
	FRIENDS_ADD(45, "[I]"),
	FRIENDS_REMOVE(46, "I"),
	FRIENDS_RETURN(47, "[I]"),
	FRIENDS_ONLINE(48, ""),
	DAILY_QUEST_REQUEST(49, ""),
	DAILY_QUEST_ADD(50, "BB"),
	DAILY_QUEST_COMPLETE(51, "B"),
	CHAT_MESSAGE(52, "BS"),
	CHAT_COMMAND(53, "BB"),
	COLLECTION_ASSEMBLE(54, "BB"),
	COLLECTION_EXCHANGE_ADD(55, "[B]"),
	COLLECTION_EXCHANGE_REMOVE(56, "[B]"),
	COLLECTION_EXCHANGE(57, "IBB"),
	GIFT_SEND(58, "[I]"),
	GIFT_ACCEPT(59, "BI"),
	DEFERRED_BONUS_ACCEPT(60, "I"),
	EVENT_REMOVE(61, "L"),
	ROUND_ALIVE(62, ""),
	ROUND_HERO(63, "BFFFF,B"),
	ROUND_CAST_BEGIN(64, "BS"),
	ROUND_CAST_END(65, "BBB"),
	ROUND_NUT(66, "B"),
	ROUND_HOLLOW(67, "B,I"),
	ROUND_DIE(68, "FFB,I"),
	ROUND_RESPAWN(69, ",B"),
	ROUND_SYNC(70, "B[WFFFFFF]"),
	ROUND_WORLD(71, "IA"),
	ROUND_SKILL(72, "BBI"),
	ROUND_SKILL_ACTION(73, "BI,I"),
	ROUND_SKILL_SHAMAN(74, "BB"),
	ROUND_COMMAND(75, "S"),
	ROUND_ELEMENT(76, "B"),
	ROUND_VOTE(77, "IB"),
	ROUND_SMILE(78, "B"),
	ROUND_ZOMBIE(79, "I"),
	MAPS_LIST(80, "BBB"),
	MAPS_GET(81, "I"),
	MAPS_ADD(82, "BWIA,BB"),
	MAPS_EDIT(83, "IBBBB,WIA"),
	MAPS_REMOVE(84, "I"),
	MAPS_CLEAR_RATING(85, "I"),
	MAPS_CHECK(86, ",II"),
	CLAN_CREATE(87, "S"),
	CLAN_INFO(88, "SS"),
	CLAN_RENAME(89, "S"),
	CLAN_ACCEPT(90, "[I]B"),
	CLAN_LEAVE(91, ""),
	CLAN_INVITE(92, "I"),
	CLAN_JOIN(93, "I"),
	CLAN_KICK(94, "I"),
	CLAN_CLOSE(95, ""),
	CLAN_DONATION(96, "II"),
	CLAN_REQUEST(97, "[I]I"),
	CLAN_SUBSTITUTE(98, "I"),
	CLAN_UNSUBSTITUTE(99, "I"),
	CLAN_GET_TRANSACTIONS(100, ""),
	CLAN_GET_APPLICATION(101, ""),
	CLAN_GET_MEMBERS(102, "I"),
	CLAN_GET_ROOMS(103, ""),
	CHAT_ENTER(104, ""),
	CHAT_LEAVE(105, ""),
	CLAN_NEWS(106, "S"),
	CLAN_TOTEM(107, "BB"),
	CLAN_ADD_IN_BLACKLIST(108, "I"),
	CLAN_REMOVE_FROM_BLACKLIST(109, "I"),
	CLAN_LEVEL_LIMITER(110, "B"),
	COLOR(111, "B"),
	DISCOUNT_CLOTHES(112, "[W]"),
	DISCOUNT_USE(113, "BI"),
	INTERIOR_CHANGE(114, "[BB]"),
	LEARN_SHAMAN_SKILL(115, "B"),
	CHANGE_SHAMAN_BRANCH(116, "B"),
	TRANSFER(117, "B,LSS"),
	AB_GUI_ACTION(118, "B");

	private static HashMap<Short, ClientParser> _client;

	static
	{
		ClientParser._client = new HashMap<>();

		for (ClientParser packet : ClientParser.values())
			ClientParser._client.put(packet.id, packet);
	}

	private short id;
	private String mask;

	ClientParser(int id, String mask)
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
		return "ClientParser packet " + this.id + " " + this.getClass().getName() + "\"" + this.mask + "\"";
	}

	public ClientParser getById(short id)
	{
		return ClientParser._client.get(id);
	}

}
