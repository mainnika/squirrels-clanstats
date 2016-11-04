package ru.mainnika.squirrels.urban.net.packets.client;

import ru.mainnika.squirrels.urban.net.packets.ClientPacket;

import java.nio.ByteBuffer;

public class Login extends ClientPacket {

    private Long netId;
    private Byte netType;
    private Boolean isOauth;
    private String authKey;
    private Integer foo;
    private Integer bar;
    private String sessionKey;

    public Login(long netId, byte netType, boolean isOauth, String authKey, int foo, int bar) {
        this.netId = netId;
        this.netType = netType;
        this.isOauth = isOauth;
        this.authKey = authKey;
        this.foo = foo;
        this.bar = bar;
    }

    public Login(long netId, byte netType, boolean isOauth, String authKey, int foo, int bar, String sessionKey) {
        this.netId = netId;
        this.netType = netType;
        this.isOauth = isOauth;
        this.authKey = authKey;
        this.foo = foo;
        this.bar = bar;
        this.sessionKey = sessionKey;
    }

    @Override
    public ByteBuffer build() {
        return joinBuffers(
                writeL(this.netId),
                writeB(this.netType),
                writeB(this.isOauth),
                writeS(this.authKey),
                writeI(this.foo),
                writeI(this.bar),
                //                      optional:
                writeS(this.sessionKey)
        );
    }
}
