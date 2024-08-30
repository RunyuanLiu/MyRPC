package part1.Server.server.Impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import part1.Server.netty.nettyInitializer.NettyServerInitializer;
import part1.Server.provider.ServiceProvider;
import part1.Server.server.RPCServer;

/**
 * @ClassName NettyRPCServer
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/26 19:55
 * @Version 1.0
 */

public class NettyRPCServer implements RPCServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
//        netty服务线程组boss负责建立连接(accpet请求)，work负责具体的请求(读写请求)
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("Netty服务端启动了");
        try{
//            启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            初始化
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(serviceProvider));
//          同步阻塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
//            死循环监听
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    @Override
    public void stop() {

    }
}
