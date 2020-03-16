package dao.impl;

import dao.UserDao;
import domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by asus on 2020/2/2.
 *UserDao的实现类。这种命名方式是常用的写法
 * dao层直接使用domain的接口和JDBC连接数据库，获取到user并返回给调用者
 */

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());//借助工具类获取连接池对象
    @Override
    public List<User> findAll() {
        //使用JDBC操作数据库
        //使用sql语句
        String sql="select * from user";
        List<User> users=template.query(sql,new BeanPropertyRowMapper<User>(User.class));
        return users;
    }

    public User findUserByUsernameAndPassword(String username,String password)
    {
      try {
         String sql="select * from user where username = ? and password = ?";
          User user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
          return user;
      }catch (Exception e)
      {
          e.printStackTrace();
          return null;
      }

    }

    public void add(User user) {
        try {
            String sql="insert into user values(null,?,?,?,?,?,?,null,null)";
            template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void del(int id)
    {
        try{
            String sql="delete from user where id = ?";
            template.update(sql,id);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public User findById(int id)
    {
        try{
            String sql="delete from user where id = ?";

            return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),id);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
         return null;
    }

    public void updateUser(User user)
    {
        String sql="update user set name = ?,gender = ?, age = ?, address = ?, qq = ?, email = ? where id = ?";
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail(),user.getId());
    }


    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        //定义初始化sql。自行加一个where 1=1 省去这部分
        String sql="select count(*) from user where 1 = 1 ";
        StringBuilder sb=new StringBuilder(sql);
        //遍历condition map
        Set<String> keySet=condition.keySet();
        //定义参数的集合
        List<Object> params=new ArrayList<Object>();
        for(String key:keySet)
        {
            //排除分页条件的两个参数currentPage 和rows
            if("currentPage".equals(key)||"rows".equals(key))
            {
                continue;
            }
            //获取map 的value值
            String value=condition.get(key)[0];
            //判断value是否有值
            if(value!=null && !"".equals(value))
            {
                //有值，把其作为条件加进去
                sb.append(" and "+key+" like ?");
                params.add("%"+value+"%");//加条件值

            }
        }


        return template.queryForObject(sb.toString(),Integer.class,params.toArray());//注意这里sql要换成sb.第二个参数为返回值类型
    }

    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
        String sql = "select * from user  where 1 = 1 ";

        StringBuilder sb = new StringBuilder(sql);
        //2.遍历map
        Set<String> keySet = condition.keySet();
        //定义参数的集合
        List<Object> params = new ArrayList<Object>();
        for (String key : keySet) {

            //排除分页条件参数
            if("currentPage".equals(key) || "rows".equals(key)){
                continue;
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if(value != null && !"".equals(value)){
                //有值
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//？条件的值
            }
        }

        //添加分页查询
        sb.append(" limit ?,? ");
        //添加分页查询参数值
        params.add(start);
        params.add(rows);
        sql = sb.toString();
        System.out.println(sql);
        System.out.println(params);

        return template.query(sql,new BeanPropertyRowMapper<User>(User.class),params.toArray());
    }


}
