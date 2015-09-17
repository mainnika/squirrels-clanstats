package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum ClanInfoParser
{
	ID(0, "I"),
	INFO(1 << 0, "SSS", 3),
	NEWS(1 << 1, "S"),
	LEADER_ID(1 << 2, "I"),
	SIZE(1 << 3, "I"),
	STATE(1 << 4, "B"),
	RANK(1 << 5, "BII", 3),
	RANK_RANGE(1 << 6, "I"),
	PLACES(1 << 7, "I"),
	BAN(1 << 8, "I"),
	TOTEMS(1 << 9, "[BIB]I", 2),
	TOTEMS_RANGS(1 << 10, "[BBII]"),
	TOTEMS_BONUSES(1 << 11, "[BB]"),
	STATISICS(1 << 12, "[III]"),
	BLACKLIST(1 << 13, "[I]"),
	LEVEL_LIMITER(1 << 14, "B"),
	ADMIN_BALANCE(1 << 15, "II", 2);

	private Integer offset;
	private String mask;
	private Integer length;

	ClanInfoParser(Integer offset, String mask)
	{
		this.offset = offset;
		this.mask = mask;
		this.length = 1;
	}

	ClanInfoParser(Integer offset, String mask, Integer length)
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
