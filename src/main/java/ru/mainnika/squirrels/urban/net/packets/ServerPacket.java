package ru.mainnika.squirrels.urban.net.packets;

public abstract class ServerPacket extends Packet implements Packet.Readable {

    @Override
    public Object clone() {
        return null;
    }
}
