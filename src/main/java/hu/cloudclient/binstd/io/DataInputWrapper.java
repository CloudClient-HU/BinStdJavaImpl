package hu.cloudclient.binstd.io;

import com.google.common.io.ByteStreams;
import hu.cloudclient.binstd.exception.MismatchedLengthException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

    private static int validate(int actual, int max) throws MismatchedLengthException {
        if (actual < 0 || actual > max) {
            throw new MismatchedLengthException(0, max, actual);
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
        for (int value = 0, pos = 0;; pos += 7) {
            byte b = delegate.readByte();
            value |= (b & 0b01111111) << pos;

            if ((b & 0b10000000) == 0) {
                return value;
            }

            if (pos > 32 - 7) {
                throw new IOException("var32 too big");
            }
        }
    }

    public long readVar64() throws IOException {
        long value = 0;
        for (int pos = 0;; pos += 7) {
            byte b = delegate.readByte();
            value |= (b & 0b01111111L) << pos;

            if ((b & 0b10000000) == 0) {
                return value;
            }

            if (pos > 64 - 7) {
                throw new IOException("var64 too big");
            }
        }
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

    public UUID readUUID() throws IOException {
        return new UUID(delegate.readLong(), delegate.readLong());
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

    public <T> T[] readFixedArray(IntFunction<T[]> arrayFactory, Decoder<T> decoder, int length) throws IOException {
        T[] array = arrayFactory.apply(length);
        for (int i = 0; i < length; i++) {
            array[i] = decoder.decode(this);
        }

        return array;
    }

    public <T> T[] readDynArray(IntFunction<T[]> factory, Decoder<T> decoder) throws IOException {
        return readFixedArray(factory, decoder, validate(readVar32(), config.maxArrayLength));
    }

    public <T> T[] readDynArray(IntFunction<T[]> factory, Decoder<T> decoder, int maxLength) throws IOException {
        return readFixedArray(factory, decoder, validate(readVar32(), maxLength));
    }

    public <T, C extends Collection<T>> C readDynArrayAsCollection(IntFunction<C> collectionFactory, Decoder<T> decoder) throws IOException {
        return readFixedCollection(collectionFactory, decoder, validate(readVar32(), config.maxArrayLength));
    }

    public <T, C extends Collection<T>> C readFixedCollection(IntFunction<C> collectionFactory, Decoder<T> decoder, int size) throws IOException {
        C collection = collectionFactory.apply(size);

        for (int i = 0; i < size; i++) {
            collection.add(decoder.decode(this));
        }

        return collection;
    }

    public <T, C extends Collection<T>> C readDynArrayAsCollection(IntFunction<C> collectionFactory, Decoder<T> decoder, int maxSize) throws IOException {
        return readFixedCollection(collectionFactory, decoder, validate(readVar32(), maxSize));
    }

    public byte[] readFixedI8Array(int length) throws IOException {
        byte[] bytes = new byte[length];
        delegate.readFully(bytes);
        return bytes;
    }

    public byte[] readDynI8Array() throws IOException {
        return readFixedI8Array(validate(readVar32(), config.maxArrayLength));
    }

    public byte[] readDynI8Array(int maxLength) throws IOException {
        return readFixedI8Array(validate(readVar32(), maxLength));
    }

    public int[] readFixedVar32Array(int length) throws IOException {
        int[] ints = new int[length];

        for (int i = 0; i < length; i++) {
            ints[i] = readVar32();
        }

        return ints;
    }

    public int[] readDynVar32Array() throws IOException {
        return readFixedVar32Array(validate(readVar32(), config.maxArrayLength));
    }

    public int[] readDynVar32Array(int maxLength) throws IOException {
        return readFixedVar32Array(validate(readVar32(), maxLength));
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
