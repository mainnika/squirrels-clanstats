package ru.mainnika.squirrels.clanstats.net;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Packet
{
	private static final Logger log;

	static
	{
		log = Logger.getLogger(Connection.class.getName());
	}

	private String format;
	private List<Byte> raw;
	private ArrayList<Object> objects;

	public Packet(String format, List<Byte> raw)
	{
		this.raw = raw;
		this.format = format;

		Byte[] object_array = raw.toArray(new Byte[raw.size()]);
		byte[] primitive_array = ArrayUtils.toPrimitive(object_array);

		ByteBuffer buffer = ByteBuffer.wrap(primitive_array);

		buffer.order(ByteOrder.LITTLE_ENDIAN);

		this.objects = parser(format, buffer, 1, false);
	}

	public Packet(String format, Object... args) throws IOException
	{
		this.format = format;
		this.objects = new ArrayList<>(Arrays.asList(args));

		ByteBuffer rawBuffer = writer(format, this.objects);
		Byte[] objectArray = ArrayUtils.toObject(rawBuffer.array());

		this.raw = Arrays.asList(objectArray);
	}

	private static ArrayList<Object> parser(String format, ByteBuffer raw, int groupLen, boolean optional)
	{
		ArrayList<Object> result = new ArrayList<>();

		while (groupLen-- > 0)
		{
			for (int i = 0; i < format.length(); i++)
			{
				char symbol = format.charAt(i);

				switch (symbol)
				{
					case '[':
					{
						int next = format.indexOf(']', i);
						int subGroupLen = raw.getShort();
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
						int stringLen = raw.getInt();

						if (stringLen == 0)
						{
							result.add("");
							break;
						}

						byte[] stringRaw = new byte[stringLen];
						int pos = raw.position();

						ByteBuffer stringBuf = raw.get(stringRaw, pos, stringLen);
						raw.position(pos + stringLen + 1);

						String element = new String(stringBuf.array());

						result.add(element);
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

	private static ByteBuffer writer(String format, ArrayList<Object> objects) throws IOException
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
					if (!(object instanceof ArrayList))
					{
						throw new IOException("Invalid format, ArrayList required");
					}

					int next = format.indexOf(']', formatOffset);
					String subMask = format.substring(formatOffset + 1, next);
					ArrayList<Object> subObjects = (ArrayList) object;

					ByteBuffer rawSize = ByteBuffer.allocate(4);

					rawSize.order(ByteOrder.LITTLE_ENDIAN);
					rawSize.putInt(subObjects.size());

					buffers.add(rawSize);
					buffers.add(writer(subMask, subObjects));
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
					rawString.put(string.getBytes(StandardCharsets.US_ASCII));
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

	public Object get(int index)
	{
		return this.objects.get(index);
	}

	public Byte[] getRaw()
	{
		if (this.raw != null)
		{
			return this.raw.toArray(new Byte[this.raw.size()]);
		}

		return null;
	}


}
