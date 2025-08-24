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
        return new InstanceCodec<>(instance);
    }

    static <K, V, M extends Map<K, V>> Codec<Map<K, V>> fixedMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec, int size) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readFixedMap(mapFactory, keyCodec, valueCodec, size);
            }

            @Override
            public void encode(DataOutputWrapper out, Map<K, V> map) throws IOException {
                if (map.size() != size) {
                    throw new MismatchedLengthException(size, map.size());
                }

                out.writeFixedMap(map, keyCodec, valueCodec);
            }

        };
    }

    static <K, V, M extends Map<K, V>> Codec<Map<K, V>> dynMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readDynMap(mapFactory, keyCodec, valueCodec);
            }

            @Override
            public void encode(DataOutputWrapper out, Map<K, V> map) throws IOException {
                out.writeDynMap(map, keyCodec, valueCodec);
            }

        };
    }

    static <K, V, M extends Map<K, V>> Codec<Map<K, V>> dynMap(IntFunction<M> mapFactory, Codec<K> keyCodec, Codec<V> valueCodec, int maxSize) {
        return new Codec<>() {

            @Override
            public M decode(DataInputWrapper in) throws IOException {
                return in.readDynMap(mapFactory, keyCodec, valueCodec, maxSize);
            }

            @Override
            public void encode(DataOutputWrapper out, Map<K, V> map) throws IOException {
                if (map.size() > maxSize) {
                    throw new MismatchedLengthException(0, maxSize, map.size());
                }
                out.writeDynMap(map, keyCodec, valueCodec);
            }

        };
    }

    // Explanation of type / parameter names:
    // T: the type of the record-like object or record
    // F?: field ?
    // c?: codec of field ?
    // g?: getter of field ? from an instance of T
    // v?: decoded value using c?

    // This is an automatically generated method, you should probably not modify this
    static <T, F1> Codec<T> rec(Function<F1, T> factory, Codec<F1> c1, Function<T, F1> g1) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                return factory.apply(v1);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                c1.encode(out, g1.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <T, F1, F2> Codec<T> rec(BiFunction<F1, F2, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                return factory.apply(v1, v2);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <T, F1, F2, F3> Codec<T> rec(Function3<F1, F2, F3, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                return factory.apply(v1, v2, v3);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <T, F1, F2, F3, F4> Codec<T> rec(Function4<F1, F2, F3, F4, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                return factory.apply(v1, v2, v3, v4);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <T, F1, F2, F3, F4, F5> Codec<T> rec(Function5<F1, F2, F3, F4, F5, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                return factory.apply(v1, v2, v3, v4, v5);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
                c1.encode(out, g1.apply(value));
                c2.encode(out, g2.apply(value));
                c3.encode(out, g3.apply(value));
                c4.encode(out, g4.apply(value));
                c5.encode(out, g5.apply(value));
            }
        };
    }

    // This is an automatically generated method, you should probably not modify this
    static <T, F1, F2, F3, F4, F5, F6> Codec<T> rec(Function6<F1, F2, F3, F4, F5, F6, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7> Codec<T> rec(Function7<F1, F2, F3, F4, F5, F6, F7, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8> Codec<T> rec(Function8<F1, F2, F3, F4, F5, F6, F7, F8, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9> Codec<T> rec(Function9<F1, F2, F3, F4, F5, F6, F7, F8, F9, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> Codec<T> rec(Function10<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11> Codec<T> rec(Function11<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12> Codec<T> rec(Function12<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13> Codec<T> rec(Function13<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14> Codec<T> rec(Function14<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15> Codec<T> rec(Function15<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16> Codec<T> rec(Function16<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15, Codec<F16> c16, Function<T, F16> g16) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                F16 v16 = c16.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17> Codec<T> rec(Function17<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15, Codec<F16> c16, Function<T, F16> g16, Codec<F17> c17, Function<T, F17> g17) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                F16 v16 = c16.decode(in);
                F17 v17 = c17.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18> Codec<T> rec(Function18<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15, Codec<F16> c16, Function<T, F16> g16, Codec<F17> c17, Function<T, F17> g17, Codec<F18> c18, Function<T, F18> g18) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                F16 v16 = c16.decode(in);
                F17 v17 = c17.decode(in);
                F18 v18 = c18.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19> Codec<T> rec(Function19<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15, Codec<F16> c16, Function<T, F16> g16, Codec<F17> c17, Function<T, F17> g17, Codec<F18> c18, Function<T, F18> g18, Codec<F19> c19, Function<T, F19> g19) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                F16 v16 = c16.decode(in);
                F17 v17 = c17.decode(in);
                F18 v18 = c18.decode(in);
                F19 v19 = c19.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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
    static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20> Codec<T> rec(Function20<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, T> factory, Codec<F1> c1, Function<T, F1> g1, Codec<F2> c2, Function<T, F2> g2, Codec<F3> c3, Function<T, F3> g3, Codec<F4> c4, Function<T, F4> g4, Codec<F5> c5, Function<T, F5> g5, Codec<F6> c6, Function<T, F6> g6, Codec<F7> c7, Function<T, F7> g7, Codec<F8> c8, Function<T, F8> g8, Codec<F9> c9, Function<T, F9> g9, Codec<F10> c10, Function<T, F10> g10, Codec<F11> c11, Function<T, F11> g11, Codec<F12> c12, Function<T, F12> g12, Codec<F13> c13, Function<T, F13> g13, Codec<F14> c14, Function<T, F14> g14, Codec<F15> c15, Function<T, F15> g15, Codec<F16> c16, Function<T, F16> g16, Codec<F17> c17, Function<T, F17> g17, Codec<F18> c18, Function<T, F18> g18, Codec<F19> c19, Function<T, F19> g19, Codec<F20> c20, Function<T, F20> g20) {
        return new Codec<>() {

            @Override
            public T decode(DataInputWrapper in) throws IOException {
                F1 v1 = c1.decode(in);
                F2 v2 = c2.decode(in);
                F3 v3 = c3.decode(in);
                F4 v4 = c4.decode(in);
                F5 v5 = c5.decode(in);
                F6 v6 = c6.decode(in);
                F7 v7 = c7.decode(in);
                F8 v8 = c8.decode(in);
                F9 v9 = c9.decode(in);
                F10 v10 = c10.decode(in);
                F11 v11 = c11.decode(in);
                F12 v12 = c12.decode(in);
                F13 v13 = c13.decode(in);
                F14 v14 = c14.decode(in);
                F15 v15 = c15.decode(in);
                F16 v16 = c16.decode(in);
                F17 v17 = c17.decode(in);
                F18 v18 = c18.decode(in);
                F19 v19 = c19.decode(in);
                F20 v20 = c20.decode(in);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20);
            }

            @Override
            public void encode(DataOutputWrapper out, T value) throws IOException {
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

    default <C extends Collection<T>> Codec<Collection<T>> fixedCollection(IntFunction<C> collectionFactory, int expectedSize) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readFixedCollection(collectionFactory, Codec.this, expectedSize);
            }

            @Override
            public void encode(DataOutputWrapper out, Collection<T> collection) throws IOException {
                if (collection.size() != expectedSize) {
                    throw new MismatchedLengthException(expectedSize, collection.size());
                }

                out.writeFixedCollection(collection, Codec.this);
            }

        };
    }

    default <C extends Collection<T>> Codec<Collection<T>> dynCollection(IntFunction<C> collectionFactory) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readDynArrayAsCollection(collectionFactory, Codec.this);
            }

            @Override
            public void encode(DataOutputWrapper out, Collection<T> collection) throws IOException {
                out.writeDynCollection(collection, Codec.this);
            }

        };
    }

    default <C extends Collection<T>> Codec<Collection<T>> dynCollection(IntFunction<C> collectionFactory, int maxSize) {
        return new Codec<>() {

            @Override
            public C decode(DataInputWrapper in) throws IOException {
                return in.readDynArrayAsCollection(collectionFactory, Codec.this, maxSize);
            }

            @Override
            public void encode(DataOutputWrapper out, Collection<T> collection) throws IOException {
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
