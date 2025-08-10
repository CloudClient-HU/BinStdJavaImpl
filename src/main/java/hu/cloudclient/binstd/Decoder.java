package hu.cloudclient.binstd;

import java.io.IOException;

public interface Decoder<T> {

    T decode(DataInputWrapper in) throws IOException;

}
