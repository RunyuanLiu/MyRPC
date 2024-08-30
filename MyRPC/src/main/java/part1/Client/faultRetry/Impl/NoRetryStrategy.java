package part1.Client.faultRetry.Impl;

import part1.common.Message.RPCResponse;
import part1.Client.faultRetry.RetryStrategy;

import java.util.concurrent.Callable;

/**
 * @ClassName NoRetryStrategy
 * @Description 不重试策略
 * @Author 氟西汀
 * @Date 2024/7/4 9:53
 * @Version 1.0
 */

public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RPCResponse doRetry(Callable<RPCResponse> callable) throws Exception {
        return callable.call();
    }
}
