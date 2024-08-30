package part1.Server.server.Impl;

import part1.Server.server.work.WorkThread;
import part1.Server.server.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @ClassName SimpleRPCServer
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/26 10:17
 * @Version 1.0
 */

public class SimpleRPCServer implements RPCServer {
    private Map<String , Object> serviceProvider;
//    private ServiceProvider serviceProvider;

    public SimpleRPCServer(Map<String, Object> serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动了");
            while(true){
                Socket socket = serverSocket.accept();
//                开启一个线程去处理
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }

    }

    @Override
    public void stop() {

    }
}
