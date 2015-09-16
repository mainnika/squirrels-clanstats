package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum PlayerInfo
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
	INFO(1 << 8, "ISI"),
	EXPERIENCE(1 << 9, "I"),
	CLOTHES(1 << 10, "[W]"),
	WEARED(1 << 11, "B[WB]"),
	CLAN(1 << 12, "I"),
	COLLECTION_SETS(1 << 13, "[BBI]"),
	COLLECTION_EXCHANGE(1 << 14, "[B]"),
	IS_GONE(1 << 15, "B"),
	CLAN_TOTEM(1 << 16, "I"),
	AWARD_STAT(1 << 17, "[BW]"),
	VIP_INFO(1 << 18, "IBB"),
	INTERIOR(1 << 19, "[B]"),
	DECORATIONS(1 << 20, "[B]"),
	SHAMAN_EXP(1 << 21, "I"),
	SHAMAN_SKILLS(1 << 22, "[BBB]"),
	RATING_INFO(1 << 23, "III"),
	RATING_HISTORY(1 << 24, "[WI]");

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
