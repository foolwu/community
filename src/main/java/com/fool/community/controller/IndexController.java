package com.fool.community.controller;

import com.fool.community.dto.PageDTO;
import com.fool.community.entity.Article;
import com.fool.community.mapper.UserMapper;
import com.fool.community.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ArticleService articleService;

    //表示什么都不输入的时候默认访问index
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        //page表示每页的页码，size表示每页展示的文章条数
        // search表示根据搜索显示文章tag表示根据tag显示文章
        PageDTO pageDTO = articleService.list(page, size,search,tag);
        model.addAttribute("pageDTO", pageDTO);

        //获取阅读量最高的十篇文章
        List<Article> articles= articleService.getTopTenArticle();
        model.addAttribute("toparticle",articles);

        return "index";
    }
}
