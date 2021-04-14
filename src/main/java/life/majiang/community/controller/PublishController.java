package life.majiang.community.controller;

import life.majiang.community.Entity.Question;
import life.majiang.community.Entity.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    //get就去渲染页面
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    //post就执行请求
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value="title") String title,
                            @RequestParam(value="description") String description,
                            @RequestParam(value="tag") String tag,
                            HttpServletRequest request,
                            Model model){
        //将信息放到model里，用来将已经填好的信息回显到页面
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //判断填的内容是否完整，不完整进行提示
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        //验证用户是否登录
        User user=null;
        Cookie[] cookies=request.getCookies();
        //当cookies不为空的时候才进行取值操作
        if(cookies!=null&&cookies.length!=0)
        //读取token
        for (Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();
                //通过token查询用户是否存在
                user=userMapper.findByToken(token);
                //如果用户存在，就绑定到session上
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        //如果用户未登录，就把提示信息返回到前端去
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        //将文章填充到question对象，并且插入数据库
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
