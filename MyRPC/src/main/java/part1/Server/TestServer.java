package part1.Server;

import part1.Server.server.Impl.SimpleRPCServer;
import part1.Server.provider.ServiceProvider;
import part1.Server.server.RPCServer;
import part1.common.service.Impl.BlogServiceImpl;
import part1.common.service.Impl.UserServiceImpl;

/**
 * @ClassName TestServer
 * @Description hashMap 添加多个服务的实现类
 * @Author 氟西汀
 * @Date 2024/6/26 10:03
 * @Version 1.0
 */

public class TestServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        BlogServiceImpl blogService = new BlogServiceImpl();
//        HashMap<String, Object> serviceProvide = new HashMap<>();
//        serviceProvide.put("part1.Service.blogService",blogService);
//        serviceProvide.put("part1.Service.userService",userService);
        ServiceProvider serviceProvider = new ServiceProvider(8080,"localhost");
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        RPCServer RPCServer = new SimpleRPCServer(serviceProvider.getInterfaceProvider());
        RPCServer.start(8899);


    }
}
