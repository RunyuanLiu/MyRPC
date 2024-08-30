package part1.Client.serviceCenterr.balance.Impl;

import part1.Client.serviceCenterr.balance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @ClassName RandomLoadBalance
 * @Description 随机算法实现
 * @Author 氟西汀
 * @Date 2024/7/3 20:28
 * @Version 1.0
 */

public class RandomLoadBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size()-1);
        System.out.println("负载均衡选择了"+choose+"服务");
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
