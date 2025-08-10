package hu.cloudclient.binstd;

import java.io.IOException;

public interface Encodable {

    void encode(DataOutputWrapper out) throws IOException;

}
