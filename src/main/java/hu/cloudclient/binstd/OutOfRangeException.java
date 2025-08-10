package hu.cloudclient.binstd;

import java.io.IOException;

public class OutOfRangeException extends IOException {

    public OutOfRangeException(int min, int max, int value) {
        super(value + " not in " + min + ".." + max);
    }

}
