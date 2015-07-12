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
	private int id;

	private Packet(String format, ByteBuffer buffer)
	{
		super(parser(format, buffer, 1, false));
	}

	private Packet(Group argsGroup) throws IOException
	{
		super(argsGroup);
	}

	public static Packet make(String format, int id, List<Byte> raw)
	{
		Byte[] object_array = raw.toArray(new Byte[raw.size()]);
		byte[] primitive_array = ArrayUtils.toPrimitive(object_array);
		ByteBuffer buffer = ByteBuffer.wrap(primitive_array);

		buffer.order(ByteOrder.LITTLE_ENDIAN);

		Packet packet = new Packet(format, buffer);

		packet.id = id;

		return packet;
	}

	public static Packet make(String format, int id, Object... args) throws IOException
	{
		Group argsGroup = new Group(Arrays.asList(args));
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
			for (int i = 0; i < format.length(); i++)
			{
				char symbol = format.charAt(i);

				if (raw.position() >= raw.capacity() && optional)
					break;

				switch (symbol)
				{
					case '[':
					{
						int next = format.indexOf(']', i);
						int subGroupLen = raw.getInt();
						String subMask = format.substring(i + 1, next);

						result.add(parser(subMask, raw, subGroupLen, optional));

						i = next;
						break;
					}
					case 'B':
					{
						Byte element = raw.get();
						result.add(element);
						break;
					}
					case 'W':
					{
						Short element = raw.getShort();
						result.add(element);
						break;
					}
					case 'I':
					{
						Integer element = raw.getInt();
						result.add(element);
						break;
					}
					case 'L':
					{
						Long element = raw.getLong();
						result.add(element);
						break;
					}
					case 'S':
					{
						short stringLen = raw.getShort();

						if (stringLen == 0)
						{
							result.add("");
							break;
						}

						byte[] stringRaw = new byte[stringLen];

						raw.get(stringRaw, 0, stringLen);
						raw.position(raw.position() + 1);

						String element = new String(stringRaw, StandardCharsets.UTF_8);

						result.add(element);
						break;
					}
					case 'A':
					{
						int bufLen = raw.getInt();

						if (bufLen == 0)
						{
							result.add(new byte[0]);
							break;
						}

						byte[] bytesRaw = new byte[bufLen];

						raw.get(bytesRaw, 0, bufLen);

						result.add(bytesRaw);
						break;
					}
					case ',':
						optional = true;
						break;
					default:
						log.warning("Unknow symbol in mask (" + symbol + ")");
						break;
				}
			}
		}

		return result;
	}

	public static ByteBuffer writer(String format, Group objects) throws IOException
	{
		ArrayList<ByteBuffer> buffers = new ArrayList<>(objects.size());

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
					if (!(object instanceof List))
					{
						throw new IOException("Invalid format, ArrayList required");
					}

					int next = format.indexOf(']', formatOffset);
					String subMask = format.substring(formatOffset, next);
					Group subGroup = new Group((List)object);

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

	public int getId()
	{
		return this.id;
	}


}
