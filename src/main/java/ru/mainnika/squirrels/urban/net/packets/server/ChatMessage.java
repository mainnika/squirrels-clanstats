package ru.mainnika.squirrels.urban.net.packets.server;

import ru.mainnika.squirrels.urban.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class ChatMessage extends ServerPacket {

    public Byte chatType;
    public Integer senderId;
    public String message;

    @Override
    public Readable read(ByteBuffer buffer) {
        this.chatType = readB(buffer);
        this.senderId = readI(buffer);
        this.message = readS(buffer);

        return this;
    }
}
