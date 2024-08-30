package part1.Client.serviceCenterr;

import java.net.InetSocketAddress;

/**
 * 定义服务注册接口，实现两大基本功能，注册：保存服务与地址
 * 查询：根据服务名查找地址
 */
public interface ServiceCenter {

    InetSocketAddress serviceDiscovery(String serviceName);
    boolean checkRetry(String serviceName);
}
