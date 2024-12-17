package me.fbiflow.gameengine.util;

import java.io.*;

public class SerializeUtil {
    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] serialized = serialize(obj);
        byte[] encrypted = encrypt(serialized);
        return encrypted;
    }
    public static Object fromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        byte[] decrypted = decrypt(bytes);
        Object deserialized = deserialize(decrypted);
        return deserialized;
    }

    private static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    private static byte[] encrypt(byte[] data) {
        byte[] encryptedData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            encryptedData[i] = (byte) (data[i] + 1);
        }
        return encryptedData;
    }

    private static byte[] decrypt(byte[] data) {
        byte[] decryptedData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            decryptedData[i] = (byte) (data[i] - 1);
        }
        return decryptedData;
    }
}