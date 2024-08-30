package part1.Server.serviceRegister;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import part1.Client.cache.serviceCache;

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

public class ZKServiceRegister implements ServiceRegister {
//    curator 提供的zookeeper客户端
    private CuratorFramework client;
//    本地缓存
    private serviceCache cache;
//    zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
//    负责zookeeper客户端的初始化，并与zookeeper服务建立连接
    public ZKServiceRegister(){
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
    }
    @Override
    public void register(String serviceName, InetSocketAddress severAddress,boolean canRetry) {
//    serviceName创建成永久节点，服务提供者下线时，不删除服务，只删除地址
        try {
            if (client.checkExists().forPath("/"+serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/"+serviceName);
            }
//            路径地址， 一个/代表一个节点
            String path = "/"+serviceName+"/"+getServiceAddress(severAddress);
//            临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
//          如果服务是幂等的，就将其新增到节点中
            if (canRetry){
                path = "/"+RETRY+"/"+serviceName;
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (Exception e) {
            System.out.println("此服务已存在");
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
            String s = serviceList.get(0);
            return parseAddress(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
