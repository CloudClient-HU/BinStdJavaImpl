package hu.cloudclient.binstd;

import java.io.IOException;

public final class Codecs {

    public static final Codec<Boolean> BOOL = new Codec<>() {

        @Override
        public Boolean decode(DataInputWrapper in) throws IOException {
            return in.readBool();
        }

        @Override
        public void encode(DataOutputWrapper out, Boolean value) throws IOException {
            out.writeBool(value);
        }

    };

    public static final Codec<Byte> I8 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Byte value) throws IOException {
            out.write8(value);
        }

        @Override
        public Byte decode(DataInputWrapper in) throws IOException {
            return in.readI8();
        }

    };

    public static final Codec<Integer> U8 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Integer value) throws IOException {
            out.write8(value);
        }

        @Override
        public Integer decode(DataInputWrapper in) throws IOException {
            return in.readU8();
        }

    };

    public static final Codec<Short> I16 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Short value) throws IOException {
            out.write16(value);
        }

        @Override
        public Short decode(DataInputWrapper in) throws IOException {
            return in.readI16();
        }

    };

    public static final Codec<Integer> U16 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Integer value) throws IOException {
            out.write16(value);
        }

        @Override
        public Integer decode(DataInputWrapper in) throws IOException {
            return in.readU16();
        }

    };

    public static final Codec<Integer> I32 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Integer value) throws IOException {
            out.write32(value);
        }

        @Override
        public Integer decode(DataInputWrapper in) throws IOException {
            return in.readI32();
        }

    };

    public static final Codec<Long> I64 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Long value) throws IOException {
            out.write64(value);
        }

        @Override
        public Long decode(DataInputWrapper in) throws IOException {
            return in.readI64();
        }

    };

    public static final Codec<Integer> VAR32 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Integer value) throws IOException {
            out.writeVar32(value);
        }

        @Override
        public Integer decode(DataInputWrapper in) throws IOException {
            return in.readVar32();
        }

    };

    public static final Codec<Long> VAR64 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Long value) throws IOException {
            out.writeVar64(value);
        }

        @Override
        public Long decode(DataInputWrapper in) throws IOException {
            return in.readVar64();
        }

    };

    public static final Codec<Float> F32 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Float value) throws IOException {
            out.writeF32(value);
        }

        @Override
        public Float decode(DataInputWrapper in) throws IOException {
            return in.readF32();
        }

    };

    public static final Codec<Double> F64 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, Double value) throws IOException {
            out.writeF64(value);
        }

        @Override
        public Double decode(DataInputWrapper in) throws IOException {
            return in.readF64();
        }

    };

    public static final Codec<String> UTF8 = new Codec<>() {

        @Override
        public void encode(DataOutputWrapper out, String value) throws IOException {
            out.writeUTF8(value);
        }

        @Override
        public String decode(DataInputWrapper in) throws IOException {
            return in.readUTF8();
        }

    };

    public static <T extends Enum<T>> Codec<T> createEnum(Class<T> clazz) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                return in.readEnum(clazz);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                out.writeEnum(value);
            }

        };
    }

}
