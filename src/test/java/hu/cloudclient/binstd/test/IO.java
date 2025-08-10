package hu.cloudclient.binstd.test;

import hu.cloudclient.binstd.Codec;
import hu.cloudclient.binstd.Codecs;
import hu.cloudclient.binstd.DataInputWrapper;
import hu.cloudclient.binstd.DataOutputWrapper;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IO {

    private static <T> void validate(T value, Codec<T> codec, int numExpectedBytes) {
        try {
            byte[] bytes = DataOutputWrapper.encodeAndGetBytes(codec.asEncodable(value));

            assertEquals(bytes.length, numExpectedBytes);

            DataInputWrapper in = new DataInputWrapper(bytes);
            assertEquals(codec.decode(in), value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void validateExactly(T value, Codec<T> codec, int... expectedBytes) {
        try {
            byte[] bytes = DataOutputWrapper.encodeAndGetBytes(codec.asEncodable(value));
            byte[] actualExpectedBytes = asByteArray(expectedBytes);
            assertArrayEquals(actualExpectedBytes, bytes);

            DataInputWrapper in = new DataInputWrapper(bytes);
            assertEquals(codec.decode(in), value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] asByteArray(int[] ints) {
        byte[] bytes = new byte[ints.length];

        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }

        return bytes;
    }

    @Test
    public void testI8() {
        Codec<Byte> codec = Codecs.I8;
        validateExactly((byte) 0,    codec, 0x00);
        validateExactly((byte) 127,  codec, 0x7F);
        validateExactly((byte) -128, codec, 0x80);
        validateExactly((byte) -1,   codec, 0xFF);
    }

    @Test
    public void testU8() {
        Codec<Integer> codec = Codecs.U8;
        validateExactly(0,   codec, 0x00);
        validateExactly(127, codec, 0x7F);
        validateExactly(255, codec, 0xFF);
    }

    @Test
    public void testI16() {
        Codec<Short> codec = Codecs.I16;
        validateExactly((short) 0,      codec, 0x00, 0x00);
        validateExactly((short) 32767,  codec, 0x7F, 0xFF);
        validateExactly((short) -32768, codec, 0x80, 0x00);
        validateExactly((short) -1,     codec, 0xFF, 0xFF);
    }

    @Test
    public void testU16() {
        Codec<Integer> codec = Codecs.U16;
        validateExactly(0,     codec, 0x00, 0x00);
        validateExactly(32767, codec, 0x7F, 0xFF);
        validateExactly(65535, codec, 0xFF, 0xFF);
    }

    @Test
    public void testI32() {
        Codec<Integer> codec = Codecs.I32;
        validateExactly(0,           codec, 0x00, 0x00, 0x00, 0x00);
        validateExactly(2147483647,  codec, 0x7F, 0xFF, 0xFF, 0xFF);
        validateExactly(-2147483648, codec, 0x80, 0x00, 0x00, 0x00);
        validateExactly(-1,          codec, 0xFF, 0xFF, 0xFF, 0xFF);
    }

    @Test
    public void testI64() {
        Codec<Long> codec = Codecs.I64;
        validateExactly(0L,                    codec, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
        validateExactly(9223372036854775807L,  codec, 0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
        validateExactly(-9223372036854775808L, codec, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
        validateExactly(-1L,                   codec, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
    }

    @Test
    public void testVar32() {
        Codec<Integer> codec = Codecs.VAR32;
        validate(0,         codec, 1);
        validate(127,       codec, 1);
        validate(128,       codec, 2);
        validate(16383,     codec, 2);
        validate(16384,     codec, 3);
        validate(2097151,   codec, 3);
        validate(2097152,   codec, 4);
        validate(268435455, codec, 4);
        validate(268435456, codec, 5);
        validate(-1,        codec, 5);
    }

    @Test
    public void testVar64() {
        Codec<Long> codec = Codecs.VAR64;
        validate(0L,                    codec, 1);
        validate(127L,                  codec, 1);
        validate(128L,                  codec, 2);
        validate(16383L,                codec, 2);
        validate(16384L,                codec, 3);
        validate(2097151L,              codec, 3);
        validate(2097152L,              codec, 4);
        validate(268435455L,            codec, 4);
        validate(268435456L,            codec, 5);
        validate(34359738367L,          codec, 5);
        validate(34359738368L,          codec, 6);
        validate(4398046511103L,        codec, 6);
        validate(4398046511104L,        codec, 7);
        validate(562949953421311L,      codec, 7);
        validate(562949953421312L,      codec, 8);
        validate(72057594037927935L,    codec, 8);
        validate(72057594037927936L,    codec, 9);
        validate(9223372036854775807L,  codec, 9);
        validate(-9223372036854775808L, codec, 10);
        validate(-1L,                   codec, 10);
    }

    @Test
    public void testF32() {
        Codec<Float> codec = Codecs.F32;
        validate(-1.0f, codec, 4);
        validate(-0.0f, codec, 4);
        validate(0.0f, codec, 4);
        validate(1.0f, codec, 4);
        validate(Float.MIN_VALUE, codec, 4);
        validate(Float.MAX_VALUE, codec, 4);
        validate(Float.NaN, codec, 4);
    }

    @Test
    public void testF64() {
        Codec<Double> codec = Codecs.F64;
        validate(-1.0, codec, 8);
        validate(-0.0, codec, 8);
        validate(0.0, codec, 8);
        validate(1.0, codec, 8);
        validate(Double.MIN_VALUE, codec, 8);
        validate(Double.MAX_VALUE, codec, 8);
        validate(Double.NaN, codec, 8);
    }

    enum TestEnum {
        FOO,
        BAR,
        FOOBAR;
    }

    @Test
    public void testEnum() {
        Codec<TestEnum> codec = Codecs.createEnum(TestEnum.class);
        validateExactly(TestEnum.FOO, codec, 0x00);
        validateExactly(TestEnum.BAR, codec, 0x01);
        validateExactly(TestEnum.FOOBAR, codec, 0x02);
    }

    @Test
    public void testNullable() {
        Codec<@Nullable String> codec = Codecs.UTF8.nullable();
        validateExactly("", codec, 0x01, 0x00);
        validateExactly(null, codec, 0x00);
    }

    record Vec3d(double x, double y, double z) {

        public static final Codec<Vec3d> CODEC = new Codec<>() {

            @Override
            public Vec3d decode(DataInputWrapper in) throws IOException {
                return new Vec3d(in.readF64(), in.readF64(), in.readF64());
            }

            @Override
            public void encode(DataOutputWrapper out, Vec3d v) throws IOException {
                out.writeF64(v.x);
                out.writeF64(v.y);
                out.writeF64(v.z);
            }

        };

    }

    @Test
    public void testCustom() {
        validate(new Vec3d(420, 69, 0), Vec3d.CODEC, 3 * 8);
    }

}
