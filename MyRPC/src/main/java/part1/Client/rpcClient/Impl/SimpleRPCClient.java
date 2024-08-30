package part1.Client.rpcClient.Impl;

import part1.Client.rpcClient.RPCClient;
import part1.Server.serviceRegister.ZKServiceRegister;
import part1.Server.serviceRegister.ServiceRegister;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName SimpleRPCClient
 * @Description 客户端改造
 * @Author 氟西汀
 * @Date 2024/7/3 16:20
 * @Version 1.0
 */

public class SimpleRPCClient implements RPCClient {
    private String hostName;
    private int port;
    private ServiceRegister serviceRegister;
    private  SimpleRPCClient(){
//        初始化注册中心，建立连接
        this.serviceRegister = new ZKServiceRegister();
    }
//    客户端发起一次请求调用，Socket建立连接，发起请求Request,得到相应Reponse
//    request是封装好的，不同的service需要进行不同的封装，客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
//        从注册中心根据接口名获取host和port
        InetSocketAddress inetSocketAddress = serviceRegister.serviceDiscovery(request.getInterfaceName());
        port = inetSocketAddress.getPort();
        hostName = inetSocketAddress.getHostName();
//        封装response请求
        try {
            Socket socket = new Socket(hostName, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println(request);
            oos.writeObject(request);
            oos.flush();
            RPCResponse response = (RPCResponse)ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }


    }
}
