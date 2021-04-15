package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.entity.Question;
import life.majiang.community.entity.User;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;


    //get就去渲染页面
    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //post就执行请求
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title") String title,
                            @RequestParam(value = "description") String description,
                            @RequestParam(value = "tag") String tag,
                            @RequestParam(value = "id") Integer id,
                            HttpServletRequest request,
                            Model model) {
        //将信息放到model里，用来将已经填好的信息回显到页面
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("id",id);
        //判断填的内容是否完整，不完整进行提示
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        //如果用户未登录，就把提示信息返回到前端去
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        //将文章填充到question对象，并且插入数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    //编辑跳转
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Integer id,Model model){
        QuestionDTO question=questionService.getById(id);

        //将信息放到model里，用来将已经填好的信息回显到页面
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTitle());
        model.addAttribute("id", question.getId());
        return "publish";
    }
}
