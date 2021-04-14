package life.majiang.community.controller;

import life.majiang.community.Entity.Question;
import life.majiang.community.Entity.User;
import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    //表示什么都不输入的时候默认访问index
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size) {
        //page表示每页的页码，size表示每页展示的文章条数
        //得到cookies
        Cookie[] cookies = request.getCookies();
        //当cookies不为空的时候才进行取值操作，否则不做操作
        if (cookies != null && cookies.length != 0)
            //读取token
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //通过token查询用户是否存在
                    User user = userMapper.findByToken(token);
                    //如果用户不为空，再次设置session
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                }
                break;
            }

        PageDTO pageDTO = questionService.List(page,size);
        model.addAttribute("pageDTO", pageDTO);
        return "index";
    }
}
