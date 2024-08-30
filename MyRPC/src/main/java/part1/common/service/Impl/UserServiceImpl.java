package part1.common.service.Impl;

import part1.common.service.UserService;
import part1.common.pojo.User;

import java.util.Random;
import java.util.UUID;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/21 22:06
 * @Version 1.0
 */

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUsrId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户");
//      模拟从数据库中取用户的行为
        Random random = new Random();
        User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return null;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功:"+user);
        return 1;
    }
}
