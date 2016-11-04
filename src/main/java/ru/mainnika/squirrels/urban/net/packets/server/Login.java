package ru.mainnika.squirrels.urban.net.packets.server;

import ru.mainnika.squirrels.urban.net.packets.ServerPacket;

import java.nio.ByteBuffer;

public class Login extends ServerPacket {

    public static class Discount implements Readable {

        public Byte type;
        public Integer time;

        @Override
        public Readable read(ByteBuffer buffer) {
            this.type = readB(buffer);
            this.time = readI(buffer);

            return this;
        }

        @Override
        public Object clone() {
            Discount clone = new Discount();

            clone.time = this.time;
            clone.type = this.type;

            return clone;
        }
    }

    public static class ShamanInfo implements Readable {

        public Byte skillId;
        public Byte levelFree;
        public Byte levelPaid;

        @Override
        public Readable read(ByteBuffer buffer) {
            this.skillId = readB(buffer);
            this.levelFree = readB(buffer);
            this.levelPaid = readB(buffer);

            return this;
        }

        @Override
        public Object clone() {
            ShamanInfo clone = new ShamanInfo();

            clone.skillId = this.skillId;
            clone.levelFree = this.levelFree;
            clone.levelPaid = this.levelPaid;

            return clone;
        }
    }

    public Byte status;
    public Integer innerId;
    public Integer unixTime;
    public Integer tag;
    public Byte canOffer;
    public Byte advertising;
    public String email;
    public Byte editor;
    public Integer inviterId;
    public Integer registerTime;
    public Integer logoutTime;
    public Integer paymentType;
    public Group<Short> items;
    public Integer clanApplication;
    public Group<Short> newsRepost;
    public Group<Discount> discounts;
    public Group<ShamanInfo> shamanInfo;
    public Integer key;

    @Override
    public Readable read(ByteBuffer buffer) {
        this.status = readB(buffer);

//		optional:
        this.innerId = readI(buffer);
        this.unixTime = readI(buffer);
        this.tag = readI(buffer);
        this.canOffer = readB(buffer);
        this.advertising = readB(buffer);
        this.email = readS(buffer);
        this.editor = readB(buffer);
        this.inviterId = readI(buffer);
        this.registerTime = readI(buffer);
        this.logoutTime = readI(buffer);
        this.paymentType = readI(buffer);
        this.items = new Group<>((short) 0).read(buffer);
        this.clanApplication = readI(buffer);
        this.newsRepost = new Group<>((short) 0).read(buffer);
        this.discounts = new Group<>(new Discount()).read(buffer);
        this.shamanInfo = new Group<>(new ShamanInfo()).read(buffer);
        this.key = readI(buffer);

        return this;
    }
}
