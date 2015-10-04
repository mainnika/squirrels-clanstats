package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum PlayerInfoParser
{
	INNER_ID(0, "I", "id"),
	NET_ID(1 << 0, "L", "nid"),
	TYPE(1 << 1, "B", "type"),
	MODERATOR(1 << 2, "B", "moderator"),
	EDITOR(1 << 3, "B", "editor"),
	NAME(1 << 4, "S", "name"),
	SEX(1 << 5, "B", "sex"),
	PHOTO(1 << 6, "S", "photo_big"),
	ONLINE(1 << 7, "B", "online"),
	INFO(1 << 8, "ISI", "person_info", 3),
	EXPERIENCE(1 << 9, "I", "exp"),
	CLOTHES(1 << 10, "[W]", "clothes"),
	WEARED(1 << 11, "B[WB]", "weared_info", 2),
	CLAN(1 << 12, "I", "clan_id"),
	COLLECTION_SETS(1 << 13, "[BBI]", "collection_sets"),
	COLLECTION_EXCHANGE(1 << 14, "[B]", "collection_exchange"),
	IS_GONE(1 << 15, "B", "is_gone"),
	CLAN_TOTEM(1 << 16, "I", "clan_totem"),
	AWARD_STAT(1 << 17, "[BW]", "award_stat"),
	VIP_INFO(1 << 18, "IBB", "vip_info", 3),
	INTERIOR(1 << 19, "[B]", "interior"),
	DECORATIONS(1 << 20, "[B]", "decorations"),
	SHAMAN_EXP(1 << 21, "I", "shaman_exp"),
	SHAMAN_SKILLS(1 << 22, "[BBB]", "shaman_skills"),
	RATING_INFO(1 << 23, "III", "rating_info", 3),
	RATING_HISTORY(1 << 24, "[WI]", "rating_history");

	private Integer offset;
	private String mask;
	private Integer length;

	PlayerInfoParser(Integer offset, String mask, String dummy)
	{
		this.offset = offset;
		this.mask = mask;
		this.length = 1;
	}

	PlayerInfoParser(Integer offset, String mask, String dummy, Integer length)
	{
		this.offset = offset;
		this.mask = mask;
		this.length = length;
	}

	public Integer id()
	{
		return this.offset;
	}

	public String mask()
	{
		return this.mask;
	}

	public Integer length()
	{
		return this.length;
	}

	public static Group parse(byte[] raw, String mask)
	{
		ByteBuffer wrapped = ByteBuffer.wrap(raw);
		wrapped.order(ByteOrder.LITTLE_ENDIAN);

		int groupLen = wrapped.getInt();

		return Packet.parser(mask, wrapped, groupLen, false);
	}
}
