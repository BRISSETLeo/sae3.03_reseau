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
import java.util.List;

public class ByteManager {

    private ByteManager() {
    }

    public static <T extends Serializable> byte[] getBytes(T object) throws IOException {
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

            byte[] elementBytes = ByteManager.getBytes(element);
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

}
