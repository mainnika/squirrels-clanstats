package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum PlayerInfo
{
	INNER_ID(0, "I"),
	NET_ID(1, "L"),
	TYPE(1 << 1, "B"),
	MODERATOR(1 << 2, "B"),
	EDITOR(1 << 3, "B"),
	NAME(1 << 4, "S"),
	SEX(1 << 5, "B"),
	PHOTO(1 << 6, "S"),
	ONLINE(1 << 7, "B"),
	INFO(1 << 8, "ISIIIIIIIIII"),
	LEVEL_PLACE(1 << 9, "I"),
	EXPERIENCE(1 << 10, "I"),
	CLOTHES(1 << 11, "[W]"),
	WEARED(1 << 12, "B[WB]"),
	CLAN(1 << 13, "I"),
	RATING_SCORE(1 << 14, "I"),
	RATING_WEEKLY_SCORE(1 << 15, "I"),
	COLLECTION_SETS(1 << 16, "[BBI]"),
	COLLECTION_EXCHANGE(1 << 17, "[B]"),
	IS_GONE(1 << 18, "B"),
	CLAN_TOTEM(1 << 19, "I"),
	AWARD_STAT(1 << 20, "[BW]"),
	VIP_INFO(1 << 21, "IBB"),
	INTERIOR(1 << 22, "[B]"),
	DECORATIONS(1 << 23, "[B]"),
	SHAMAN_EXP(1 << 24, "I"),
	SHAMAN_SKILLS(1 << 25, "[BBB]");

	private Integer offset;
	private String mask;

	PlayerInfo(Integer offset, String mask)
	{
		this.offset = offset;
		this.mask = mask;
	}

	public Integer id()
	{
		return this.offset;
	}

	public String mask()
	{
		return this.mask;
	}

	public static Group get(byte[] raw, int mask)
	{
		PlayerInfo[] list = PlayerInfo.values();
		String maskTotal = "";

		for (PlayerInfo element : list)
		{
			if ((mask & element.offset) == element.offset)
			{
				maskTotal += element.mask();
			}
		}

		ByteBuffer wrapped = ByteBuffer.wrap(raw);

		wrapped.order(ByteOrder.LITTLE_ENDIAN);

		int groupLen = wrapped.getInt();

		return Packet.parser(maskTotal, wrapped, groupLen, false);
	}
}
