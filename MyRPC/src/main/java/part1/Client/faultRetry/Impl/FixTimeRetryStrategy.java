package part1.Client.faultRetry.Impl;

import part1.common.Message.RPCResponse;
import part1.Client.faultRetry.RetryStrategy;
import com.github.rholder.retry.*;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName FixTimeRetryStrategy
 * @Description 使用guava-retry实现固定时间间隔重试
 * @Author 氟西汀
 * @Date 2024/7/4 9:57
 * @Version 1.0
 */

public class FixTimeRetryStrategy implements RetryStrategy {
    @Override
    public RPCResponse doRetry(Callable<RPCResponse> callable) throws Exception {
       Retryer<RPCResponse> retryer =RetryerBuilder.<RPCResponse>newBuilder()
//               无论出现什么异常都重试
               .retryIfException()
//               当返回结果为error时重试
               .retryIfResult(response-> Objects.equals(response.getCode(),500))
//               设置等待时常
               .withWaitStrategy(WaitStrategies.fixedWait(2l, TimeUnit.SECONDS))
//               设置停止策略:重试达到5次
               .withStopStrategy(StopStrategies.stopAfterAttempt(5))
               .withRetryListener(new RetryListener() {
                   @Override
                   public <V> void onRetry(Attempt<V> attempt) {
                       System.out.println("RetryListener:第"+attempt.getAttemptNumber()+"次调用");
                   }
               }).build();
        return retryer.call(callable);
    }
}
