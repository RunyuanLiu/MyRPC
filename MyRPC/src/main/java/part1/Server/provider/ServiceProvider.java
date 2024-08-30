package part1.Server.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import part1.Server.LimteRate.RateLimitProvider;
import part1.Server.serviceRegister.ZKServiceRegister;
import part1.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ServiceProvider
 * @Description 将Map的简单实现 变为类
 * 存放服务名与服务对应的实现类
 * 服务启动是要暴露其相关的实现类
 * 根据request中interface 调用服务端中相关的实现类
 * 最新版，将服务注册到服务中心而不是本地
 * @Author 氟西汀
 * @Date 2024/6/26 11:07
 * @Version 1.0
 */
@AllArgsConstructor
@Data
public class ServiceProvider {
//    一个实现类可以实现多个接口
    private Map<String,Object> interfaceProvider;
    private int port;
    private String hostName;
    private ServiceRegister serviceRegister;
    private RateLimitProvider rateLimit;
    public ServiceProvider(int port, String hostName) {
        this.hostName = hostName;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
        this.rateLimit = new RateLimitProvider();
    }
    public void provideServiceInterface(Object service){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class clazz : interfaces){
//            本地映射表
            interfaceProvider.put(clazz.getName(),service);
//            再注册中心注册服务

            serviceRegister.register(serviceName,new InetSocketAddress(hostName,port),true);
        }

    }
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
