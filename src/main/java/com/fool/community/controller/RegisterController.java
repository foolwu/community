package com.fool.community.controller;

import com.fool.community.entity.User;
import com.fool.community.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//注册
@Controller
public class RegisterController {
    @Resource
    private UserMapper userMapper;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

   @PostMapping("/registercheck")
    public String registercheck(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username =request.getParameter("username");
        //随机生成一个token用来当cookies的value
        String token= UUID.randomUUID().toString();
        User user=new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setToken(token);
        user.setAccountId("1");
        user.setBio("暂无");
       user.setName(username);
       user.setAvatarUrl("../img/headpic.jpg");
       user.setGmtCreate(System.currentTimeMillis());
        //如果查找用户不为null，则用户存在，重新注册，否则写入数据
        //如果数据库中有重复用户网页会出错
        if(userMapper.findUserByEmail(user)!=null){
          //注册失败，返回注册页
            return "redirect:/register";
        }else {
        	//注册成功，则把用户信息写进cookies，直接跳转到主页
        	userMapper.insertNewUser(user);
        	response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }
    }
}
