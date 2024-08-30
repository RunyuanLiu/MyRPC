package part1.Client.rpcClient;

import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @ClassName IOClient
 * @Description 客户端根据不同的Service进行动态代理
 * @Author 氟西汀
 * @Date 2024/6/25 10:13
 * @Version 1.0
 */
//底层通信
public class IOClient {
/**
 * 负责底层与服务端之间的通信，发送的request，
 * 接收的是reponse对象，客户端发起一次请求调用，Socekt建立连接
 * 得到相应的reponse, request(上层进行封装)，不同的servic需要
 * 进行不同的封装，客户端只知道Service接口，需要一层动态代理根据封装不同的Service
 */
public static RPCResponse sendRequest(String host, int port, RPCRequest request) {
    try {
        Socket socket = new Socket(host, port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println(request);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        RPCResponse response = (RPCResponse) objectInputStream.readObject();
        return response;
    } catch (Exception e) {
        System.out.println();
        return null;
    }
}
}
