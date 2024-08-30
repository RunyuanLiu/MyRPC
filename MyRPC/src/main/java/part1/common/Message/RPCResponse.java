package part1.common.Message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RPCResponse
 * @Description 定义返回信息格式(类似http格式)
 * @Author 氟西汀
 * @Date 2024/6/25 9:48
 * @Version 1.0
 */
@Builder
@Data
public class RPCResponse implements Serializable {
//    状态码
    private int code;
//    状态信息
    private String message;
//    具体数据
    private Object data;
    private Class<?> dataType;
//    构造成功信息
public static RPCResponse success(Object data) {
    return RPCResponse.builder()
            .code(200)
            .data(data).build();
}
//    构造失败信息
public  static RPCResponse fail() {
    return RPCResponse.builder()
            .code(500).message("服务器发生错误")
            .build();
}
}
