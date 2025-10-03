package hu.cloudclient.binstd.io;

import java.io.IOException;

public interface Encoder<T> {

	void encode(DataOutputWrapper out, T value) throws IOException;

}
