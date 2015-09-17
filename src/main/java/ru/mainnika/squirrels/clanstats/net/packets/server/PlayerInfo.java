package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;
import ru.mainnika.squirrels.clanstats.net.packets.PlayerInfoParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInfo extends Packet
{
	public static final int FULL_MASK = -1;

	public class Info
	{
		private Group info;

		private Info(Group info)
		{
			this.info = info;
		}

		public Integer id()
		{
			return this.info.getInt(0);
		}

		public Long netId()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.NET_ID);

			if (offset == null)
				return null;

			return this.info.getLong(offset);
		}

		public Byte netType()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.TYPE);

			if (offset == null)
				return null;

			return this.info.getByte(offset);
		}

		public String name()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.NAME);

			if (offset == null)
				return null;

			return this.info.getString(offset);
		}

		public String photo()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.PHOTO);

			if (offset == null)
				return null;

			return this.info.getString(offset);
		}

		public String profile()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.INFO);

			if (offset == null)
				return null;

			return this.info.getString(offset + 1);
		}

		public Boolean isModerator()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.MODERATOR);

			if (offset == null)
				return null;

			return this.info.getByte(offset) > 0;
		}

		public Boolean isEditor()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.EDITOR);

			if (offset == null)
				return null;

			return this.info.getByte(offset) > 0;
		}

		public Boolean isOnline()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.ONLINE);

			if (offset == null)
				return null;

			return this.info.getByte(offset) > 0;
		}

		public Boolean isVip()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.VIP_INFO);

			if (offset == null)
				return null;

			return this.info.getInt(offset) > 0;
		}

		public Integer squirrelExperience()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.EXPERIENCE);

			if (offset == null)
				return null;

			return this.info.getInt(offset);
		}

		public Integer shamanExperience()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.SHAMAN_EXP);

			if (offset == null)
				return null;

			return this.info.getInt(offset);
		}

		public Integer clanId()
		{
			Integer offset = PlayerInfo.this.offsets.get(PlayerInfoParser.CLAN);

			if (offset == null)
				return null;

			return this.info.getInt(offset);
		}
	}

	private int mask;
	private ArrayList<Info> info;
	private HashMap<PlayerInfoParser, Integer> offsets;

	public PlayerInfo(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));

		this.offsets = new HashMap<>();
		this.info = new ArrayList<>();
		this.mask = this.getInt(1);

		PlayerInfoParser[] list = PlayerInfoParser.values();
		String maskTotal = "";
		int offset = 0;

		for (PlayerInfoParser element : list)
		{
			if ((this.mask & element.id()) != element.id())
				continue;

			this.offsets.put(element, offset);

			maskTotal += element.mask();
			offset += element.length();
		}

		byte[] raw = this.getArray(0);
		Group parsed = PlayerInfoParser.parse(raw, maskTotal);

		for (int i = 0; i < parsed.size(); i++)
		{
			Group element = parsed.getGroup(i);
			this.info.add(new Info(element));
		}
	}

	public Info getInfoElement(int index)
	{
		return this.info.get(index);
	}

	public boolean isFull()
	{
		return this.mask == PlayerInfo.FULL_MASK;
	}

	public int count()
	{
		return this.info.size();
	}
}
