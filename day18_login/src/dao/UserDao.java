package dao;

import domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2020/2/2.
 */
public interface UserDao {


    public List<User> findAll();

    User findUserByUsernameAndPassword(String username,String password);

    void add(User user);

    void del(int id);

    User findById(int i);

    void updateUser(User uesr);


    int findTotalCount(Map<String, String[]> condition);

    List<User> findByPage(int start, int rows, Map<String, String[]> condition);
}
