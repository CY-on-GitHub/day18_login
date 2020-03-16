package web.servlet;

import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by asus on 2020/2/8.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置编码
        request.setCharacterEncoding("utf-8");
        //2 获取数据
        //获取用户填写的验证码
        String verifyCode=request.getParameter("verifycode");
        //验证码校验
        HttpSession session=request.getSession();
        String checkCode_server=(String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute(checkCode_server);//验证码只用一次
        if(!checkCode_server.equalsIgnoreCase(verifyCode))
        {
          //验证码错误
            request.setAttribute("login_msg","验证码错误");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
            return;//回到登录页面
        }

        //封装user对象
        Map<String,String[]> map=request.getParameterMap();
        User user=new User();
        try{
            BeanUtils.populate(user,map);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        //调用service查询
        UserService service=new UserServiceImpl();
        User loginUser= service.login(user);
        if(loginUser!=null)
        {
            //登陆成功，将用户存入session
            session.setAttribute("user",loginUser);
            //跳转页面
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }
        else {
            //登录失败，提示信息
            request.setAttribute("login_msg","用户名或密码错误");//注意这里的变量名login_msg是与页面上的资源名对应的
            //重新跳转到登录页面
            request.getRequestDispatcher("/login.jsp").forward(request,response);


        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     this.doPost(request,response);
    }
}
