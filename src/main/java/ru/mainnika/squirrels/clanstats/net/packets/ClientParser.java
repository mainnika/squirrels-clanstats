package ru.mainnika.squirrels.clanstats.net.packets;

import java.util.HashMap;

public enum ClientParser
{
	ADMIN_REQUEST(1, "I"),
	ADMIN_REQUEST_CLAN(2, "I"),
	ADMIN_EDIT(3, "IBBSSSIIII[W][BB][BB]IBBBI[BBI][W][WB]I[BI]IB[BBBI]BWWB"),
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
	BUNDLE_NEWBIE_ACTIVATE(42, ""),
	HOLIDAY_LOTTERY(43, ""),
	RATING_REQUEST(44, ""),
	RATING_REQUEST_TOP(45, "B"),
	SOCIAL_FRIENDS(46, "[L]"),
	FRIENDS_ADD(47, "[I]"),
	FRIENDS_REMOVE(48, "I"),
	FRIENDS_RETURN(49, "[I]"),
	FRIENDS_ONLINE(50, ""),
	DAILY_QUEST_REQUEST(51, ""),
	DAILY_QUEST_ADD(52, "BB"),
	DAILY_QUEST_COMPLETE(53, "B"),
	CHAT_MESSAGE(54, "BS"),
	CHAT_COMMAND(55, "BB"),
	COLLECTION_ASSEMBLE(56, "BB"),
	COLLECTION_EXCHANGE_ADD(57, "[B]"),
	COLLECTION_EXCHANGE_REMOVE(58, "[B]"),
	COLLECTION_EXCHANGE(59, "IBB"),
	GIFT_SEND(60, "[I]"),
	GIFT_ACCEPT(61, "BI"),
	DEFERRED_BONUS_ACCEPT(62, "I"),
	EVENT_REMOVE(63, "L"),
	ROUND_ALIVE(64, ""),
	ROUND_HERO(65, "BFFFF,B"),
	ROUND_CAST_BEGIN(66, "BS"),
	ROUND_CAST_END(67, "BBB"),
	ROUND_NUT(68, "B"),
	ROUND_HOLLOW(69, "B,I"),
	ROUND_DIE(70, "FFB,I"),
	ROUND_RESPAWN(71, ",B"),
	ROUND_SYNC(72, "B[WFFFFFF]"),
	ROUND_WORLD(73, "IA"),
	ROUND_SKILL(74, "BBI"),
	ROUND_SKILL_ACTION(75, "BI,I"),
	ROUND_SKILL_SHAMAN(76, "BB"),
	ROUND_COMMAND(77, "S"),
	ROUND_ELEMENT(78, "B"),
	ROUND_VOTE(79, "IB"),
	ROUND_SMILE(80, "B"),
	ROUND_ZOMBIE(81, "I"),
	MAPS_LIST(82, "BBB"),
	MAPS_GET(83, "I"),
	MAPS_ADD(84, "BWIA,BB"),
	MAPS_EDIT(85, "IBBBB,WIA"),
	MAPS_REMOVE(86, "I"),
	MAPS_CLEAR_RATING(87, "I"),
	MAPS_CHECK(88, ",II"),
	CLAN_CREATE(89, "S"),
	CLAN_INFO(90, "SS"),
	CLAN_RENAME(91, "S"),
	CLAN_ACCEPT(92, "[I]B"),
	CLAN_LEAVE(93, ""),
	CLAN_INVITE(94, "I"),
	CLAN_JOIN(95, "I"),
	CLAN_KICK(96, "I"),
	CLAN_CLOSE(97, ""),
	CLAN_DONATION(98, "II"),
	CLAN_REQUEST(99, "[I]I"),
	CLAN_SUBSTITUTE(100, "I"),
	CLAN_UNSUBSTITUTE(101, "I"),
	CLAN_GET_TRANSACTIONS(102, ""),
	CLAN_GET_APPLICATION(103, ""),
	CLAN_GET_MEMBERS(104, "I"),
	CLAN_GET_ROOMS(105, ""),
	CHAT_ENTER(106, ""),
	CHAT_LEAVE(107, ""),
	CLAN_NEWS(108, "S"),
	CLAN_TOTEM(109, "BB"),
	CLAN_ADD_IN_BLACKLIST(110, "I"),
	CLAN_REMOVE_FROM_BLACKLIST(111, "I"),
	CLAN_LEVEL_LIMITER(112, "B"),
	COLOR(113, "B"),
	DISCOUNT_CLOTHES(114, "[W]"),
	DISCOUNT_USE(115, "BI"),
	INTERIOR_CHANGE(116, "[BB]"),
	LEARN_SHAMAN_SKILL(117, "B"),
	CHANGE_SHAMAN_BRANCH(118, "B"),
	TRANSFER(119, "B,LSS"),
	AB_GUI_ACTION(120, "B");

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
