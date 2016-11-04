package ru.mainnika.squirrels.urban.net.packets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class Packet {

    public static final byte[] EMPTY_BUFFER = new byte[0];

    public interface Readable extends Cloneable {

        Readable read(ByteBuffer buffer);

        Object clone();
    }

    public interface Buildable {

        ByteBuffer build();
    }

    public static class Group<T> extends ArrayList<T> implements Readable, Buildable {

        private T defaultValue;

        public Group(Collection<T> collection) {
            super(collection);
        }

        public Group(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Group<T> read(ByteBuffer buffer) {
            this.clear();

            if (!buffer.hasRemaining()) {
                return this;
            }

            Integer len = readI(buffer);

            if (len == null) {
                return this;
            }

            this.ensureCapacity(len);

            while (len-- > 0) {
                if (this.defaultValue instanceof Byte) {
                    this.add((T) readB(buffer));
                    continue;
                }

                if (this.defaultValue instanceof Short) {
                    this.add((T) readW(buffer));
                    continue;
                }

                if (this.defaultValue instanceof Integer) {
                    this.add((T) readI(buffer));
                    continue;
                }

                if (this.defaultValue instanceof Long) {
                    this.add((T) readL(buffer));
                    continue;
                }

                if (this.defaultValue instanceof String) {
                    this.add((T) readS(buffer));
                    continue;
                }

                if (this.defaultValue instanceof byte[]) {
                    this.add((T) readA(buffer));
                    continue;
                }

                if (defaultValue instanceof Readable) {
                    Readable element = (Readable) ((Readable) this.defaultValue).clone();
                    this.add((T) element.read(buffer));
                    continue;
                }

                this.add(this.defaultValue);
            }

            return this;
        }

        @Override
        public ByteBuffer build() {
            int len = this.size();

            ByteBuffer[] buffers = new ByteBuffer[len + 1];

            buffers[0] = writeI(len);

            while (len-- > 0) {
                T value = this.get(len);

                if (value instanceof Byte) {
                    buffers[len + 1] = writeB((Byte) value);
                    continue;
                }
                if (value instanceof Short) {
                    buffers[len + 1] = writeW((Short) value);
                    continue;
                }

                if (value instanceof Integer) {
                    buffers[len + 1] = writeI((Integer) value);
                    continue;
                }

                if (value instanceof Long) {
                    buffers[len + 1] = writeL((Long) value);
                    continue;
                }

                if (value instanceof String) {
                    buffers[len + 1] = writeS((String) value);
                    continue;
                }

                if (value instanceof byte[]) {
                    buffers[len + 1] = writeA((byte[]) value);
                    continue;
                }

                if (value instanceof Buildable) {
                    Buildable element = (Buildable) value;
                    buffers[len + 1] = element.build();
                    continue;
                }
            }

            return joinBuffers(buffers);
        }

        @Override
        public Group<T> clone() {
            return (Group<T>) super.clone();
        }
    }

    public static byte[] readA(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        int len = buffer.getInt();
        byte[] data = new byte[len];

        buffer.get(data, 0, len);

        return data;
    }

    public static String readS(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        int len = buffer.getShort();
        byte[] data = new byte[len];

        buffer.get(data, 0, len);
        buffer.position(buffer.position() + 1);

        return new String(data, StandardCharsets.UTF_8);
    }

    public static Long readL(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        return buffer.getLong();
    }

    public static Integer readI(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        return buffer.getInt();
    }

    public static Short readW(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        return buffer.getShort();
    }

    public static Byte readB(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return null;
        }

        return buffer.get();
    }

    public static ByteBuffer writeA(byte[] data) {
        if (data == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(data.length);
        buffer.put(data);

        return buffer;
    }

    public static ByteBuffer writeS(String string) {
        if (string == null) {
            return null;
        }

        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(2 + data.length + 1);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) data.length);
        buffer.put(data);
        buffer.put((byte) 0);

        return buffer;
    }

    public static ByteBuffer writeL(Long l) {
        if (l == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(l);

        return buffer;
    }

    public static ByteBuffer writeI(Integer i) {
        if (i == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(i);

        return buffer;
    }

    public static ByteBuffer writeW(Short s) {
        if (s == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(2);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(s);

        return buffer;
    }

    public static ByteBuffer writeB(Byte b) {
        if (b == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1);

        buffer.put(b);

        return buffer;
    }

    public static ByteBuffer writeB(Boolean b) {
        if (b == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1);

        buffer.put(b ? (byte) 1 : (byte) 0);

        return buffer;
    }

    public static ByteBuffer joinBuffers(ByteBuffer... buffers) {
        int estimatedLen = 0;
        for (ByteBuffer buffer : buffers) {
            if (buffer == null) {
                break;
            }

            estimatedLen += buffer.capacity();
        }

        ByteBuffer result = ByteBuffer.allocate(estimatedLen);
        result.order(ByteOrder.LITTLE_ENDIAN);

        for (ByteBuffer buffer : buffers) {
            if (buffer == null) {
                break;
            }

            result.put(buffer.array());
        }

        return result;
    }
}
