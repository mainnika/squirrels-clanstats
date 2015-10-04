package ru.mainnika.squirrels.clanstats.net.packets;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum ClanInfoParser
{
	ID(0, "I", "id"),
	INFO(1 << 0, "SSS", "info", 3),
	NEWS(1 << 1, "S", "news"),
	LEADER_ID(1 << 2, "I", "leader_id"),
	SIZE(1 << 3, "I", "size"),
	STATE(1 << 4, "B", "state"),
	RANK(1 << 5, "BII", "rank", 3),
	RANK_RANGE(1 << 6, "I", "rank_range"),
	PLACES(1 << 7, "I", "places"),
	BAN(1 << 8, "I", "ban"),
	TOTEMS(1 << 9, "[BIB]I", "totems", 2),
	TOTEMS_RANGS(1 << 10, "[BBII]", "totems_rangs"),
	TOTEMS_BONUSES(1 << 11, "[BB]", "totems_bonuses"),
	STATISICS(1 << 12, "[III]", "statistics"),
	BLACKLIST(1 << 13, "[I]", "blacklist"),
	LEVEL_LIMITER(1 << 14, "B", "level_limiter"),
	ADMIN_BALANCE(1 << 15, "II", "balance", 2);

	private Integer offset;
	private String mask;
	private Integer length;

	ClanInfoParser(Integer offset, String mask, String dummy)
	{
		this.offset = offset;
		this.mask = mask;
		this.length = 1;
	}

	ClanInfoParser(Integer offset, String mask, String dummy, Integer length)
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
