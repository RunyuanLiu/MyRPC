package part1.Server.LimteRate;

import part1.Server.LimteRate.Impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RateLimitProvider
 * @Description 维护每个服务对应的限流器，并负责向外提供限流器
 * @Author 氟西汀
 * @Date 2024/7/4 15:54
 * @Version 1.0
 */

public class RateLimitProvider {
//    使用hashMap做存储，其中key是服务名，value是对应的限流器名
  private Map<String,RateLimit> rateLimitMap = new HashMap<>();
//  根据服务接口名获取限流器
    public RateLimit getRateLimit(String serviceName){
        if (!rateLimitMap.containsKey(serviceName)){
//            没有则定义限流器
            RateLimit rateLimit = new TokenBucketRateLimitImpl(100, 10);
            rateLimitMap.put(serviceName,rateLimit);
            return rateLimit;
        }
        return rateLimitMap.get(serviceName);
    }

}
