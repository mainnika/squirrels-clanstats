package ru.mainnika.squirrels.clanstats.net.packets;

import java.util.HashMap;

public enum Client
{
	ADMIN_REQUEST(1, "I"),
	ADMIN_REQUEST_CLAN(2, "I"),
	ADMIN_EDIT(3, "IBBSSSIIII[W][BB][BB]IBBBI[BBI][B][WB]I[BI]IB[BBBI]BWWB"),
	ADMIN_EDIT_CLAN(4, "ISSSSIIIIBII[BBI][BI]I"),
	ADMIN_CLOSE_CLAN(5, "I"),
	ADMIN_MESSAGE(6, "S"),
	ADMIN_CLEAR(7, "LB"),
	HELLO(8, ""),
	LOGIN(9, "LBSII,S"),
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
	EVENT_REMOVE(60, "L"),
	ROUND_ALIVE(61, ""),
	ROUND_HERO(62, "BFFFF,B"),
	ROUND_CAST_BEGIN(63, "BS"),
	ROUND_CAST_END(64, "BBB"),
	ROUND_NUT(65, "B"),
	ROUND_HOLLOW(66, "B,I"),
	ROUND_DIE(67, "FFB,I"),
	ROUND_RESPAWN(68, ",B"),
	ROUND_SYNC(69, "B[WFFFFFF]"),
	ROUND_WORLD(70, "IA"),
	ROUND_SKILL(71, "BBI"),
	ROUND_SKILL_ACTION(72, "BI,I"),
	ROUND_SKILL_SHAMAN(73, "BB"),
	ROUND_COMMAND(74, "S"),
	ROUND_ELEMENT(75, "B"),
	ROUND_VOTE(76, "IB"),
	ROUND_SMILE(77, "B"),
	ROUND_ZOMBIE(78, "I"),
	MAPS_LIST(79, "BBB"),
	MAPS_GET(80, "I"),
	MAPS_ADD(81, "BWIA,BB"),
	MAPS_EDIT(82, "IBBBB,WIA"),
	MAPS_REMOVE(83, "I"),
	MAPS_CLEAR_RATING(84, "I"),
	MAPS_CHECK(85, ",II"),
	CLAN_CREATE(86, "S"),
	CLAN_INFO(87, "SS"),
	CLAN_RENAME(88, "S"),
	CLAN_ACCEPT(89, "[I]B"),
	CLAN_LEAVE(90, ""),
	CLAN_INVITE(91, "I"),
	CLAN_JOIN(92, "I"),
	CLAN_KICK(93, "I"),
	CLAN_CLOSE(94, ""),
	CLAN_DONATION(95, "II"),
	CLAN_REQUEST(96, "[I]I"),
	CLAN_SUBSTITUTE(97, "I"),
	CLAN_UNSUBSTITUTE(98, "I"),
	CLAN_GET_TRANSACTIONS(99, ""),
	CLAN_GET_APPLICATION(100, ""),
	CLAN_GET_MEMBERS(101, "I"),
	CLAN_GET_ROOMS(102, ""),
	CHAT_ENTER(103, ""),
	CHAT_LEAVE(104, ""),
	CLAN_NEWS(105, "S"),
	CLAN_TOTEM(106, "BB"),
	CLAN_ADD_IN_BLACKLIST(107, "I"),
	CLAN_REMOVE_FROM_BLACKLIST(108, "I"),
	CLAN_LEVEL_LIMITER(109, "B"),
	COLOR(110, "B"),
	DISCOUNT_CLOTHES(111, "[W]"),
	DISCOUNT_USE(112, "BI"),
	INTERIOR_CHANGE(113, "[BB]"),
	LEARN_SHAMAN_SKILL(114, "B"),
	CHANGE_SHAMAN_BRANCH(115, "B"),
	TRANSFER(116, "B,LSS"),
	AB_GUI_ACTION(117, "B");

	private static HashMap<Short, Client> _client;

	static
	{
		Client._client = new HashMap<>();

		for (Client packet : Client.values())
			Client._client.put(packet.id, packet);
	}

	private short id;
	private String mask;

	Client(int id, String mask)
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
		return "Client packet " + this.id + " " + this.getClass().getName() + "\"" + this.mask + "\"";
	}

	public Client getById(short id)
	{
		return Client._client.get(id);
	}

}
