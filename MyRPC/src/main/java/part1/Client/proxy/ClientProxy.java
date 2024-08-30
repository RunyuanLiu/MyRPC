package part1.Client.proxy;

import lombok.AllArgsConstructor;
import part1.Client.serviceCenterr.ServiceCenter;
import part1.Client.serviceCenterr.ZKServiceCenter;
import part1.Client.circuitBreaker.CircuitBreaker;
import part1.Client.circuitBreaker.CircuitBreakerProvider;
import part1.Client.rpcClient.Impl.NettyRPCClient;
import part1.Client.rpcClient.RPCClient;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;
import part1.Client.faultRetry.Impl.FixTimeRetryStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName ClientProxy
 * @Description 动态代理封装request对象
 * @Author 氟西汀
 * @Date 2024/6/25 10:27
 * @Version 1.0
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private RPCClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;
    public ClientProxy(){
        this.rpcClient = new NettyRPCClient();
        this.serviceCenter = new ZKServiceCenter();
        this.circuitBreakerProvider = new CircuitBreakerProvider();
    }
//    传入参数Service接口的class对象，反射封装成一个request
//    jdk动态代理，每一次代理对象调用方法，会经过此方法增强
//    反射获取request对象，socket发送给客户端
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args).paramsType(method.getParameterTypes()).build();
//        获取熔断器
        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(method.getName());
        if (!circuitBreaker.allowRequest()){
            return null;
        }
//        数据传输
        RPCResponse response ;
        //    幂等性判断，只对白名单上的服务进行重试
        if (serviceCenter.checkRetry(request.getInterfaceName())) {
//            调用retry框架进行重试
            response = new FixTimeRetryStrategy().doRetry(()->{
//                System.out.println("失败重试");
                throw new RuntimeException();
            });
        }else {
//            只调用一次
            response = rpcClient.sendRequest(request);

        }
//        记录reponse的状态，上报给熔断器
        if (response.getCode() == 200){
            circuitBreaker.recordSuccess();
        }
        if (response.getCode() == 500){
            circuitBreaker.recordFailture();
        }

        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }

}
