package hu.cloudclient.binstd.io;

import java.io.IOException;

public record InstanceCodec<T>(T instance) implements Codec<T> {

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

}
