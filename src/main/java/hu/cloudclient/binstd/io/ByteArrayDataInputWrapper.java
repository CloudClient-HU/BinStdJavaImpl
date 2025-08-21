package hu.cloudclient.binstd.io;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class ByteArrayDataInputWrapper extends DataInputWrapper {

    public final int maxBytes;
    public int bytesRead;

    public ByteArrayDataInputWrapper(ByteArrayDataInput delegate, Config config) {
        super(delegate, config);
        maxBytes = -1;
    }

    public ByteArrayDataInputWrapper(ByteArrayDataInput delegate) {
        super(delegate, Config.DEFAULT);
        maxBytes = -1;
    }

    public ByteArrayDataInputWrapper(byte[] data, Config config) {
        super(ByteStreams.newDataInput(data), config);
        maxBytes = data.length;
    }

    public ByteArrayDataInputWrapper(byte[] data) {
        super(ByteStreams.newDataInput(data), Config.DEFAULT);
        maxBytes = data.length;
    }

    @Deprecated
    @Override
    public void readFully(byte @NotNull [] b) throws IOException {
        if (maxBytes != -1) {
            bytesRead = maxBytes;
        }

        super.readFully(b);
    }

    @Deprecated
    @Override
    public void readFully(byte @NotNull [] b, int off, int len) throws IOException {
        bytesRead += len;
        super.readFully(b, off, len);
    }

    @Deprecated
    @Override
    public int skipBytes(int n) throws IOException {
        bytesRead += n;
        return super.skipBytes(n);
    }

    @Deprecated
    @Override
    public boolean readBoolean() throws IOException {
        bytesRead += 1;
        return super.readBoolean();
    }

    @Deprecated
    @Override
    public byte readByte() throws IOException {
        bytesRead += 1;
        return super.readByte();
    }

    @Deprecated
    @Override
    public int readUnsignedByte() throws IOException {
        bytesRead += 1;
        return super.readUnsignedByte();
    }

    @Deprecated
    @Override
    public short readShort() throws IOException {
        bytesRead += 2;
        return super.readShort();
    }

    @Deprecated
    @Override
    public int readUnsignedShort() throws IOException {
        bytesRead += 2;
        return super.readUnsignedShort();
    }

    @Deprecated
    @Override
    public char readChar() throws IOException {
        bytesRead += 2;
        return super.readChar();
    }

    @Deprecated
    @Override
    public int readInt() throws IOException {
        bytesRead += 4;
        return super.readInt();
    }

    @Deprecated
    @Override
    public long readLong() throws IOException {
        bytesRead += 8;
        return super.readLong();
    }

    @Deprecated
    @Override
    public float readFloat() throws IOException {
        bytesRead += 4;
        return super.readFloat();
    }

    @Deprecated
    @Override
    public double readDouble() throws IOException {
        bytesRead += 8;
        return super.readDouble();
    }

}
