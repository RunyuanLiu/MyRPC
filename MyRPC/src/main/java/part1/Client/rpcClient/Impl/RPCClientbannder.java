//package part1.Client.rpcClient.Impl;
//
//import part1.Client.proxy.ClientProxy;
//import part1.common.service.UserService;
//import part1.common.pojo.User;
//
///**
// * @ClassName RPCClient
// * @Description 客户端建立socketa连接，传输Id给服务端，得到返回的User对象
// * @Author 氟西汀
// * @Date 2024/6/25 8:48
// * @Version 1.0
// */
//
//public class RPCClientbannder {
//
//    public static void main(String[] args) {
//        Socket socket = new Socket("127.0.0.1",8899);
//        ClientProxy clientProxy = new ClientProxy();
//        UserService proxy = clientProxy.getProxy(UserService.class);
////                调用方法一
//        User userByUsrId = proxy.getUserByUsrId(10);
//        User user = User.builder().userName("张三").sex(true).id(100).build();
//        Integer integer = proxy.insertUserId(user);
//        System.out.println("向服务器插入数据:"+integer);
////            try {
////           建立Socket连接
////
////            建立输出流与输入流
////                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
////            获得输入流
////                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
////            传给服务器Id
////                objectOutputStream.writeInt(new Random().nextInt());
////                objectOutputStream.flush();
////            服务器查询数据，返回对应的对象
////                User user = (User)objectInputStream.readObject();
////                System.out.println("服务器返回的Useer:"+user);
////
////
////            } catch (IOException | ClassNotFoundException e) {
////                e.printStackTrace();
////                System.out.println("客户端启动失败");
//
//
//        }
//    }
//
