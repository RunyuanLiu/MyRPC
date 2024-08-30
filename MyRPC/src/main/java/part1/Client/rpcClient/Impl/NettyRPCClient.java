package part1.Client.rpcClient.Impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part1.Client.rpcClient.RPCClient;
import part1.Client.serviceCenterr.ServiceCenter;
import part1.Client.serviceCenterr.ZKServiceCenter;
import part1.Client.netty.nettyInitailzer.NettyClientInitializer;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.net.InetSocketAddress;

/**
 * @ClassName NettyRPCClient
 * @Description 在注册中心去查找服务对应的ip和port,再去连接对应的服务器
 * @Author 氟西汀
 * @Date 2024/6/26 20:57
 * @Version 1.0
 */

public class NettyRPCClient implements RPCClient {
    private ServiceCenter serviceCenterr;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    public NettyRPCClient()  {
        this.serviceCenterr = new ZKServiceCenter();
    }
//    private String host;
//    private int port;

//    public NettyRPCClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }
//    netty客户端初始化，重复使用
    static{
     eventLoopGroup = new NioEventLoopGroup();
     bootstrap = new Bootstrap();
     bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
             .handler(new NettyClientInitializer());
}

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
//        从注册中心中获取后host和port
        InetSocketAddress inetSocketAddress = serviceCenterr.serviceDiscovery(request.getInterfaceName());
        String hostName = inetSocketAddress.getHostName();
        int port = inetSocketAddress.getPort();

        try{
            ChannelFuture channelFuture = bootstrap.connect(hostName, port);
            Channel channel = channelFuture.channel();
//            发送数据
            channel.writeAndFlush(request);
//            sync()堵塞获取结果
            channel.closeFuture().sync();
//            阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel的内容
//            AttributeKey是线程隔离的，不会有线程安全问题
//            当前场景下选择堵塞获取结果 实际上不应通过阻塞，可以通过回调函数
//            其他场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addLister
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();
            System.out.println(response);
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
