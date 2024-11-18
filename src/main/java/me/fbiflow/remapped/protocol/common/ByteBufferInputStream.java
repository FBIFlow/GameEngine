package me.fbiflow.remapped.protocol.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (!buffer.hasRemaining()) {
            return -1; // Конец потока
        }
        return buffer.get() & 0xFF; // Возвращаем байт
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!buffer.hasRemaining()) {
            return -1; // Конец потока
        }
        int bytesRead = Math.min(len, buffer.remaining());
        buffer.get(b, off, bytesRead);
        return bytesRead;
    }
}