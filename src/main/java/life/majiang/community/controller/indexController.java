package life.majiang.community.controller;

import life.majiang.community.Entity.User;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;
    //表示什么都不输入的时候默认访问index
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        //读取token
        for (Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();
                //通过token查询用户是否存在
                User user=userMapper.findByToken(token);
                //如果用户不为空，再次设置session
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
            }
            break;
        }
        return "index";
    }
}
