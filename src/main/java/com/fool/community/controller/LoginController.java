package com.fool.community.controller;

import com.fool.community.entity.User;
import com.fool.community.mapper.UserMapper;
import com.fool.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//登陆
@Controller
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logincheck")
    public String checklogin(HttpServletRequest request, HttpServletResponse response) {
        //通过request获取输入的用户名和密码在数据库中查找相关用户，如果存在就登录成功
        User user = new User();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        user.setEmail(email);
        user.setPassword(password);
        User newUser = userService.findUser(user);
        if (newUser != null) {
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setStatus(1);
            userMapper.setTokenAndStatusByUser(user);
            response.addCookie(new Cookie("token", token));
            request.getSession().setAttribute("user",user);
        } else {
            //登陆失败，重新登陆
        	return "redirect:/login";
        }
        request.getSession().setAttribute("user",newUser);
        return "redirect:/";
    }

    //退出登陆
   /* @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }*/
}
