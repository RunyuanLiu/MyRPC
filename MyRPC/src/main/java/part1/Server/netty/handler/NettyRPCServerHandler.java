package part1.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import part1.Server.LimteRate.RateLimit;
import part1.Server.provider.ServiceProvider;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.lang.reflect.Method;

/**
 * @ClassName NettyRPCServerHandler
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/26 20:46
 * @Version 1.0
 */
@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
       RPCResponse response =  getResponse(msg);
       ctx.writeAndFlush(response);
       ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
    RPCResponse getResponse(RPCRequest request){
//        得到服务器名
        String interfaceName = request.getInterfaceName();
//        进行接口限流
        RateLimit rateLimit = serviceProvider.getRateLimit().getRateLimit(interfaceName);
        if (!rateLimit.getToken()){
            //获取令牌失败，进行限流降级，并返回结果
            return RPCResponse.fail();
        }
//        得到服务器的实现类
        Object service = serviceProvider.getService(interfaceName);
//        反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsType());
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            return RPCResponse.fail();
        }

    }
}
