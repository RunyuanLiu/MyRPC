package part1.Server.server.Impl;

import part1.Server.server.work.WorkThread;
import part1.Server.server.RPCServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolRPCServer
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/26 10:26
 * @Version 1.0
 */

public class ThreadPoolRPCServer implements RPCServer {
    private Map<String , Object> serverProvide;
    private final ThreadPoolExecutor threadPool;

    /**
     * ArrayBlockingQueue<>(100) 适合于生产者消费者模型的实现，生产者线程向队列中添加元素，消费者线程从队列中取出元素进行处理
     * 其是有界的，队列的容量是固定的，不会随着运行时的需求而扩展。
     * 阻塞（Blocking）：当队列已满时，尝试向队列中添加元素的操作将会阻塞，直到队列中有空间为止。同样，当队列为空时，尝试从队列中取出元素的操作也会阻塞，直到队列中有元素为止。
     * 线程安全（Thread-safe）：ArrayBlockingQueue 内部使用锁来实现多线程间的安全访问，因此可以安全地用于多线程环境中。
     * @param serverProvide
     */
    public ThreadPoolRPCServer(Map<String, Object> serverProvide) {
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 1000,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        this.serverProvide = serverProvide;
    }

    public ThreadPoolRPCServer(Map<String, Object> serverProvide,
                               int corePoolSize, int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue) {
        this.serverProvide = serverProvide;
        this.threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
    }

    @Override
    public void start(int port) {
//        使用线程池开启服务
        System.out.println("服务启动了");
        try {
            ServerSocket serverSocket = new ServerSocket();
            while (true){
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket,serverProvide));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void stop() {

    }
}
