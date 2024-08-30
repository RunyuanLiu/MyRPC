package part1.Client.proxy;

import lombok.AllArgsConstructor;
import part1.Client.rpcClient.RPCClient;

/**
 * @ClassName RPCClienrProxy
 * @Description 动态代理Service类，封装不同的Service请求为
 * request对象，并且持有一个RPCCLient，负责与服务端的通信
 * @Author 氟西汀
 * @Date 2024/6/26 19:50
 * @Version 1.0
 */
@AllArgsConstructor
public class RPCClientProxy {
    private RPCClient rpcClient;
}
