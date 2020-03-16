package service;

import domain.PageBean;
import domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2020/2/2.
 * 用户管理的业务接口
 */
public interface UserService {

    public List<User> findAll();

    public User login(User user) ;

    void add(User user);

    void del(int id);

    User findById(String id);

    void updateUser(User user);

   void delSelectedUser(String[] ids);

    PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> condition);
}
