package hu.cloudclient.binstd;

import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

public record DataInputWrapper(DataInput delegate, Config config) implements DataInput {

    public DataInputWrapper(DataInput delegate) {
        this(delegate, Config.DEFAULT);
    }

    public DataInputWrapper(byte[] data, Config config) {
        this(ByteStreams.newDataInput(data), config);
    }

    public DataInputWrapper(byte[] data) {
        this(ByteStreams.newDataInput(data), Config.DEFAULT);
    }

    private static int validate(int actual, int max) throws OutOfRangeException {
        if (actual < 0 || actual > max) {
            throw new OutOfRangeException(0, max, actual);
        }

        return actual;
    }

    public boolean readBool() throws IOException {
        return delegate.readBoolean();
    }

    public byte readI8() throws IOException {
        return delegate.readByte();
    }

    public int readU8() throws IOException {
        return delegate.readUnsignedByte();
    }

    public short readI16() throws IOException {
        return delegate.readShort();
    }

    public int readU16() throws IOException {
        return delegate.readUnsignedShort();
    }

    public int readI32() throws IOException {
        return delegate.readInt();
    }

    public long readI64() throws IOException {
        return delegate.readLong();
    }

    public int readVar32() throws IOException {
        int value = 0;
        int pos = 0;
        while (true) {
            byte b = delegate.readByte();
            value |= (b & 0x7F) << pos;
            if ((b & 0x80) != 0x80) {
                break;
            }

            pos += 7;

            if (pos > 32) {
                throw new IOException("var32 too big");
            }
        }
        return value;
    }

    public long readVar64() throws IOException {
        long value = 0;
        int pos = 0;
        while (true) {
            byte b = delegate.readByte();
            value |= (b & 0x7FL) << pos;

            if ((b & 0x80) != 0x80) {
                break;
            }

            pos += 7;

            if (pos > 64) {
                throw new IOException("var64 too big");
            }
        }
        return value;
    }

    public float readF32() throws IOException {
        return delegate.readFloat();
    }

    public double readF64() throws IOException {
        return delegate.readDouble();
    }

    public String readUTF8() throws IOException {
        return new String(readFixedI8Array(validate(readVar32(), config.maxUTF8Size)), StandardCharsets.UTF_8);
    }

    public String readUTF8(int maxSize) throws IOException {
        return new String(readFixedI8Array(validate(readVar32(), maxSize)), StandardCharsets.UTF_8);
    }

    public <T extends Enum<T>> T readEnum(Class<T> clazz) throws IOException {
        int ordinal = readVar32();
        T[] constants = clazz.getEnumConstants();

        if (constants == null) {
            throw new IOException("class not enum: " + clazz.getName());
        }

        if (ordinal < 0 || ordinal > constants.length) {
            throw new IOException("enum ordinal " + ordinal + " for class" + clazz.getName() + " is out of bounds");
        }

        return constants[ordinal];
    }

    @Nullable
    public <T> T readNullable(Decoder<T> decoder) throws IOException {
        boolean isPresent = delegate.readBoolean();

        if (isPresent) {
            return decoder.decode(this);
        } else {
            return null;
        }
    }

    public <T> Optional<T> readOptional(Decoder<T> decoder) throws IOException {
        boolean isPresent = delegate.readBoolean();

        if (isPresent) {
            return Optional.of(decoder.decode(this));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T[] readFixedArray(Class<T> clazz, Decoder<T> decoder, int length) throws IOException {
        T[] array = (T[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            array[i] = decoder.decode(this);
        }

        return array;
    }

    public <T, C extends Collection<T>> C readFixedArrayAsCollection(IntFunction<C> collectionFactory, Decoder<T> decoder, int size) throws IOException {
        C collection = collectionFactory.apply(size);

        for (int i = 0; i < size; i++) {
            collection.add(decoder.decode(this));
        }

        return collection;
    }

    public byte[] readFixedI8Array(int length) throws IOException {
        byte[] bytes = new byte[length];
        delegate.readFully(bytes);
        return bytes;
    }

    public <T> T[] readDynArray(Class<T> clazz, Decoder<T> decoder) throws IOException {
        return readFixedArray(clazz, decoder, validate(readVar32(), config.maxArrayLength));
    }

    public <T> T[] readDynArray(Class<T> clazz, Decoder<T> decoder, int maxLength) throws IOException {
        return readFixedArray(clazz, decoder, validate(readVar32(), maxLength));
    }

    public <T, C extends Collection<T>> C readDynArrayAsCollection(IntFunction<C> collectionFactory, Decoder<T> decoder) throws IOException {
        return readFixedArrayAsCollection(collectionFactory, decoder, validate(readVar32(), config.maxArrayLength));
    }

    public <T, C extends Collection<T>> C readDynArrayAsCollection(IntFunction<C> collectionFactory, Decoder<T> decoder, int maxSize) throws IOException {
        return readFixedArrayAsCollection(collectionFactory, decoder, validate(readVar32(), maxSize));
    }

    public byte[] readDynI8Array() throws IOException {
        return readFixedI8Array(validate(readVar32(), config.maxArrayLength));
    }

    public byte[] readDynI8Array(int maxLength) throws IOException {
        return readFixedI8Array(validate(readVar32(), maxLength));
    }

    public <K, V, M extends Map<K, V>> M readFixedMap(IntFunction<M> mapFactory, Decoder<K> keyDecoder, Decoder<V> valueDecoder, int size) throws IOException {
        M map = mapFactory.apply(size);

        for (int i = 0; i < size; i++) {
            K key = keyDecoder.decode(this);
            V value = valueDecoder.decode(this);
            map.put(key, value);
        }

        return map;
    }

    public <K, V, M extends Map<K, V>> M readDynMap(IntFunction<M> mapFactory, Decoder<K> keyDecoder, Decoder<V> valueDecoder) throws IOException {
        return readFixedMap(mapFactory, keyDecoder, valueDecoder, validate(readVar32(), config.maxMapSize));
    }

    public <K, V, M extends Map<K, V>> M readDynMap(IntFunction<M> mapFactory, Decoder<K> keyDecoder, Decoder<V> valueDecoder, int maxSize) throws IOException {
        return readFixedMap(mapFactory, keyDecoder, valueDecoder, validate(readVar32(), maxSize));
    }

    public <T> T read(Decoder<T> decoder) throws IOException {
        return decoder.decode(this);
    }

    @Deprecated
    @Override
    public void readFully(byte @NotNull [] b) throws IOException {
        delegate.readFully(b);
    }

    @Deprecated
    @Override
    public void readFully(byte @NotNull [] b, int off, int len) throws IOException {
        delegate.readFully(b, off, len);
    }

    @Deprecated
    @Override
    public int skipBytes(int n) throws IOException {
        return delegate.skipBytes(n);
    }

    @Deprecated
    @Override
    public boolean readBoolean() throws IOException {
        return delegate.readBoolean();
    }

    @Deprecated
    @Override
    public byte readByte() throws IOException {
        return delegate.readByte();
    }

    @Deprecated
    @Override
    public int readUnsignedByte() throws IOException {
        return delegate.readUnsignedByte();
    }

    @Deprecated
    @Override
    public short readShort() throws IOException {
        return delegate.readShort();
    }

    @Deprecated
    @Override
    public int readUnsignedShort() throws IOException {
        return delegate.readUnsignedShort();
    }

    @Deprecated
    @Override
    public char readChar() throws IOException {
        return delegate.readChar();
    }

    @Deprecated
    @Override
    public int readInt() throws IOException {
        return delegate.readInt();
    }

    @Deprecated
    @Override
    public long readLong() throws IOException {
        return delegate.readLong();
    }

    @Deprecated
    @Override
    public float readFloat() throws IOException {
        return delegate.readFloat();
    }

    @Deprecated
    @Override
    public double readDouble() throws IOException {
        return delegate.readDouble();
    }

    @Nullable
    @Deprecated
    @Override
    public String readLine() throws IOException {
        return delegate.readLine();
    }

    @NotNull
    @Deprecated
    @Override
    public String readUTF() throws IOException {
        return delegate.readUTF();
    }

    public record Config(int maxUTF8Size, int maxArrayLength, int maxMapSize) {

        public static final Config UNRESTRICTED = new Config(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        public static final Config DEFAULT = new Config(16383, 16383, 16383);

    }

}
