package part1.common.serializer.mySerializer;

import java.io.*;

/**
 * @ClassName ObjectSerializer
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/7/1 22:18
 * @Version 1.0
 */

public class ObjectSerializer implements Serializer {
    /**
     * 利用java io 对象 -> 字节
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 字节数组转对象
     * @param bytes
     * @param messageType
     * @return
     */

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return obj;
    }
//0 代表Java原生序列化器
    @Override
    public int getType() {
        return 0;
    }
}
