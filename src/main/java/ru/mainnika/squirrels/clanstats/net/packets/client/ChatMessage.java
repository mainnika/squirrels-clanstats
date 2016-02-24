package ru.mainnika.squirrels.clanstats.net.packets.client;

import ru.mainnika.squirrels.clanstats.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class ChatMessage extends ClientPacket
{
	private Byte chatType;
	private String message;

	public ChatMessage(Byte chatType, String message)
	{
		this.chatType = chatType;
		this.message = message;
	}

	@Override
	public ByteBuffer build()
	{
		return joinBuffers(
			writeB(this.chatType),
			writeS(this.message)
		);
	}
}
