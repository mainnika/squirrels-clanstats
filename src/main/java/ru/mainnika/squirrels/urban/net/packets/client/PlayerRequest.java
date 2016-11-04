package ru.mainnika.squirrels.urban.net.packets.client;

import ru.mainnika.squirrels.urban.net.packets.ClientPacket;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PlayerRequest extends ClientPacket {

    private Group<Integer> playerIds;
    private Integer mask;

    public PlayerRequest(Integer mask, Integer... ids) {
        this.playerIds = new Group<>(Arrays.asList(ids));
        this.mask = mask;
    }

    @Override
    public ByteBuffer build() {
        return joinBuffers(
                this.playerIds.build(),
                writeI(this.mask)
        );
    }
}
