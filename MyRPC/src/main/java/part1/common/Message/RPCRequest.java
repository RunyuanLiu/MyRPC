package part1.common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RPCRequest
 * @Description 定义请求消息的格式
 * @Author 氟西汀
 * @Date 2024/6/25 9:32
 * @Version 1.0
 */
//注解可以自动为类生成一个建造者模式的构造方法，
//        使得创建对象时可以使用链式调用来设置属性，
//        而不必手动编写多个构造方法或者使用复杂的构造器。
@Data
@Builder
public class RPCRequest implements Serializable {
    /**
     * 服务端不会只有一个服务一个方法，因此只传递参数不知道会调用
     *那个方法，因此在RPC请求中，clinet发送应该时需要调用的service接口名，方法名，
     * 参数名，参数类别，这样服务端就能根据这些信息反射调用相应的方法
     */
//    服务器类名，客户端只知道接口
    private String interfaceName;
//    调用的方法名
    private String methodName;
//    参数列表
    private Object[] params;
//    参数类型
    private Class<?>[] paramsType;

}
