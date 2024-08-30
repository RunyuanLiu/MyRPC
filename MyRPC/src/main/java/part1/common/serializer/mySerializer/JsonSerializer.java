package part1.common.serializer.mySerializer;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import part1.common.Message.RPCRequest;
import part1.common.Message.RPCResponse;

/**
 * @ClassName JsonSerializer
 * @Description json的序列化方式是通过将对象转化成字符串，丢失了data对象的类信息，
 * 因此deserialize 需要了解对象的类信息， 根据类信息将jsonObject->对应的对象
 * @Author 氟西汀
 * @Date 2024/7/1 22:29
 * @Version 1.0
 */

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = JSON.toJSONBytes(object);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                RPCRequest rpcRequest = JSON.parseObject(bytes, RPCRequest.class);
                Object[] objects = new Object[rpcRequest.getParams().length];
//                将json字符串转换成对应的对象，fastjason可以取出基本数据类型，不用转化
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramsType = rpcRequest.getParamsType()[i];
//                    检查第i个参数的实际类型是否可以赋值给paramType表示的类型，如果不能，则说明类型不匹配，可能需要转换
                    if (!paramsType.isAssignableFrom(rpcRequest.getParams()[i].getClass())){
//                        将(JSONObject) rpcRequest.getParams()[i]转换为rpcRequest.getParamsType()[i]类型
                       objects[i] = JSONObject.toJavaObject((JSONObject) rpcRequest.getParams()[i],rpcRequest.getParamsType()[i]);
                    }else {
                        objects[i] = rpcRequest.getParams()[i];
                    }

                }
                rpcRequest.setParams(objects);
                obj = rpcRequest;
                break;
            case 1:
                RPCResponse rpcResponse = JSON.parseObject(bytes, RPCResponse.class);
                Class<?> dataType = rpcResponse.getDataType();
//                对数据的类型进行判断
                if (!dataType.isAssignableFrom(rpcResponse.getData().getClass())){
                    rpcResponse.setData(JSONObject.toJavaObject((JSONObject)rpcResponse.getData(),dataType));
                }
                obj = rpcResponse;
                break;
            default:
                System.out.println("暂不支持此种消息");
                throw new RuntimeException();
        }


        return obj;
    }
//1代表json序列化方式
    @Override
    public int getType() {
        return 1;
    }
}
