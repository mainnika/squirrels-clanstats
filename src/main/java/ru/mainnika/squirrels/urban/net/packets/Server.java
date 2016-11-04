package ru.mainnika.squirrels.urban.net.packets;

import ru.mainnika.squirrels.urban.net.packets.server.Login;
import ru.mainnika.squirrels.urban.net.packets.server.Hello;
import ru.mainnika.squirrels.urban.net.packets.server.ClanBalance;
import ru.mainnika.squirrels.urban.net.packets.server.PlayerInfo;
import ru.mainnika.squirrels.urban.net.packets.server.Guard;
import ru.mainnika.squirrels.urban.net.packets.server.ChatMessage;
import ru.mainnika.squirrels.urban.net.packets.server.ClanInfo;
import ru.mainnika.squirrels.urban.net.packets.server.ClanMembers;

import java.util.HashMap;

public enum Server {
    HELLO(1, Hello.class),
    GUARD(2, Guard.class),
    LOGIN(6, Login.class),
    INFO(7, PlayerInfo.class),
    INFO_NET(8, PlayerInfo.class),
    CHAT_MESSAGE(57, ChatMessage.class),
    CLAN_INFO(101, ClanInfo.class),
    CLAN_BALANCE(103, ClanBalance.class),
    CLAN_MEMBERS(105, ClanMembers.class);

    private static HashMap<Integer, Server> _server;

    static {
        Server._server = new HashMap<>();

        for (Server packet : Server.values()) {
            Server._server.put(packet.id, packet);
        }
    }

    private int id;
    private Class<? extends ServerPacket> specialize;

    Server(int id, Class<? extends ServerPacket> specialize) {
        this.id = id;
        this.specialize = specialize;
    }

    public int id() {
        return this.id;
    }

    public Class<? extends ServerPacket> specialize() {
        return this.specialize;
    }

    public String toString() {
        return "Server packet " + this.id + " " + this.name();
    }

    public static Server getById(int id) {
        return Server._server.get(id);
    }
}
