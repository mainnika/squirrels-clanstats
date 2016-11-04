package ru.mainnika.squirrels.urban.net.packets.client;

import ru.mainnika.squirrels.urban.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class Guard extends ClientPacket {

    private String guardResponse;

    public Guard(String guardResponse) {
        this.guardResponse = guardResponse;
    }

    @Override
    public ByteBuffer build() {
        return joinBuffers(
                writeS(this.guardResponse)
        );
    }
}
