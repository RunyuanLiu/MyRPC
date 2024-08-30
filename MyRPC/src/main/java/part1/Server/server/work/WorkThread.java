package part1.Server.server.work;

import lombok.AllArgsConstructor;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @ClassName WorkThread
 * @Description 负责解析得到的request请求，执行服务方法，返回给客户端
 * 1.从request得到interfaceName
 * 2.根据interfaceName在serviceProvide Map中获取服务端的实现类
 * 3.从request中得到方法名，参数，利用反射执行服务中的方法
 * 4.得到结果，封装成reponse，写入socket
 * @Author 氟西汀
 * @Date 2024/6/26 10:45
 * @Version 1.0
 */
@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;
    private Map<String, Object> serviceProvide;
    @Override
    public void run() {
        try{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//        读取客户端穿过来的request
            RPCRequest request = (RPCRequest)ois.readObject();
//            使用反射调用方法获得返回值
            RPCResponse response = getResponse(request);
//            将response写入socket
            oos.writeObject(response);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("从IO中读取输出出错");
        }
    }
    private RPCResponse getResponse(RPCRequest request){
//        得到服务名
        String interfaceName = request.getInterfaceName();
//        得到服务名所对应的实现类
        Object service = serviceProvide.get(interfaceName);
//        反射调方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsType());
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("方法执行失败");
            return RPCResponse.fail();
        }

    }

}
