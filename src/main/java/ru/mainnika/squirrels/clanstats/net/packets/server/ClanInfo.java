package ru.mainnika.squirrels.clanstats.net.packets.server;

import ru.mainnika.squirrels.clanstats.net.Group;
import ru.mainnika.squirrels.clanstats.net.Packet;
import ru.mainnika.squirrels.clanstats.net.packets.ClanInfoParser;
import ru.mainnika.squirrels.clanstats.net.packets.PlayerInfoParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ClanInfo extends Packet
{
	public static final int FULL_MASK = -65537;

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

		public Integer leaderId()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.LEADER_ID);

			if (offset == null)
				return null;

			return this.info.getInt(offset);
		}

		public Byte level()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.RANK);

			if (offset == null)
				return null;

			return this.info.getByte(offset);
		}

		public Integer experience()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.RANK);

			if (offset == null)
				return null;

			return this.info.getInt(offset + 1);
		}

		public String name()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.INFO);

			if (offset == null)
				return null;

			return this.info.getString(offset);
		}

		public String photo()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.INFO);

			if (offset == null)
				return null;

			return this.info.getString(offset + 1);
		}

		public String emblem()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.INFO);

			if (offset == null)
				return null;

			return this.info.getString(offset + 2);
		}

		public Group stats()
		{
			Integer offset = ClanInfo.this.offsets.get(ClanInfoParser.STATISICS);

			if (offset == null)
				return null;

			return this.info.getGroup(offset);
		}

	}

	private int mask;
	private ArrayList<Info> info;
	private HashMap<ClanInfoParser, Integer> offsets;

	public ClanInfo(String format, ByteBuffer buffer) throws IOException
	{
		super(parser(format, buffer, 1, false).getGroup(0));

		this.offsets = new HashMap<>();
		this.info = new ArrayList<>();
		this.mask = this.getInt(1);

		ClanInfoParser[] list = ClanInfoParser.values();
		String maskTotal = "";
		int offset = 0;

		for (ClanInfoParser element : list)
		{
			if ((this.mask & element.id()) != element.id())
				continue;

			this.offsets.put(element, offset);

			maskTotal += element.mask();
			offset += element.length();
		}

		byte[] raw = this.getArray(0);
		Group parsed = ClanInfoParser.parse(raw, maskTotal);

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
		return this.mask == ClanInfo.FULL_MASK;
	}

	public int count()
	{
		return this.info.size();
	}
}
