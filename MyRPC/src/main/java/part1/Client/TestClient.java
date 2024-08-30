package part1.Client;

import part1.Client.proxy.ClientProxy;
import part1.common.service.UserService;
import part1.common.pojo.User;

/**
 * @ClassName TestClient
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/7/2 11:33
 * @Version 1.0
 */

public class TestClient {
    public static void main(String[] args) {
//      创建clientProxy对象
        ClientProxy clientProxy = new ClientProxy();
//        通过clientProxy对象获取代理对象
        UserService proxy = clientProxy.getProxy(UserService.class);
//        调用代理对象
        User userByUsrId = proxy.getUserByUsrId(1);
        System.out.println("从服务端得到的user="+ userByUsrId.toString());
    }

}
