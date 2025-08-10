package hu.cloudclient.binstd;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface Codec<T> extends Encoder<T>, Decoder<T> {

    static <T> Codec<T> of(Encoder<T> encoder, Decoder<T> decoder) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                return decoder.decode(in);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                encoder.encode(out, value);
            }

        };
    }

    static <T> Codec<T> ofInstance(T instance) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                return instance;
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                if (value != instance) {
                    throw new IOException("value != instance");
                }
            }

        };
    }

    default Codec<@Nullable T> nullable() {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                return in.readNullable(Codec.this);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                out.writeNullable(value == null ? null : Codec.this.asEncodable(value));
            }

        };
    }

}
