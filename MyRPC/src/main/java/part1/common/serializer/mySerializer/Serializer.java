package part1.common.serializer.mySerializer;

//自定义编解码器
public interface Serializer {
//    将对象序列化为字节数组
    byte[] serialize(Object object);
//    从字节数组中反序列化成消息，使用Java自带序列化方式不用messageType也能得到
//    相应的对象（序列化字节数组中包含类信息
//    其他方式需要指定消息格式，再根据message转换成相应的对象
    Object deserialize(byte[] bytes, int messageType);
//    返回使用的序列器，是那个
//    0：java自带序列化方式，1：json序列化方式
    int getType();
//    根据序号取出序列化器
    static  Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;

        }
    }
}
