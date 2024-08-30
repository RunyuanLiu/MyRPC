package part1.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import part1.common.Message.RPCResponse;

/**
 * @ClassName NettyClientHandler
 * @Description 配置NettyClientHandler类，指定接收消息的处理方法
 * @Author 氟西汀
 * @Date 2024/7/1 21:08
 * @Version 1.0
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
//        接收到response，给channel设计别名，让sendRequest读取response
        AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
        channelHandlerContext.channel().attr(key).set(rpcResponse);
        channelHandlerContext.channel().close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
