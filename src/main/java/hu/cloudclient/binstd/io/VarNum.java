package hu.cloudclient.binstd.io;

public class VarNum {

	public static int sizeOf(int i) {
		if (i < 0) {
			return 5;
		} else if (i < (1 << (7))) {
			return 1;
		} else if (i < (1 << (7 * 2))) {
			return 2;
		} else if (i < (1 << (7 * 3))) {
			return 3;
		} else if (i < (1 << (7 * 4))) {
			return 4;
		} else {
			return 5;
		}
	}

	public static int sizeOf(long l) {
		if (l < 0) {
			return 10;
		} else if (l < (1L << (7))) {
			return 1;
		} else if (l < (1L << (7 * 2))) {
			return 2;
		} else if (l < (1L << (7 * 3))) {
			return 3;
		} else if (l < (1L << (7 * 4))) {
			return 4;
		} else if (l < (1L << (7 * 5))) {
			return 5;
		} else if (l < (1L << (7 * 6))) {
			return 6;
		} else if (l < (1L << (7 * 7))) {
			return 7;
		} else if (l < (1L << (7 * 8))) {
			return 8;
		} else {
			return 9;
		}
	}

}
