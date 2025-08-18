package hu.cloudclient.binstd.exception;

import java.io.IOException;

public class MismatchedLengthException extends IOException {

    public MismatchedLengthException(int min, int max, int value) {
        super("length/size " + value + " not in " + min + ".." + max);
    }

    public MismatchedLengthException(int expected, int actual) {
        super("length/size " + expected + " != " + actual);
    }

}
