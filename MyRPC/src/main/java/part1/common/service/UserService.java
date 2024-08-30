package part1.common.service;

import part1.common.pojo.User;

public interface UserService {

    User getUserByUsrId(Integer id);
//    新增一个功能
    Integer insertUserId(User user);
}
