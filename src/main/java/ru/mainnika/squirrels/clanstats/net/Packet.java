package ru.mainnika.squirrels.clanstats.net;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

	public Packet(String format, Objects... args)
	{
		this.raw = null;
		this.format = format;
		this.objects = new ArrayList<>(Arrays.asList(args));
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
