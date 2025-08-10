package hu.cloudclient.binstd;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public record DataOutputWrapper(DataOutput delegate) implements DataOutput {

    public DataOutputWrapper() {
        this(ByteStreams.newDataOutput());
    }

    public static byte[] encodeAndGetBytes(Encodable encodable) throws IOException {
        ByteArrayDataOutput delegate = ByteStreams.newDataOutput();
        DataOutputWrapper out = new DataOutputWrapper(delegate);
        encodable.encode(out);
        return delegate.toByteArray();
    }

    public void write8(int value) throws IOException {
        delegate.writeByte(value);
    }

    public void write16(int value) throws IOException {
        delegate.writeShort(value);
    }

    public void write32(int value) throws IOException {
        delegate.writeInt(value);
    }

    public void write64(long value) throws IOException {
        delegate.writeLong(value);
    }

    public void writeVar32(int value) throws IOException {
        while (true) {
            if ((value & 0xFFFFFF80) == 0x00) {
                delegate.writeByte(value);
                return;
            }

            delegate.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    public void writeVar64(long value) throws IOException {
        while (true) {
            if ((value & 0xFFFFFFFFFFFFFF80L) == 0x00) {
                delegate.writeByte((int) (value & 0xFF));
                return;
            }

            delegate.writeByte((int) ((value & 0x7F | 0x80) & 0xFF));
            value >>>= 7;
        }
    }

    public void writeF32(float value) throws IOException {
        delegate.writeFloat(value);
    }

    public void writeF64(double value) throws IOException {
        delegate.writeDouble(value);
    }

    public void writeUTF8(String value) throws IOException {
        writeDyn8Array(value.getBytes(StandardCharsets.UTF_8));
    }

    public void writeEnum(Enum<?> instance) throws IOException {
        writeVar32(instance.ordinal());
    }

    public <T extends Encodable> void writeNullable(@Nullable T encodable) throws IOException {
        if (encodable == null) {
            delegate.writeBoolean(false);
        } else {
            delegate.writeBoolean(true);
            write(encodable);
        }
    }

    public <T> void writeNullable(@Nullable T value, Encoder<T> encoder) throws IOException {
        if (value == null) {
            delegate.writeBoolean(false);
        } else {
            delegate.writeBoolean(true);
            encoder.encode(this, value);
        }
    }

    public <T extends Encodable> void writeFixedArray(T[] array) throws IOException {
        for (T encodable : array) {
            encodable.encode(this);
        }
    }

    public <T> void writeFixedArray(T[] array, Encoder<T> encoder) throws IOException {
        for (T t : array) {
            encoder.encode(this, t);
        }
    }

    public <T extends Encodable> void writeFixedCollection(Collection<T> collection) throws IOException {
        for (T encodable : collection) {
            encodable.encode(this);
        }
    }

    public <T> void writeFixedCollection(Collection<T> collection, Encoder<T> encoder) throws IOException {
        for (T t : collection) {
            encoder.encode(this, t);
        }
    }

    public void writeFixed8Array(byte[] array) throws IOException {
        delegate.write(array);
    }

    public <T extends Encodable> void writeDynArray(T[] array) throws IOException {
        writeVar32(array.length);
        writeFixedArray(array);
    }

    public <T> void writeDynArray(T[] array, Encoder<T> encoder) throws IOException {
        writeVar32(array.length);
        writeFixedArray(array, encoder);
    }

    public <T extends Encodable> void writeDynCollection(Collection<T> collection) throws IOException {
        writeVar32(collection.size());
        writeFixedCollection(collection);
    }

    public <T> void writeDynCollection(Collection<T> collection, Encoder<T> encoder) throws IOException {
        writeVar32(collection.size());
        writeFixedCollection(collection, encoder);
    }

    public void writeDyn8Array(byte[] array) throws IOException {
        writeVar32(array.length);
        writeFixed8Array(array);
    }

    public <K extends Encodable, V extends Encodable> void writeFixedMap(Map<K, V> map) throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            entry.getKey().encode(this);
            entry.getValue().encode(this);
        }
    }

    public <K extends Encodable, V> void writeFixedMapKE(Map<K, V> map, Encoder<V> valueEncoder) throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            entry.getKey().encode(this);
            valueEncoder.encode(this, entry.getValue());
        }
    }

    public <K, V extends Encodable> void writeFixedMapVE(Map<K, V> map, Encoder<K> keyEncoder) throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keyEncoder.encode(this, entry.getKey());
            entry.getValue().encode(this);
        }
    }

    public <K, V> void writeFixedMap(Map<K, V> map, Encoder<K> keyEncoder, Encoder<V> valueEncoder) throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keyEncoder.encode(this, entry.getKey());
            valueEncoder.encode(this, entry.getValue());
        }
    }

    public <K extends Encodable, V extends Encodable> void writeDynMap(Map<K, V> map) throws IOException {
        writeVar32(map.size());
        writeFixedMap(map);
    }

    public <K extends Encodable, V> void writeDynMapKE(Map<K, V> map, Encoder<V> valueEncoder) throws IOException {
        writeVar32(map.size());
        writeFixedMapKE(map, valueEncoder);
    }

    public <K, V extends Encodable> void writeDynMapVE(Map<K, V> map, Encoder<K> keyEncoder) throws IOException {
        writeVar32(map.size());
        writeFixedMapVE(map, keyEncoder);
    }

    public <K, V> void writeDynMap(Map<K, V> map, Encoder<K> keyEncoder, Encoder<V> valueEncoder) throws IOException {
        writeVar32(map.size());
        writeFixedMap(map, keyEncoder, valueEncoder);
    }

    public <T> void write(T value, Encoder<T> encoder) throws IOException {
        encoder.encode(this, value);
    }

    public <T extends Encodable> void write(T encodable) throws IOException {
        encodable.encode(this);
    }

    @Deprecated
    @Override
    public void write(int b) throws IOException {
        delegate.write(b);
    }

    @Deprecated
    @Override
    public void write(byte @NotNull [] b) throws IOException {
        delegate.write(b);
    }

    @Deprecated
    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        delegate.write(b, off, len);
    }

    @Deprecated
    @Override
    public void writeBoolean(boolean v) throws IOException {
        delegate.writeBoolean(v);
    }

    @Deprecated
    @Override
    public void writeByte(int v) throws IOException {
        delegate.writeByte(v);
    }

    @Deprecated
    @Override
    public void writeShort(int v) throws IOException {
        delegate.writeShort(v);
    }

    @Deprecated
    @Override
    public void writeChar(int v) throws IOException {
        delegate.writeChar(v);
    }

    @Deprecated
    @Override
    public void writeInt(int v) throws IOException {
        delegate.writeInt(v);
    }

    @Deprecated
    @Override
    public void writeLong(long v) throws IOException {
        delegate.writeLong(v);
    }

    @Deprecated
    @Override
    public void writeFloat(float v) throws IOException {
        delegate.writeFloat(v);
    }

    @Deprecated
    @Override
    public void writeDouble(double v) throws IOException {
        delegate.writeDouble(v);
    }

    @Deprecated
    @Override
    public void writeChars(@NotNull String s) throws IOException {
        delegate.writeChars(s);
    }

    @Deprecated
    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        delegate.writeUTF(s);
    }

    @Deprecated
    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        delegate.writeBytes(s);
    }

}
