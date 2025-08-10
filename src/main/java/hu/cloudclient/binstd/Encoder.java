package hu.cloudclient.binstd;

import java.io.IOException;

public interface Encoder<T> {

    void encode(DataOutputWrapper out, T value) throws IOException;

    default Encodable asEncodable(T value) {
        return out -> this.encode(out, value);
    }

}
