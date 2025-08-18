package hu.cloudclient.binstd.io;

import hu.cloudclient.binstd.exception.MismatchedLengthException;
import hu.cloudclient.binstd.function.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

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

    static <K, V, M extends Map<K, V>> Codec<M> fixedMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec, int size) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readFixedMap(mapFactory, keyCodec, valueCodec, size);
            }

            @Override
            public void encode(DataOutputWrapper out, M map) throws IOException {
                if (map.size() != size) {
                    throw new MismatchedLengthException(size, map.size());
                }

                out.writeFixedMap(map, keyCodec, valueCodec);
            }

        };
    }

    static <K, V, M extends Map<K, V>> Codec<M> dynMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readDynMap(mapFactory, keyCodec, valueCodec);
            }

            @Override
            public void encode(DataOutputWrapper out, M map) throws IOException {
                out.writeDynMap(map, keyCodec, valueCodec);
            }

        };
    }

    static <K, V, M extends Map<K, V>> Codec<M> dynMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec, int maxSize) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readDynMap(mapFactory, keyCodec, valueCodec, maxSize);
            }

            @Override
            public void encode(DataOutputWrapper out, M map) throws IOException {
                if (map.size() > maxSize) {
                    throw new MismatchedLengthException(0, maxSize, map.size());
                }
                out.writeDynMap(map, keyCodec, valueCodec);
            }

        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1> Codec<CT> composite(Function<T1, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                return factory.apply(value1);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2> Codec<CT> composite(BiFunction<T1, T2, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                return factory.apply(value1, value2);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3> Codec<CT> composite(Function3<T1, T2, T3, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                return factory.apply(value1, value2, value3);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4> Codec<CT> composite(Function4<T1, T2, T3, T4, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                return factory.apply(value1, value2, value3, value4);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5> Codec<CT> composite(Function5<T1, T2, T3, T4, T5, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                return factory.apply(value1, value2, value3, value4, value5);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6> Codec<CT> composite(Function6<T1, T2, T3, T4, T5, T6, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7> Codec<CT> composite(Function7<T1, T2, T3, T4, T5, T6, T7, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8> Codec<CT> composite(Function8<T1, T2, T3, T4, T5, T6, T7, T8, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9> Codec<CT> composite(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Codec<CT> composite(Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Codec<CT> composite(Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Codec<CT> composite(Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Codec<CT> composite(Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Codec<CT> composite(Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Codec<CT> composite(Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Codec<CT> composite(Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15, Codec<T16> codec16, Function<CT, T16> getter16) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                T16 value16 = codec16.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
                codec16.encode(out, getter16.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Codec<CT> composite(Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15, Codec<T16> codec16, Function<CT, T16> getter16, Codec<T17> codec17, Function<CT, T17> getter17) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                T16 value16 = codec16.decode(in);
                T17 value17 = codec17.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
                codec16.encode(out, getter16.apply(value));
                codec17.encode(out, getter17.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Codec<CT> composite(Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15, Codec<T16> codec16, Function<CT, T16> getter16, Codec<T17> codec17, Function<CT, T17> getter17, Codec<T18> codec18, Function<CT, T18> getter18) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                T16 value16 = codec16.decode(in);
                T17 value17 = codec17.decode(in);
                T18 value18 = codec18.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
                codec16.encode(out, getter16.apply(value));
                codec17.encode(out, getter17.apply(value));
                codec18.encode(out, getter18.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Codec<CT> composite(Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15, Codec<T16> codec16, Function<CT, T16> getter16, Codec<T17> codec17, Function<CT, T17> getter17, Codec<T18> codec18, Function<CT, T18> getter18, Codec<T19> codec19, Function<CT, T19> getter19) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                T16 value16 = codec16.decode(in);
                T17 value17 = codec17.decode(in);
                T18 value18 = codec18.decode(in);
                T19 value19 = codec19.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
                codec16.encode(out, getter16.apply(value));
                codec17.encode(out, getter17.apply(value));
                codec18.encode(out, getter18.apply(value));
                codec19.encode(out, getter19.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Codec<CT> composite(Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, CT> factory, Codec<T1> codec1, Function<CT, T1> getter1, Codec<T2> codec2, Function<CT, T2> getter2, Codec<T3> codec3, Function<CT, T3> getter3, Codec<T4> codec4, Function<CT, T4> getter4, Codec<T5> codec5, Function<CT, T5> getter5, Codec<T6> codec6, Function<CT, T6> getter6, Codec<T7> codec7, Function<CT, T7> getter7, Codec<T8> codec8, Function<CT, T8> getter8, Codec<T9> codec9, Function<CT, T9> getter9, Codec<T10> codec10, Function<CT, T10> getter10, Codec<T11> codec11, Function<CT, T11> getter11, Codec<T12> codec12, Function<CT, T12> getter12, Codec<T13> codec13, Function<CT, T13> getter13, Codec<T14> codec14, Function<CT, T14> getter14, Codec<T15> codec15, Function<CT, T15> getter15, Codec<T16> codec16, Function<CT, T16> getter16, Codec<T17> codec17, Function<CT, T17> getter17, Codec<T18> codec18, Function<CT, T18> getter18, Codec<T19> codec19, Function<CT, T19> getter19, Codec<T20> codec20, Function<CT, T20> getter20) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 value1 = codec1.decode(in);
                T2 value2 = codec2.decode(in);
                T3 value3 = codec3.decode(in);
                T4 value4 = codec4.decode(in);
                T5 value5 = codec5.decode(in);
                T6 value6 = codec6.decode(in);
                T7 value7 = codec7.decode(in);
                T8 value8 = codec8.decode(in);
                T9 value9 = codec9.decode(in);
                T10 value10 = codec10.decode(in);
                T11 value11 = codec11.decode(in);
                T12 value12 = codec12.decode(in);
                T13 value13 = codec13.decode(in);
                T14 value14 = codec14.decode(in);
                T15 value15 = codec15.decode(in);
                T16 value16 = codec16.decode(in);
                T17 value17 = codec17.decode(in);
                T18 value18 = codec18.decode(in);
                T19 value19 = codec19.decode(in);
                T20 value20 = codec20.decode(in);
                return factory.apply(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13, value14, value15, value16, value17, value18, value19, value20);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                codec1.encode(out, getter1.apply(value));
                codec2.encode(out, getter2.apply(value));
                codec3.encode(out, getter3.apply(value));
                codec4.encode(out, getter4.apply(value));
                codec5.encode(out, getter5.apply(value));
                codec6.encode(out, getter6.apply(value));
                codec7.encode(out, getter7.apply(value));
                codec8.encode(out, getter8.apply(value));
                codec9.encode(out, getter9.apply(value));
                codec10.encode(out, getter10.apply(value));
                codec11.encode(out, getter11.apply(value));
                codec12.encode(out, getter12.apply(value));
                codec13.encode(out, getter13.apply(value));
                codec14.encode(out, getter14.apply(value));
                codec15.encode(out, getter15.apply(value));
                codec16.encode(out, getter16.apply(value));
                codec17.encode(out, getter17.apply(value));
                codec18.encode(out, getter18.apply(value));
                codec19.encode(out, getter19.apply(value));
                codec20.encode(out, getter20.apply(value));
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
            public void encode(DataOutputWrapper out, T nullable) throws IOException {
                out.writeNullable(nullable, Codec.this);
            }

        };
    }

    default Codec<Optional<T>> optional() {
        return new Codec<>() {

            @Override
            public Optional<T> decode(DataInputWrapper in) throws IOException {
                return Optional.ofNullable(in.readNullable(Codec.this));
            }

            @Override
            public void encode(DataOutputWrapper out, Optional<T> optional) throws IOException {
                out.writeNullable(optional.orElse(null), Codec.this);
            }

        };
    }

    default Codec<T[]> fixedArray(IntFunction<T[]> arrayFactory, int length) {
        return new Codec<>() {

            @Override
            public T[] decode(DataInputWrapper in) throws IOException {
                return in.readFixedArray(arrayFactory, Codec.this, length);
            }

            @Override
            public void encode(DataOutputWrapper out, T[] value) throws IOException {
                if (value.length != length) {
                    throw new MismatchedLengthException(length, value.length);
                }

                out.writeFixedArray(value, Codec.this);
            }

        };
    }

    default Codec<T[]> dynArray(IntFunction<T[]> arrayFactory) {
        return new Codec<>() {

            @Override
            public T[] decode(DataInputWrapper in) throws IOException {
                return in.readDynArray(arrayFactory, Codec.this);
            }

            @Override
            public void encode(DataOutputWrapper out, T[] array) throws IOException {
                out.writeDynArray(array, Codec.this);
            }

        };
    }

    default Codec<T[]> dynArray(IntFunction<T[]> arrayFactory, int maxLength) {
        return new Codec<>() {

            @Override
            public T[] decode(DataInputWrapper in) throws IOException {
                return in.readDynArray(arrayFactory, Codec.this, maxLength);
            }

            @Override
            public void encode(DataOutputWrapper out, T[] array) throws IOException {
                if (array.length > maxLength) {
                    throw new MismatchedLengthException(0, maxLength, array.length);
                }

                out.writeDynArray(array, Codec.this);
            }

        };
    }

    default <C extends Collection<T>> Codec<C> fixedCollection(IntFunction<C> collectionFactory, int expectedSize) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readFixedArrayAsCollection(collectionFactory, Codec.this, expectedSize);
            }

            @Override
            public void encode(DataOutputWrapper out, C collection) throws IOException {
                if (collection.size() != expectedSize) {
                    throw new MismatchedLengthException(expectedSize, collection.size());
                }

                out.writeFixedCollection(collection, Codec.this);
            }

        };
    }

    default <C extends Collection<T>> Codec<C> dynCollection(IntFunction<C> collectionFactory) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readDynArrayAsCollection(collectionFactory, Codec.this);
            }

            @Override
            public void encode(DataOutputWrapper out, C collection) throws IOException {
                out.writeDynCollection(collection, Codec.this);
            }

        };
    }

    default <C extends Collection<T>> Codec<C> dynCollection(IntFunction<C> collectionFactory, int maxSize) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readDynArrayAsCollection(collectionFactory, Codec.this, maxSize);
            }

            @Override
            public void encode(DataOutputWrapper out, C collection) throws IOException {
                if (collection.size() > maxSize) {
                    throw new MismatchedLengthException(0, maxSize, collection.size());
                }

                out.writeDynCollection(collection, Codec.this);
            }

        };
    }

    default <NT> Codec<NT> xmap(Function<? super T, ? extends NT> factory, Function<? super NT, ? extends T> getter) {
        return new Codec<>() {

            @Override
            public NT decode(DataInputWrapper in) throws IOException {
                return factory.apply(Codec.this.decode(in));
            }

            @Override
            public void encode(DataOutputWrapper out, NT value) throws IOException {
                Codec.this.encode(out, getter.apply(value));
            }

        };
    }

}
