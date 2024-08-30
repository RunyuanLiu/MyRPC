
package part1.Client.faultRetry;
import part1.common.Message.RPCResponse;

import java.util.concurrent.Callable;

/**
 *
 * 重试策略
 */
public interface RetryStrategy {
    RPCResponse doRetry(Callable<RPCResponse> callable) throws Exception;
}
