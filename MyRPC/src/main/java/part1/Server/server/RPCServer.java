package part1.Server.server;

/**
 * 开闭原则
 */

public interface RPCServer {
    void start(int port);
    void stop();
}
