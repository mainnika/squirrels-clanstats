package ru.mainnika.squirrels.urban.core;

import org.apache.commons.lang3.ArrayUtils;
import ru.mainnika.squirrels.urban.net.Connection;
import ru.mainnika.squirrels.urban.net.Receiver;
import ru.mainnika.squirrels.urban.net.packets.client.*;
import ru.mainnika.squirrels.urban.utils.Timers;

import java.util.logging.Logger;

public class Analytics extends Receiver implements Timers.Task {

    private static final int FORCE_RECONNECT_TIME = 15;

    private static final Logger log;

    static {
        log = Logger.getLogger(Connection.class.getName());
    }

    private int playerId;
    private int clanId;

    private int tick;

    private boolean loginFirstTime;

    private Credentials credentials;

    public Analytics(Connection connection, Credentials credentials) {
        super(connection);

        this.credentials = credentials;
        this.tick = 0;
        this.loginFirstTime = true;
    }

    public void onConnect() {
        this.sendPacket(new Hello());
    }

    public void onDisconnect() {
        Timers.unsubscribe(this);
    }

    public void onPacket(ru.mainnika.squirrels.urban.net.packets.server.Hello packet) {
        log.info("HELLO");
    }

    @Override
    public void onTimer() {
        log.info("Timer tick #" + this.tick);

        this.tick++;

        if (this.tick % Analytics.FORCE_RECONNECT_TIME == 0) {
            log.info("Force reconnect");
            this.io.disconnect();
            return;
        }
    }

    public void requestPlayers(byte type, long... nids) {
        this.sendPacket(new PlayerRequestNet(-1, type, ArrayUtils.toObject(nids)));
    }

    public void requestPlayers(int... uids) {
        this.sendPacket(new PlayerRequest(-1, ArrayUtils.toObject(uids)));
    }

    public void requestClans(int... uids) {
        this.sendPacket(new ClanRequest(-1, ArrayUtils.toObject(uids)));

        for (int uid : uids) {
            this.sendPacket(new ClanGetMembers(uid));
        }
    }

    public void clanChat(String message) {
        this.sendPacket(new ChatMessage((byte) 1, message));
    }

    public void loginFirstTime() {
        if (!loginFirstTime) {
            return;
        }

        this.loginFirstTime = false;
    }

    public int clanId() {
        return this.clanId;
    }

    public int playerId() {
        return this.playerId;
    }
}
