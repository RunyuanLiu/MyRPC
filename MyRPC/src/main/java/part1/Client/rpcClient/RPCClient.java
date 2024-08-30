package part1.Client.rpcClient;

import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

public interface RPCClient {
    /**
     * 不同的网络有不同的实现方式
     * @param request
     * @return
     */
    RPCResponse sendRequest(RPCRequest request);
}
