package part1.Client.serviceCenterr.ZkWatcher;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import part1.Client.cache.serviceCache;
/**
 * @ClassName watchZk
 * @Description watchZK 监听zookeeper客户端
 * @Author 氟西汀
 * @Date 2024/7/3 18:06
 * @Version 1.0
 */

public class watchZk {
//    curator 提供的zookeeper客户端
    private CuratorFramework client;
//    本地缓存
    private serviceCache serviceCache;

    public watchZk(CuratorFramework client, serviceCache serviceCache) {
        this.client = client;
        this.serviceCache = serviceCache;
    }
    /**
     * 监听当前节点和子节点的更新、删除、创建
     *
     */
    public void watchToUpdate(String path) throws InterruptedException{
        CuratorCache curatorCache = CuratorCache.build(client, "/");
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
//                第一个参数：事件类型(枚举)
//                第二个参数：节点更新前的状态、数据
//                第三个参数：节点更新后的状态、数据
//                创建节点时：节点刚被创建， 不存在 更新前节点，所以第二个参数为空
//                删除节点时， 节点被删除， 不存在，更新后节点，所以第三个参数为空
//                节点创建时没有赋值 create/curator/app1 只创建节点，在这种情况下，更新前节点的date为null
                switch (type.name()){
                    case "NODE_CREATED"://监听器第一次执行时节点存在也会出发次事件
//                        获取更新的节点的路径
                        String path = new String(childData1.getPath());
                        String[] pathList = path.split("/");
                        if (pathList.length<=2) break;
                        else {
                            String serviceName = pathList[1];
                            String address = pathList[2];
//                            将新注册的服务加入到本地缓存中
                            serviceCache.addServiceToCache(serviceName,address);
                        }
                        break;
//                        按照格式，读取
                    case "NODE_CHANGED": // 节点更新
                        if (childData.getData()!=null){
                            System.out.println("修改前的数据:"+new String(childData.getData()));
                        }else {
                            System.out.println("节点第一次赋值！");
                        }
                        System.out.println("修改后的数据:"+new String(childData1.getData()));
                        break;
                    case "NODE_DELTED"://节点删除
                        String path_d = new String(childData.getPath());
                        String[] pathList_d = path_d.split("/");
                        if (pathList_d.length<=2)break;
                        else {
                            String serviceName = pathList_d[1];
                            String address = pathList_d[2];
                            serviceCache.deleteAddressFromCache(serviceName,address);
                        }
                    default:
                        break;


                }
            }
        });
//        开启监听

    }
}
