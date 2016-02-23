package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.packets.ServerPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Info extends ServerPacket
{
	static final int NET_ID = 1 << 0;
	static final int TYPE = 1 << 1;
	static final int MODERATOR = 1 << 2;
	static final int NAME = 1 << 3;
	static final int SEX = 1 << 4;
	static final int PHOTO = 1 << 5;
	static final int ONLINE = 1 << 6;
	static final int INFO = 1 << 7;
	static final int EXPERIENCE = 1 << 8;
	static final int WEARED = 1 << 9;
	static final int CLAN = 1 << 10;
	static final int COLLECTION_EXCHANGE = 1 << 11;
	static final int IS_GONE = 1 << 12;
	static final int CLAN_TOTEM = 1 << 13;
	static final int VIP_INFO = 1 << 14;
	static final int INTERIOR = 1 << 15;
	static final int SHAMAN_EXP = 1 << 16;
	static final int SHAMAN_SKILLS = 1 << 17;
	static final int RATING_INFO = 1 << 18;
	static final int RATING_HISTORY = 1 << 19;
	static final int RATING_HOLIDAY = 1 << 20;
	static final int TROPHIES = 1 << 21;

	public static class Player implements Readable
	{
		public class PersonInfo implements Readable
		{
			public Integer noname1;
			public String profile;
			public Integer noname3;

			@Override
			public PersonInfo read(ByteBuffer buffer)
			{
				this.noname1 = readI(buffer);
				this.profile = readS(buffer);
				this.noname3 = readI(buffer);

				return this;
			}

			@Override
			public PersonInfo clone()
			{
				PersonInfo clone = new PersonInfo();

				clone.noname1 = this.noname1;
				clone.profile = this.profile;
				clone.noname3 = this.noname3;

				return clone;
			}
		}

		public class Weared implements Readable
		{
			public Group<Short> weared1;
			public Group<Short> weared2;

			public Weared()
			{
				this.weared1 = new Group<>((short) 0);
				this.weared2 = new Group<>((short) 0);
			}

			@Override
			public Weared read(ByteBuffer buffer)
			{
				this.weared1 = new Group<>((short) 0).read(buffer);
				this.weared2 = new Group<>((short) 0).read(buffer);

				return this;
			}

			@Override
			public Weared clone()
			{
				Weared clone = new Weared();

				clone.weared1 = (Group<Short>) this.weared1.clone();
				clone.weared2 = (Group<Short>) this.weared2.clone();

				return clone;
			}
		}

		public class Vip implements Readable
		{
			public Byte isVip;
			public Byte vipColor;

			@Override
			public Vip read(ByteBuffer buffer)
			{
				this.isVip = readB(buffer);
				this.vipColor = readB(buffer);

				return this;
			}

			@Override
			public Vip clone()
			{
				Vip clone = new Vip();

				clone.isVip = this.isVip;
				clone.vipColor = this.vipColor;

				return clone;
			}
		}

		public class ShamanSkill implements Readable
		{
			public Byte skillId;
			public Byte freeLevel;
			public Byte paidLevel;

			@Override
			public ShamanSkill read(ByteBuffer buffer)
			{
				this.skillId = readB(buffer);
				this.freeLevel = readB(buffer);
				this.paidLevel = readB(buffer);

				return this;
			}

			@Override
			public ShamanSkill clone()
			{
				ShamanSkill clone = new ShamanSkill();

				clone.skillId = this.skillId;
				clone.freeLevel = this.freeLevel;
				clone.paidLevel = this.paidLevel;

				return clone;
			}
		}

		public class RatingInfo implements Readable
		{
			public Integer noname1;
			public Integer noname2;
			public Integer noname3;

			@Override
			public RatingInfo read(ByteBuffer buffer)
			{
				this.noname1 = readI(buffer);
				this.noname2 = readI(buffer);
				this.noname3 = readI(buffer);

				return this;
			}

			@Override
			public RatingInfo clone()
			{
				RatingInfo clone = new RatingInfo();

				clone.noname1 = this.noname1;
				clone.noname2 = this.noname2;
				clone.noname3 = this.noname3;

				return clone;
			}
		}

		public class RatingHistory implements Readable
		{
			public Byte noname1;
			public Byte noname2;

			@Override
			public RatingHistory read(ByteBuffer buffer)
			{
				this.noname1 = readB(buffer);
				this.noname2 = readB(buffer);

				return this;
			}

			@Override
			public RatingHistory clone()
			{
				RatingHistory clone = new RatingHistory();

				clone.noname1 = this.noname1;
				clone.noname2 = this.noname2;

				return clone;
			}
		}

		public class Trophies implements Readable
		{
			public Byte noname1;
			public Short noname2;
			public Byte noname3;

			@Override
			public Trophies read(ByteBuffer buffer)
			{
				this.noname1 = readB(buffer);
				this.noname2 = readW(buffer);
				this.noname3 = readB(buffer);

				return this;
			}

			@Override
			public Trophies clone()
			{
				Trophies clone = new Trophies();

				clone.noname1 = this.noname1;
				clone.noname2 = this.noname2;
				clone.noname3 = this.noname3;

				return clone;
			}
		}

		public int mask;

		public Integer innerId;
		public Long netId;
		public Byte netType;
		public Byte moderator;
		public String name;
		public Byte gender;
		public String photo;
		public Byte online;
		public PersonInfo personInfo;
		public Integer experience;
		public Weared weared;
		public Integer clanId;
		public Group<Byte> collectionExchange;
		public Byte isGone;
		public Integer clanTotem;
		public Vip vip;
		public Group<Byte> interior;
		public Integer shamanExp;
		public Group<ShamanSkill> shamanSkills;
		public RatingInfo ratingInfo;
		public Group<RatingHistory> ratingHistories;
		public Integer ratingHoliday;
		public Trophies trophies;

		public Player(int mask)
		{
			this.mask = mask;

			this.personInfo = new PersonInfo();
			this.weared = new Weared();
			this.collectionExchange = new Group<>((byte) 0);
			this.vip = new Vip();
			this.interior = new Group<>((byte) 0);
			this.shamanSkills = new Group<>(new ShamanSkill());
			this.ratingInfo = new RatingInfo();
			this.ratingHistories = new Group<>(new RatingHistory());
			this.trophies = new Trophies();
		}

		@Override
		public Readable read(ByteBuffer buffer)
		{
			this.innerId = readI(buffer);

			if ((mask & NET_ID) > 0)
				this.netId = readL(buffer);

			if ((mask & TYPE) > 0)
				this.netType = readB(buffer);

			if ((mask & MODERATOR) > 0)
				this.moderator = readB(buffer);

			if ((mask & NAME) > 0)
				this.name = readS(buffer);

			if ((mask & SEX) > 0)
				this.gender = readB(buffer);

			if ((mask & PHOTO) > 0)
				this.photo = readS(buffer);

			if ((mask & ONLINE) > 0)
				this.online = readB(buffer);

			if ((mask & INFO) > 0)
				this.personInfo = new PersonInfo().read(buffer);

			if ((mask & EXPERIENCE) > 0)
				this.experience = readI(buffer);

			if ((mask & WEARED) > 0)
				this.weared = new Weared().read(buffer);

			if ((mask & CLAN) > 0)
				this.clanId = readI(buffer);

			if ((mask & COLLECTION_EXCHANGE) > 0)
				this.collectionExchange = new Group<>((byte) 0).read(buffer);

			if ((mask & IS_GONE) > 0)
				this.isGone = readB(buffer);

			if ((mask & CLAN_TOTEM) > 0)
				this.clanTotem = readI(buffer);

			if ((mask & VIP_INFO) > 0)
				this.vip = new Vip().read(buffer);

			if ((mask & INTERIOR) > 0)
				this.interior = new Group<>((byte) 0).read(buffer);

			if ((mask & SHAMAN_EXP) > 0)
				this.shamanExp = readI(buffer);

			if ((mask & SHAMAN_SKILLS) > 0)
				this.shamanSkills = new Group<>(new ShamanSkill()).read(buffer);

			if ((mask & RATING_INFO) > 0)
				this.ratingInfo = new RatingInfo().read(buffer);

			if ((mask & RATING_HISTORY) > 0)
				this.ratingHistories = new Group<>(new RatingHistory()).read(buffer);

			if ((mask & RATING_HOLIDAY) > 0)
				this.ratingHoliday = readI(buffer);

			if ((mask & TROPHIES) > 0)
				this.trophies = new Trophies().read(buffer);

			return this;
		}

		@Override
		public Object clone()
		{
			Player clone = new Player(this.mask);

			clone.innerId = this.innerId;
			clone.netId = this.netId;
			clone.netType = this.netType;
			clone.moderator = this.moderator;
			clone.name = this.name;
			clone.gender = this.gender;
			clone.photo = this.photo;
			clone.online = this.online;
			clone.personInfo = (PersonInfo) this.personInfo.clone();
			clone.experience = this.experience;
			clone.weared = (Weared) this.weared.clone();
			clone.clanId = this.clanId;
			clone.collectionExchange = (Group<Byte>) this.collectionExchange.clone();
			clone.isGone = this.isGone;
			clone.clanTotem = this.clanTotem;
			clone.vip = (Vip) this.vip.clone();
			clone.interior = (Group<Byte>) this.interior.clone();
			clone.shamanExp = this.shamanExp;
			clone.shamanSkills = (Group<ShamanSkill>) this.shamanSkills.clone();
			clone.ratingInfo = (RatingInfo) this.ratingInfo.clone();
			clone.ratingHistories = (Group<RatingHistory>) this.ratingHistories.clone();
			clone.ratingHoliday = this.ratingHoliday;
			clone.trophies = (Trophies) this.trophies.clone();

			return clone;
		}
	}

	public byte[] raw;
	public Integer mask;

	public Group<Player> players;

	@Override
	public Readable read(ByteBuffer buffer)
	{
		this.raw = readA(buffer);
		this.mask = readI(buffer);

		ByteBuffer infoBuffer = ByteBuffer.wrap(this.raw);
		infoBuffer.order(ByteOrder.LITTLE_ENDIAN);

		this.players = new Group<>(new Player(this.mask)).read(infoBuffer);

		return this;
	}

	public boolean isFull()
	{
		return this.mask == -1;
	}
}
