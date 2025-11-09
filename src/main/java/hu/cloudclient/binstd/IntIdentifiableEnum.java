package hu.cloudclient.binstd;

public interface IntIdentifiableEnum extends IntIdentifiable {

	int ordinal();

	@Override
	default int getIntId() {
		return ordinal();
	};
}
