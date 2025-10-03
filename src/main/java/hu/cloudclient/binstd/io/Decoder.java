package hu.cloudclient.binstd.io;

import java.io.IOException;

public interface Decoder<T> {

	T decode(DataInputWrapper in) throws IOException;

}
