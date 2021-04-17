package life.majiang.community.controller;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    //表示什么都不输入的时候默认访问index
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        //page表示每页的页码，size表示每页展示的文章条数

        PageDTO pageDTO = questionService.list(page, size,search,tag);
        model.addAttribute("pageDTO", pageDTO);
        return "index";
    }
}
