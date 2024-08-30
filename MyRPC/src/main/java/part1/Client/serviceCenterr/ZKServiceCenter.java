package part1.Client.serviceCenterr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part1.Client.serviceCenterr.balance.Impl.ConsistencyHashBalance;
import part1.Client.cache.serviceCache;
import part1.Client.serviceCenterr.ZkWatcher.watchZk;

import java.net.InetSocketAddress;
import java.util.List;

import static org.apache.curator.SessionFailRetryLoop.Mode.RETRY;

/**
 * @ClassName ServiceRegisterImpl
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/7/3 15:42
 * @Version 1.0
 */

public class ZKServiceCenter implements ServiceCenter {
//    curator 提供的zookeeper客户端
    private CuratorFramework client;
//    本地缓存
    private serviceCache cache;
//    zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
//    负责zookeeper客户端的初始化，并与zookeeper服务建立连接
    public ZKServiceCenter()  {
//        指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
//        zookeeper的地址固定，不管是服务提供者还是消费者都要与之建立连接
//        sessionTimeoutMs 与 zoo.cfg 的tickTime有关系
//        zk还会根据minSessionTimeout与maxTimeOut两个参数重新调整最后的超时值，
//        默认为tickTime的2倍和20倍
//        使用心跳监听机制
        this.client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181").sessionTimeoutMs(40000)
                .retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
//        初始化本地缓存
        cache = new serviceCache();
//        加入zookeeper事件监听
        watchZk watcher = new watchZk(client, cache);
        try {
            watcher.watchToUpdate(ROOT_PATH);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

//根据服务名返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
//            先从本地缓存中找找不到在从注册中心中找
            List<String> serviceList = cache.getAddressFromCache(serviceName);
            if (serviceList.isEmpty()){
                List<String> strings = client.getChildren().forPath("/" + serviceName);
            }
//            String s = serviceList.get(0);
//            负载均衡
            String address = new ConsistencyHashBalance().balance(serviceList);
            return parseAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkRetry(String serviceName) {
        boolean canRetry = false;
        try {
            List<String> serviceList = client.getChildren().forPath("/" + RETRY);
            for (String sN : serviceList){
                if (sN.equals(serviceName)){
                    System.out.println("服务"+serviceName+"在白名单上，可重试");
                    canRetry = true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return canRetry;
    }

    //    将地址->xxx.xxx.xxx.xxx:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName()+":"+serverAddress.getPort();
    }
//    将字符串解析为地址
    private InetSocketAddress parseAddress(String address){
        String[] split = address.split(":");
        return new InetSocketAddress(split[0],Integer.parseInt(split[1]));
    }
}
