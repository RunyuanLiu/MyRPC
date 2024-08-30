package part1.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CircuitBreakerProvider
 * @Description 根据服务名维护不同的服务的熔断器
 * @Author 氟西汀
 * @Date 2024/7/4 17:20
 * @Version 1.0
 */

public class CircuitBreakerProvider {
    Map<String ,CircuitBreaker>  circuitBreakerMap = new HashMap<>();
    public CircuitBreaker getCircuitBreaker(String serviceName){
        if (!circuitBreakerMap.containsKey(serviceName)){
            CircuitBreaker circuitBreaker = new CircuitBreaker(3,0.5,10000);
            circuitBreakerMap.put(serviceName,circuitBreaker);
            return circuitBreaker;
        }
        return circuitBreakerMap.get(serviceName);
    }


}
