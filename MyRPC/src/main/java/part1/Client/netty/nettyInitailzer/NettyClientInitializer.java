package part1.Client.netty.nettyInitailzer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import part1.Client.netty.handler.NettyClientHandler;


/**
 * @ClassName NettyClientInitializer
 * @Description 配置netty对消息的处理机制
 * 指定编码器（将消息转为字节数组），解码器（将字节数组转为消息）
 * 指定消息格式，消息长度，解决粘包问题
 * 粘包问题：netty默认底层通过TCP进行传输，TCP是面向流的协议，接收方收到数据时无法直接得知一条数据的具体字节数，
 * 不知道数据的边界，由于TCP的流量监控机制，发生粘包或拆包，会导致接收的一个包可能会有多条消息或者不足一条消息，从而会出现接收方少读或者多读导致消息不能读完全的情况发生
 * 在发送消息是，先告诉接收方消息的长度，让接收方读取指定长度的字节，就能避免这个问题
 * 指定对接收消息的处理handler
 * addlast没有先后顺序，netty通过加入的类实现的接口来自动识别类实现的是什么功能
 * @Author 氟西汀
 * @Date 2024/7/1 20:27
 * @Version 1.0
 */

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        消息格式 长度 消息体 ，解决粘包问题

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//        计算当前待发消息的长度，写入前四个字节
        pipeline.addLast(new LengthFieldPrepender(4));
//      使用java序列化方式，netty的自带解码编码器支持这种传输结构
        pipeline.addLast(new ObjectEncoder());
//        使用了Netty中的objectDecoder，他用于将字节流解码为java对象
//        在ObjectDecoder的构造器函数中传入了一个ClassResolver对象，用于解析类目并加载相应的类
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));
        pipeline.addLast(new NettyClientHandler());
    }
}
