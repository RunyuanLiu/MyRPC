package part1.Client.serviceCenterr.balance.Impl;

import part1.Client.serviceCenterr.balance.LoadBalance;

import java.util.List;

/**
 * @ClassName RoundLoadBalance
 * @Description 轮询算法实现
 * @Author 氟西汀
 * @Date 2024/7/3 20:31
 * @Version 1.0
 */

public class RoundLoadBalance implements LoadBalance {
   private int choose =-1;
    @Override
    public String balance(List<String> addressList) {
       choose++;
       choose = choose%addressList.size();
       System.out.println("负载均衡算法选择了"+choose+"服务器");
       String address = addressList.get(choose);
       return address;
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
