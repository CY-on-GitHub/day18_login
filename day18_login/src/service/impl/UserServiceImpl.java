package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.PageBean;
import domain.User;
import service.UserService;

import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2020/2/2.
 */
public class UserServiceImpl implements UserService {

    private UserDao dao=new UserDaoImpl();//创建一个UserDao实例对象

    //复写接口的方法
    @Override
    public List<User> findAll() {
        //调用Dao查询
        return dao.findAll();
    }
    @Override
    public User login(User user)
    {
        return  dao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    public void add(User user)
    {
        dao.add(user);
    }

    public void del(int id)
    {
        dao.del(id);
    }

    public User findById(String id)
    {
        return dao.findById(Integer.parseInt(id));
    }

    public void updateUser(User user)
    {
        dao.updateUser(user);
    }

    public void delSelectedUser(String[] ids)
    {
        for(String id:ids)
        {
            dao.del(Integer.getInteger(id));
        }
    }
    //分页查询需要的三个参数
    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition) {
        int currentPage=Integer.parseInt(_currentPage);
        int rows=Integer.parseInt(_rows);
        //创建pageBean对象
        PageBean pb=new PageBean<User>();
        //设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);
        //调用dao查询总记录数
        int totalCount=dao.findTotalCount(condition);
        pb.setTotalCount(totalCount);
        //查询list集合
        int start=(currentPage-1)*rows;
        List<User> list=dao.findByPage(start,rows,condition);
        pb.setList(list);
        //计算总共的页码
        int totalPage=(totalCount%rows)==0?totalCount/rows:totalCount/rows+1;
        pb.setTotalPage(totalPage);

        return pb;
    }
}
