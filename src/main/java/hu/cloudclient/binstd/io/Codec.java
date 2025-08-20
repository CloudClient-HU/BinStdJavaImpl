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
    static <CT, T1> Codec<CT> composite(Function<T1, CT> factory, Codec<T1> c1, Function<CT, T1> g1) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                return factory.apply(v1);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2> Codec<CT> composite(BiFunction<T1, T2, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                return factory.apply(v1, v2);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3> Codec<CT> composite(Function3<T1, T2, T3, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                return factory.apply(v1, v2, v3);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4> Codec<CT> composite(Function4<T1, T2, T3, T4, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                return factory.apply(v1, v2, v3, v4);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5> Codec<CT> composite(Function5<T1, T2, T3, T4, T5, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                return factory.apply(v1, v2, v3, v4, v5);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6> Codec<CT> composite(Function6<T1, T2, T3, T4, T5, T6, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7> Codec<CT> composite(Function7<T1, T2, T3, T4, T5, T6, T7, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8> Codec<CT> composite(Function8<T1, T2, T3, T4, T5, T6, T7, T8, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9> Codec<CT> composite(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Codec<CT> composite(Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Codec<CT> composite(Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Codec<CT> composite(Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Codec<CT> composite(Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Codec<CT> composite(Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Codec<CT> composite(Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Codec<CT> composite(Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15, Codec<T16> c16, Function<CT, T16> g16) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                T16 v16 = c16.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
                c16.encode(out, g16.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Codec<CT> composite(Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15, Codec<T16> c16, Function<CT, T16> g16, Codec<T17> c17, Function<CT, T17> g17) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                T16 v16 = c16.decode(in);
                T17 v17 = c17.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
                c16.encode(out, g16.apply(value));
                c17.encode(out, g17.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Codec<CT> composite(Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15, Codec<T16> c16, Function<CT, T16> g16, Codec<T17> c17, Function<CT, T17> g17, Codec<T18> c18, Function<CT, T18> g18) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                T16 v16 = c16.decode(in);
                T17 v17 = c17.decode(in);
                T18 v18 = c18.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
                c16.encode(out, g16.apply(value));
                c17.encode(out, g17.apply(value));
                c18.encode(out, g18.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Codec<CT> composite(Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15, Codec<T16> c16, Function<CT, T16> g16, Codec<T17> c17, Function<CT, T17> g17, Codec<T18> c18, Function<CT, T18> g18, Codec<T19> c19, Function<CT, T19> g19) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                T16 v16 = c16.decode(in);
                T17 v17 = c17.decode(in);
                T18 v18 = c18.decode(in);
                T19 v19 = c19.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
                c16.encode(out, g16.apply(value));
                c17.encode(out, g17.apply(value));
                c18.encode(out, g18.apply(value));
                c19.encode(out, g19.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <CT, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Codec<CT> composite(Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, CT> factory, Codec<T1> c1, Function<CT, T1> g1, Codec<T2> c2, Function<CT, T2> g2, Codec<T3> c3, Function<CT, T3> g3, Codec<T4> c4, Function<CT, T4> g4, Codec<T5> c5, Function<CT, T5> g5, Codec<T6> c6, Function<CT, T6> g6, Codec<T7> c7, Function<CT, T7> g7, Codec<T8> c8, Function<CT, T8> g8, Codec<T9> c9, Function<CT, T9> g9, Codec<T10> c10, Function<CT, T10> g10, Codec<T11> c11, Function<CT, T11> g11, Codec<T12> c12, Function<CT, T12> g12, Codec<T13> c13, Function<CT, T13> g13, Codec<T14> c14, Function<CT, T14> g14, Codec<T15> c15, Function<CT, T15> g15, Codec<T16> c16, Function<CT, T16> g16, Codec<T17> c17, Function<CT, T17> g17, Codec<T18> c18, Function<CT, T18> g18, Codec<T19> c19, Function<CT, T19> g19, Codec<T20> c20, Function<CT, T20> g20) {
        return new Codec<>() {

            @Override
            public CT decode(DataInputWrapper in) throws IOException {
                T1 v1 = c1.decode(in);
                T2 v2 = c2.decode(in);
                T3 v3 = c3.decode(in);
                T4 v4 = c4.decode(in);
                T5 v5 = c5.decode(in);
                T6 v6 = c6.decode(in);
                T7 v7 = c7.decode(in);
                T8 v8 = c8.decode(in);
                T9 v9 = c9.decode(in);
                T10 v10 = c10.decode(in);
                T11 v11 = c11.decode(in);
                T12 v12 = c12.decode(in);
                T13 v13 = c13.decode(in);
                T14 v14 = c14.decode(in);
                T15 v15 = c15.decode(in);
                T16 v16 = c16.decode(in);
                T17 v17 = c17.decode(in);
                T18 v18 = c18.decode(in);
                T19 v19 = c19.decode(in);
                T20 v20 = c20.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20);
            }

            @Override
            public void encode(DataOutputWrapper out, CT value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
                c6.encode(out, g6.apply(value));
                c7.encode(out, g7.apply(value));
                c8.encode(out, g8.apply(value));
                c9.encode(out, g9.apply(value));
                c10.encode(out, g10.apply(value));
                c11.encode(out, g11.apply(value));
                c12.encode(out, g12.apply(value));
                c13.encode(out, g13.apply(value));
                c14.encode(out, g14.apply(value));
                c15.encode(out, g15.apply(value));
                c16.encode(out, g16.apply(value));
                c17.encode(out, g17.apply(value));
                c18.encode(out, g18.apply(value));
                c19.encode(out, g19.apply(value));
                c20.encode(out, g20.apply(value));
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
                return in.readFixedCollection(collectionFactory, Codec.this, expectedSize);
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
