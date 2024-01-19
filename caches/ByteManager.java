package caches;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByteManager {

    private ByteManager() {
    }

    public static <T extends Serializable> byte[] toBytes(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static <T extends Serializable> T fromBytes(byte[] bytes, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return clazz.cast(objectInputStream.readObject());
    }

    public static <T extends Serializable> byte[] convertListToBytes(List<T> stringList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        for (T element : stringList) {

            byte[] elementBytes = ByteManager.toBytes(element);
            dataOutputStream.writeInt(elementBytes.length);
            dataOutputStream.write(elementBytes);

        }

        dataOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static <T extends Serializable> List<T> convertBytesToList(byte[] bytes, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        List<T> objectList = new ArrayList<>();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        while (dataInputStream.available() > 0) {

            int objectSize = dataInputStream.readInt();
            byte[] objectBytes = new byte[objectSize];
            dataInputStream.readFully(objectBytes);

            T object = fromBytes(objectBytes, clazz);
            objectList.add(object);

        }

        dataInputStream.close();
        return objectList;
    }

    public static <K extends Serializable, V extends Serializable> byte[] convertMapToBytes(Map<K, V> map)
            throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            byte[] keyBytes = ByteManager.toBytes(key);
            dataOutputStream.writeInt(keyBytes.length);
            dataOutputStream.write(keyBytes);

            byte[] valueBytes = ByteManager.toBytes(value);
            dataOutputStream.writeInt(valueBytes.length);
            dataOutputStream.write(valueBytes);

        }

        dataOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> convertBytesToMap(byte[] bytes,
            Class<K> keyClass, Class<V> valueClass)
            throws IOException, ClassNotFoundException {
        Map<K, V> resultMap = new HashMap<>();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        while (dataInputStream.available() > 0) {

            int keyLength = dataInputStream.readInt();
            byte[] keyBytes = new byte[keyLength];
            dataInputStream.readFully(keyBytes);

            int valueLength = dataInputStream.readInt();
            byte[] valueBytes = new byte[valueLength];
            dataInputStream.readFully(valueBytes);

            K key = ByteManager.fromBytes(keyBytes, keyClass);
            V value = ByteManager.fromBytes(valueBytes, valueClass);

            resultMap.put(key, value);
        }

        dataInputStream.close();

        return resultMap;
    }

}
