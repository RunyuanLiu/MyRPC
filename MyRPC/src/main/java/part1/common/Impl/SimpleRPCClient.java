package part1.common.Impl;

import lombok.AllArgsConstructor;
import part1.Client.rpcClient.RPCClient;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @ClassName SimpleRPCClient
 * @Description 使用Java的BIO方式实现
 * @Author 氟西汀
 * @Date 2024/6/26 19:36
 * @Version 1.0
 */
@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;

    /**
     * 客户端发起一次请求调用，Socket建立连接，发送请求Request，得到响应的response
     * request是封装好的，不同的service需要进行不同的封装
     * 客户端只知道Service接口，需要一层动态代理根据反射封装不同的service
     * @param request
     * @return
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
//        发起一次socekt请求
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println(request);
            oos.writeObject(request);
            oos.flush();
            RPCResponse response = (RPCResponse)ois.readObject();
            return response;
//            return null;
        }catch (Exception e){
            System.out.println();
            return null;
        }
    }
}
