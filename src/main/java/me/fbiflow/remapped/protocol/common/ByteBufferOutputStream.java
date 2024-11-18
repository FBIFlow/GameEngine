package me.fbiflow.remapped.protocol.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ByteBufferOutputStream extends java.io.OutputStream {
    private final SocketChannel socketChannel;
    private final ByteBuffer buffer;

    public ByteBufferOutputStream(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void write(int b) throws IOException {
        if (buffer.remaining() == 0) {
            flush();
        }
        buffer.put((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (buffer.remaining() < len) {
            flush();
        }
        buffer.put(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
        buffer.clear();
    }
}
