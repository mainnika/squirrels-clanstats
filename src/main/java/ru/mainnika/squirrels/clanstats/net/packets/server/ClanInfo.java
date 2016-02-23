package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.Packet;
import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClanInfo extends ServerPacket implements Packet.Readable
{
	public static final int INFO = 1 << 0;
	public static final int NEWS = 1 << 1;
	public static final int LEADER_ID = 1 << 2;
	public static final int SIZE = 1 << 3;
	public static final int STATE = 1 << 4;
	public static final int RANK = 1 << 5;
	public static final int RANK_RANGE = 1 << 6;
	public static final int PLACES = 1 << 7;
	public static final int BAN = 1 << 8;
	public static final int TOTEMS = 1 << 9;
	public static final int TOTEMS_RANGS = 1 << 10;
	public static final int TOTEMS_BONUSES = 1 << 11;
	public static final int STATISICS = 1 << 12;
	public static final int BLACKLIST = 1 << 13;
	public static final int LEVEL_LIMITER = 1 << 14;
	public static final int ADMIN_BALANCE = 1 << 15;

	public static class Clan implements Readable
	{
		public class Info implements Readable
		{
			public String noname1;
			public String photo;
			public String emblem;

			@Override
			public Info read(ByteBuffer buffer)
			{
				this.noname1 = readS(buffer);
				this.photo = readS(buffer);
				this.emblem = readS(buffer);

				return this;
			}

			@Override
			public Info clone()
			{
				Info clone = new Info();

				clone.noname1 = this.noname1;
				clone.photo = this.photo;
				clone.emblem = this.emblem;

				return clone;
			}
		}

		public class Rank implements Readable
		{
			public Byte level;
			public Integer experience;
			public Integer experienceLimit;

			@Override
			public Rank read(ByteBuffer buffer)
			{
				this.level = readB(buffer);
				this.experience = readI(buffer);
				this.experienceLimit = readI(buffer);

				return this;
			}

			@Override
			public Rank clone()
			{
				Rank clone = new Rank();

				clone.level = this.level;
				clone.experience = this.experience;
				clone.experienceLimit = this.experienceLimit;

				return clone;
			}
		}

		public class Totems implements Readable
		{
			public class Totem implements Readable
			{
				public Byte totemId;
				public Integer expires;
				public Byte noname;

				@Override
				public Totem read(ByteBuffer buffer)
				{
					this.totemId = readB(buffer);
					this.expires = readI(buffer);
					this.noname = readB(buffer);

					return this;
				}

				@Override
				public Totem clone()
				{
					Totem clone = new Totem();

					clone.totemId = this.totemId;
					clone.expires = this.expires;
					clone.noname = this.noname;

					return clone;
				}
			}

			public Group<Totem> totems;
			public Integer noname;

			public Totems()
			{
				this.totems = new Group<>(new Totem());
			}

			@Override
			public Totems read(ByteBuffer buffer)
			{
				this.totems = new Group<>(new Totem()).read(buffer);
				this.noname = readI(buffer);

				return this;
			}

			@Override
			public Totems clone()
			{
				Totems clone = new Totems();

				clone.totems = this.totems.clone();
				clone.noname = this.noname;

				return clone;
			}
		}

		public class TotemsRang implements Readable
		{
			public Byte noname1;
			public Byte noname2;
			public Integer noname3;
			public Integer noname4;

			@Override
			public TotemsRang read(ByteBuffer buffer)
			{
				this.noname1 = readB(buffer);
				this.noname2 = readB(buffer);
				this.noname3 = readI(buffer);
				this.noname4 = readI(buffer);

				return this;
			}

			@Override
			public TotemsRang clone()
			{
				TotemsRang clone = new TotemsRang();

				clone.noname1 = this.noname1;
				clone.noname2 = this.noname2;
				clone.noname3 = this.noname3;
				clone.noname4 = this.noname4;

				return clone;
			}
		}

		public class TotemsBonus implements Readable
		{
			public Byte noname1;
			public Byte noname2;

			@Override
			public TotemsBonus read(ByteBuffer buffer)
			{
				this.noname1 = readB(buffer);
				this.noname2 = readB(buffer);

				return this;
			}

			@Override
			public TotemsBonus clone()
			{
				TotemsBonus clone = new TotemsBonus();

				clone.noname1 = this.noname1;
				clone.noname2 = this.noname2;

				return clone;
			}
		}

		public class Statisic implements Readable
		{
			public Integer innerId;
			public Integer clanExp;
			public Integer playerExp;

			@Override
			public Statisic read(ByteBuffer buffer)
			{
				this.innerId = readI(buffer);
				this.clanExp = readI(buffer);
				this.playerExp = readI(buffer);

				return this;
			}

			@Override
			public Statisic clone()
			{
				Statisic clone = new Statisic();

				clone.innerId = this.innerId;
				clone.clanExp = this.clanExp;
				clone.playerExp = this.playerExp;

				return clone;
			}
		}

		public class Balance implements Readable
		{
			public Integer nuts;
			public Integer coins;

			@Override
			public Balance read(ByteBuffer buffer)
			{
				this.nuts = readI(buffer);
				this.coins = readI(buffer);

				return this;
			}

			@Override
			public Balance clone()
			{
				Balance clone = new Balance();

				clone.nuts = this.nuts;
				clone.coins = this.coins;

				return clone;
			}
		}

		private int mask;

		public Integer clanId;
		public Info info;
		public String news;
		public Integer leaderId;
		public Integer size;
		public Byte state;
		public Rank rank;
		public Integer rankRange;
		public Integer places;
		public Integer ban;
		public Totems totems;
		public Group<TotemsRang> totemsRangs;
		public Group<TotemsBonus> totemsBonuses;
		public Group<Statisic> statisics;
		public Group<Integer> blacklist;
		public Byte levelLimiter;
		public Balance balance;

		public Clan(int mask)
		{
			this.mask = mask;

			this.info = new Info();
			this.rank = new Rank();
			this.totems = new Totems();
			this.totemsRangs = new Group<>(new TotemsRang());
			this.totemsBonuses = new Group<>(new TotemsBonus());
			this.statisics = new Group<>(new Statisic());
			this.blacklist = new Group<>((int) 0);
			this.balance = new Balance();
		}

		@Override
		public Clan read(ByteBuffer buffer)
		{
			this.clanId = readI(buffer);

			if ((mask & INFO) > 0)
				this.info = new Info().read(buffer);

			if ((mask & NEWS) > 0)
				this.news = readS(buffer);

			if ((mask & LEADER_ID) > 0)
				this.leaderId = readI(buffer);

			if ((mask & SIZE) > 0)
				this.size = readI(buffer);

			if ((mask & STATE) > 0)
				this.state = readB(buffer);

			if ((mask & RANK) > 0)
				this.rank = new Rank().read(buffer);

			if ((mask & RANK_RANGE) > 0)
				this.rankRange = readI(buffer);

			if ((mask & PLACES) > 0)
				this.places = readI(buffer);

			if ((mask & BAN) > 0)
				this.ban = readI(buffer);

			if ((mask & TOTEMS) > 0)
				this.totems = new Totems().read(buffer);

			if ((mask & TOTEMS_RANGS) > 0)
				this.totemsRangs = new Group<>(new TotemsRang()).read(buffer);

			if ((mask & TOTEMS_BONUSES) > 0)
				this.totemsBonuses = new Group<>(new TotemsBonus()).read(buffer);

			if ((mask & STATISICS) > 0)
				this.statisics = new Group<>(new Statisic()).read(buffer);

			if ((mask & BLACKLIST) > 0)
				this.blacklist = new Group<>((int) 0).read(buffer);

			if ((mask & LEVEL_LIMITER) > 0)
				this.levelLimiter = readB(buffer);

			if ((mask & ADMIN_BALANCE) > 0)
				this.balance = new Balance().read(buffer);

			return this;
		}

		@Override
		public Clan clone()
		{
			Clan clone = new Clan(this.mask);

			clone.news = this.news;
			clone.leaderId = this.leaderId;
			clone.size = this.size;
			clone.state = this.state;
			clone.rank = this.rank.clone();
			clone.rankRange = this.rankRange;
			clone.places = this.places;
			clone.ban = this.ban;
			clone.totems = this.totems.clone();
			clone.totemsRangs = this.totemsRangs.clone();
			clone.totemsBonuses = this.totemsBonuses.clone();
			clone.statisics = this.statisics.clone();
			clone.blacklist = this.blacklist.clone();
			clone.levelLimiter = this.levelLimiter;
			clone.balance = this.balance.clone();

			return clone;
		}
	}

	public byte[] raw;
	public Integer mask;

	public Group<Clan> clans;

	@Override
	public Readable read(ByteBuffer buffer)
	{
		this.raw = readA(buffer);
		this.mask = readI(buffer);

		ByteBuffer infoBuffer = ByteBuffer.wrap(this.raw);
		infoBuffer.order(ByteOrder.LITTLE_ENDIAN);

		this.clans = new Group<>(new Clan(this.mask)).read(infoBuffer);

		return this;
	}
}
