package hu.cloudclient.binstd;

import java.io.IOException;

public interface Encoder<T> {

    void encode(DataOutputWrapper out, T value) throws IOException;

}
