package ru.mainnika.squirrels.clanstats.net.packets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Packet
{
	public static byte[] readA(ByteBuffer buffer)
	{
		int len = buffer.getInt();
		byte[] data = new byte[len];

		buffer.get(data, 0, len);

		return data;
	}

	public static ByteBuffer writeA(byte[] data)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(data.length);
		buffer.put(data);

		return buffer;
	}

	public static String readS(ByteBuffer buffer)
	{
		int len = buffer.getShort();
		byte[] data = new byte[len];

		buffer.get(data, 0, len);
		buffer.position(buffer.position() + 1);

		return new String(data, StandardCharsets.UTF_8);
	}

	public static ByteBuffer writeS(String string)
	{
		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		ByteBuffer buffer = ByteBuffer.allocate(2 + data.length + 1);

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) data.length);
		buffer.put(data);
		buffer.put((byte) 0);

		return buffer;
	}

	public static ByteBuffer joinBuffers(ByteBuffer... buffers)
	{
		int estimatedLen = 0;
		for (ByteBuffer buffer : buffers)
		{
			estimatedLen += buffer.capacity();
		}

		ByteBuffer result = ByteBuffer.allocate(estimatedLen);
		result.order(ByteOrder.LITTLE_ENDIAN);

		for (ByteBuffer buffer : buffers)
		{
			result.put(buffer.array());
		}

		return result;
	}
}
