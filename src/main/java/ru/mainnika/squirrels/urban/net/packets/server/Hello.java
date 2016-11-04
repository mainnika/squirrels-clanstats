package ru.mainnika.squirrels.urban.net.packets.server;

import ru.mainnika.squirrels.urban.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class Hello extends ServerPacket {

    @Override
    public Readable read(ByteBuffer buffer) {
        return this;
    }
}
