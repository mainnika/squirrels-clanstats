package ru.mainnika.squirrels.clanstats.net;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Packet extends Group
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	private List<Byte> raw;
	private short id;

	private Packet(String format, ByteBuffer buffer)
	{
		super(parser(format, buffer, 1, false).getGroup(0));
	}

	private Packet(Group argsGroup) throws IOException
	{
		super(argsGroup);
	}

	private static int find_closing_bracket(String mask, int fromIndex)
	{
		int left = 0;
		int right = 0;

		for (int i = fromIndex; i < mask.length(); i++)
		{
			char symbol = mask.charAt(i);

			switch (symbol)
			{
				case '[':
					left++;
					continue;
				case ']':
					if (left == right)
					{
						return i;
					}

					right++;

					continue;
				default:
					break;
			}
		}

		return -1;
	}

	public static Packet make(String format, short id, byte[] raw)
	{
		ByteBuffer buffer = ByteBuffer.wrap(raw);

		buffer.order(ByteOrder.LITTLE_ENDIAN);

		Packet packet = new Packet(format, buffer);

		packet.id = id;

		return packet;
	}

	public static Packet make(String format, short id, Object... args) throws IOException
	{
		Group groupElement = new Group(Arrays.asList(args));
		Group argsGroup = new Group(Collections.singletonList(groupElement));

		ByteBuffer rawBuffer = writer(format, argsGroup);
		Byte[] objectArray = ArrayUtils.toObject(rawBuffer.array());

		Packet packet = new Packet(argsGroup);

		packet.raw = Arrays.asList(objectArray);
		packet.id = id;

		return packet;
	}

	public static Group parser(String format, ByteBuffer raw, int groupLen, boolean optional)
	{
		Group result = new Group();

		while (groupLen-- > 0)
		{
			Group groupElement = new Group();
			int formatOffset = 0;

			while (formatOffset < format.length())
			{
				char symbol = format.charAt(formatOffset++);

				if (raw.position() >= raw.capacity() && optional)
					break;

				switch (symbol)
				{
					case '[':
					{
						int next = find_closing_bracket(format, formatOffset);
						int subGroupLen = raw.getInt();
						String subMask = format.substring(formatOffset, next);

						groupElement.add(parser(subMask, raw, subGroupLen, optional));

						formatOffset = next + 1;
						break;
					}
					case 'B':
					{
						Byte element = raw.get();
						groupElement.add(element);
						break;
					}
					case 'W':
					{
						Short element = raw.getShort();
						groupElement.add(element);
						break;
					}
					case 'I':
					{
						Integer element = raw.getInt();
						groupElement.add(element);
						break;
					}
					case 'L':
					{
						Long element = raw.getLong();
						groupElement.add(element);
						break;
					}
					case 'S':
					{
						short stringLen = raw.getShort();

						if (stringLen == 0)
						{
							groupElement.add("");
							break;
						}

						byte[] stringRaw = new byte[stringLen];

						raw.get(stringRaw, 0, stringLen);
						raw.position(raw.position() + 1);

						String element = new String(stringRaw, StandardCharsets.UTF_8);

						groupElement.add(element);
						break;
					}
					case 'A':
					{
						int bufLen = raw.getInt();

						if (bufLen == 0)
						{
							groupElement.add(new byte[0]);
							break;
						}

						byte[] bytesRaw = new byte[bufLen];

						raw.get(bytesRaw, 0, bufLen);

						groupElement.add(bytesRaw);
						break;
					}
					case ',':
						optional = true;
						break;
					default:
						log.warning("Unknown symbol in mask (" + symbol + ")");
						break;
				}
			}

			result.add(groupElement);
		}

		return result;
	}

	public static ByteBuffer writer(String format, Group groups) throws IOException
	{
		ArrayList<ByteBuffer> buffers = new ArrayList<>();

		int groupsOffset = 0;

		while (groupsOffset < groups.size())
		{
			Group objects = (Group) groups.get(groupsOffset++);
			int objectsOffset = 0;
			int formatOffset = 0;

			while (formatOffset < format.length())
			{
				char symbol = format.charAt(formatOffset++);
				Object object = objects.get(objectsOffset++);

				switch (symbol)
				{
					case '[':
					{
						if (!(object instanceof Group))
						{
							throw new IOException("Invalid format, ArrayList required");
						}

						int next = find_closing_bracket(format, formatOffset);
						String subMask = format.substring(formatOffset, next);
						Group subGroup = (Group) object;

						ByteBuffer rawSize = ByteBuffer.allocate(4);

						rawSize.order(ByteOrder.LITTLE_ENDIAN);
						rawSize.putInt(subGroup.size());

						buffers.add(rawSize);
						buffers.add(writer(subMask, subGroup));

						formatOffset = next + 1;
						break;
					}
					case 'B':
					{
						if (!(object instanceof Byte))
						{
							throw new IOException("Invalid format, Byte required");
						}

						ByteBuffer rawByte = ByteBuffer.allocate(1);

						rawByte.order(ByteOrder.LITTLE_ENDIAN);
						rawByte.put((byte) object);

						buffers.add(rawByte);
						break;
					}
					case 'W':
					{
						if (!(object instanceof Short))
						{
							throw new IOException("Invalid format, Short required");
						}

						ByteBuffer rawShort = ByteBuffer.allocate(2);

						rawShort.order(ByteOrder.LITTLE_ENDIAN);
						rawShort.putShort((short) object);

						buffers.add(rawShort);
						break;
					}
					case 'I':
					{
						if (!(object instanceof Integer))
						{
							throw new IOException("Invalid format, Integer required");
						}

						ByteBuffer rawInt = ByteBuffer.allocate(4);

						rawInt.order(ByteOrder.LITTLE_ENDIAN);
						rawInt.putInt((int) object);

						buffers.add(rawInt);
						break;
					}
					case 'L':
					{
						if (!(object instanceof Long))
						{
							throw new IOException("Invalid format, Long required");
						}

						ByteBuffer rawLong = ByteBuffer.allocate(8);

						rawLong.order(ByteOrder.LITTLE_ENDIAN);
						rawLong.putLong((long) object);

						buffers.add(rawLong);
						break;
					}
					case 'S':
					{
						if (!(object instanceof String))
						{
							throw new IOException("Invalid format, String required");
						}

						String string = (String) object;

						ByteBuffer rawString = ByteBuffer.allocate(string.length() + 2 + 1);

						rawString.order(ByteOrder.LITTLE_ENDIAN);
						rawString.putShort((short) string.length());
						rawString.put(string.getBytes(StandardCharsets.UTF_8));
						rawString.put((byte) 0);

						buffers.add(rawString);
						break;
					}
				}
			}
		}

		int estimatedSize = 0;

		for (ByteBuffer buffer : buffers)
		{
			estimatedSize += buffer.capacity();
		}

		ByteBuffer result = ByteBuffer.allocate(estimatedSize);

		result.order(ByteOrder.LITTLE_ENDIAN);

		for (ByteBuffer buffer : buffers)
		{
			result.put(buffer.array());
		}

		return result;
	}

	public Byte[] getRaw()
	{
		if (this.raw != null)
		{
			return this.raw.toArray(new Byte[this.raw.size()]);
		}

		return null;
	}

	public short getId()
	{
		return this.id;
	}


}
