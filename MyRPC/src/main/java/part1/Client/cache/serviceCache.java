package part1.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName serviceCache
 * @Description 创建本地缓存，缓存服务地址信息，避免每次调用服务都去zookeepr中查找地址
 * @Author 氟西汀
 * @Date 2024/7/3 17:06
 * @Version 1.0
 */

public class serviceCache {
    //key: serviceName 服务名
//    value: addressList 服务提供者
    private  static Map <String , List<String>> cache = new HashMap<>();
//添加服务
    public void addServiceToCache(String serviceName, String address){
        if (cache.containsKey(serviceName)){
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+"的服务加入到本地缓存中");
        }else {
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
        }
    }

//    从缓存中取出服务地址
    public List<String> getAddressFromCache(String serviceName){
        if (!cache.containsKey(serviceName)){
            return null;
        }
        List<String> address = cache.get(serviceName);
        return address;
    }

//    从缓存在删除服务地址
    public void deleteAddressFromCache(String serviceName, String address){
        List<String> addressList = cache.get(serviceName);
        addressList.remove(address);
        System.out.println("将name为"+serviceName+"和地址为"+address+"的服务从本地缓存中删除");
    }



}
