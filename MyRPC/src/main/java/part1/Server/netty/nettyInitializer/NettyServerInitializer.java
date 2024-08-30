package part1.Server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;
import part1.Server.provider.ServiceProvider;
import part1.Server.netty.handler.NettyRPCServerHandler;
import part1.common.serializer.mySerializer.JsonSerializer;
import part1.common.serializer.myCode.MyDecode;
import part1.common.serializer.myCode.MyEncode;

/**
 * @ClassName NettyServerInitializer
 * @Description 初始化，主要负责序列化编码解码，需要解决netty的粘包问题
 * @Author 氟西汀
 * @Date 2024/6/26 20:18
 * @Version 1.0
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        消息格式[长度][消息体],解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//        计算当前等待发送消息的长度，写入到前四个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
//使用java序列化的方式，netty的自带的解码编码其支持传输这种结果
//        pipeline.addLast(new ObjectEncoder());
//        使用自定义的编解码器
        pipeline.addLast(new MyEncode(new JsonSerializer()));
        pipeline.addLast(new MyDecode());
//        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
//            @Override
//            public Class<?> resolve(String className) throws ClassNotFoundException {
//                return Class.forName(className);
//            }
//        }));
        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
