package part1.common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import part1.common.Message.MessgaeType;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;
import part1.common.serializer.mySerializer.Serializer;

/**
 * @ClassName MyEnode
 * @Description 依次按照自定义的消息写入，传入的数据为request或者reponse
 * 需要有一个serilaze器，负责将传入的对象进行序列化
 *
 * @Author 氟西汀
 * @Date 2024/7/1 21:54
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public class MyEncode extends MessageToByteEncoder {
    private Serializer serializer;
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println(msg.getClass());
        if (msg instanceof RPCRequest){
            out.writeShort(MessgaeType.REQUEST);
        }
        if (msg instanceof RPCResponse){
            out.writeShort(MessgaeType.RESPONSE);
                    }
//        写入序列化方式
        out.writeShort(serializer.getType());
//       得到序列化数组
        byte[] serialize = serializer.serialize(msg);
//        写入长度
        out.writeInt(serialize.length);
//        写入序列化字节数组
        out.writeBytes(serialize);


    }
}
