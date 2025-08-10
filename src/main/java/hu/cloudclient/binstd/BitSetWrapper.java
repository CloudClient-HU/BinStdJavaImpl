package hu.cloudclient.binstd;

import org.jetbrains.annotations.Range;

import java.util.BitSet;

public class BitSetWrapper {

    // No support for reading and writing with the same instance
    private int index;
    private final BitSet bits;

    public BitSetWrapper(int nBits) {
        this.bits = new BitSet(nBits);
    }

    public BitSetWrapper(byte[] bytes) {
        this.bits = BitSet.valueOf(bytes);
    }

    public int readUnsigned(@Range(from = 1, to = 32) int len) {
        int i = 0;
        for (int j = 0; j < len; j++) {
            i |= (readBoolean() ? 1 : 0) << j;
        }
        return i;
    }

    public void writeUnsigned(int value, @Range(from = 1, to = 32) int nBits) {
        for (int j = 0; j < nBits; j++) {
            boolean bit = ((value >> j) & 1) != 0;
            writeBoolean(bit);
        }
    }

    public boolean readBoolean() {
        return bits.get(index++);
    }

    public void writeBoolean(boolean value) {
        if (value) {
            bits.set(index);
        }

        index++;
    }

    public byte[] asBytes() {
        return bits.toByteArray();
    }
}
