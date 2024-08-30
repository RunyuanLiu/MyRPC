package part1.common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import part1.common.Message.MessgaeType;
import part1.common.serializer.mySerializer.Serializer;

import java.util.List;

/**
 * @ClassName MyDecode
 * @Description 按照自定义的消息格式解码数据
 * @Author 氟西汀
 * @Date 2024/7/1 22:05
 * @Version 1.0
 */
@AllArgsConstructor
public class MyDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        读取消息类型
        short messageType = in.readShort();
        if (messageType != MessgaeType.REQUEST&&
        messageType != MessgaeType.RESPONSE){
            System.out.println("暂不支持此类数据");
            return ;
        }
//        2.读取序列化的类型
        short serializerType = in.readShort();
//        根据序列化的类型得到相应的序列化器
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
//        非空判断
        if(serializer == null){
           throw new RuntimeException("不存在对应的序列化器");
        }
//        读取序列化后的字节数
        int length = in.readInt();
//        读取序列化数组
        byte[] bytes = new byte[length];
         in.readBytes(bytes);
//         用对应序列化器解码字节数组
        Object deserialize = serializer.deserialize(bytes, messageType);
        out.add(deserialize);


    }
}
