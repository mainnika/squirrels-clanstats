package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum PlayerInfoParser
{
	INNER_ID(0, "I"),
	NET_ID(1 << 0, "L"),
	TYPE(1 << 1, "B"),
	MODERATOR(1 << 2, "B"),
	EDITOR(1 << 3, "B"),
	NAME(1 << 4, "S"),
	SEX(1 << 5, "B"),
	PHOTO(1 << 6, "S"),
	ONLINE(1 << 7, "B"),
	INFO(1 << 8, "ISI", 3),
	EXPERIENCE(1 << 9, "I"),
	CLOTHES(1 << 10, "[W]"),
	WEARED(1 << 11, "B[WB]", 2),
	CLAN(1 << 12, "I"),
	COLLECTION_SETS(1 << 13, "[BBI]"),
	COLLECTION_EXCHANGE(1 << 14, "[B]"),
	IS_GONE(1 << 15, "B"),
	CLAN_TOTEM(1 << 16, "I"),
	AWARD_STAT(1 << 17, "[BW]"),
	VIP_INFO(1 << 18, "IBB", 3),
	INTERIOR(1 << 19, "[B]"),
	DECORATIONS(1 << 20, "[B]"),
	SHAMAN_EXP(1 << 21, "I"),
	SHAMAN_SKILLS(1 << 22, "[BBB]"),
	RATING_INFO(1 << 23, "III", 3),
	RATING_HISTORY(1 << 24, "[WI]");

	private Integer offset;
	private String mask;
	private Integer length;

	PlayerInfoParser(Integer offset, String mask)
	{
		this.offset = offset;
		this.mask = mask;
		this.length = 1;
	}

	PlayerInfoParser(Integer offset, String mask, Integer length)
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
