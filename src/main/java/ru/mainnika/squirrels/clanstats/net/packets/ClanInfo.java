package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum ClanInfo
{
	ID(0, "I"),
	INFO(1, "SSS"),
	NEWS(1 << 1, "S"),
	LEADER_ID(1 << 2, "I"),
	SIZE(1 << 3, "I"),
	STATE(1 << 4, "B"),
	RANK(1 << 5, "BII"),
	RANK_RANGE(1 << 6, "I"),
	PLACES(1 << 7, "I"),
	RATING(1 << 8, "II"),
	BAN(1 << 9, "I"),
	TOTEMS(1 << 10, "[BIB]I"),
	TOTEMS_RANGS(1 << 11, "[BBII]"),
	TOTEMS_BONUSES(1 << 12, "[BB]"),
	STATISICS(1 << 13, "[III]"),
	BLACKLIST(1 << 14, "[I]"),
	LEVEL_LIMITER(1 << 15, "B"),
	ADMIN_BALANCE(1 << 16, "II");

	private Integer offset;
	private String mask;

	ClanInfo(Integer offset, String mask)
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
		ClanInfo[] list = ClanInfo.values();
		String maskTotal = "";

		for (ClanInfo element : list)
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