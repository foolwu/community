package com.fool.community.controller;

import com.fool.community.dto.AccessTokenDTO;
import com.fool.community.dto.GithubUser;
import com.fool.community.entity.User;
import com.fool.community.mapper.UserMapper;
import com.fool.community.provider.GithubProvider;
import com.fool.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //@Value的作用是配置文件里读取相应名字的值并赋给当前变量
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            //使用github验证成功以后，就登录，填充用户对象
            User user = new User();
            //随机生成token
            String token = UUID.randomUUID().toString();
            //填充user对象
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setBio(String.valueOf(githubUser.getBio()));
            userService.createOrUpdate(user);
            //将token写入cookie
            response.addCookie(new Cookie("token", token));
            //登录成功，写kookie和session
            request.getSession().setAttribute("user", githubUser);
            //重定向，如果不写redrect的话地址栏会带上参数，然后渲染成index。写了会去掉参数重定向到index
            return "redirect:/";
        } else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        User user = (User) request.getSession().getAttribute("user");
        //移除session
        request.getSession().removeAttribute("user");
        //通过建立重名cookie移除cookies
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //同时将数据库里的在线状态改为0表示已经退出
        userMapper.changeStatusByUser(user);
        return "redirect:/";
    }

}
