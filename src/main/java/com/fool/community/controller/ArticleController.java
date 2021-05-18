package com.fool.community.controller;

import com.fool.community.dto.ArticleDTO;
import com.fool.community.dto.CommentDTO;
import com.fool.community.entity.Article;
import com.fool.community.entity.User;
import com.fool.community.exception.CustomizeErrorCode;
import com.fool.community.exception.CustomizeException;
import com.fool.community.mapper.ArticleMapper;
import com.fool.community.service.CommentService;
import org.springframework.stereotype.Controller;
import com.fool.community.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/article/{id}")
    public String article(@PathVariable(name = "id") int id, Model model,HttpServletRequest request) {
        Article article = articleMapper.getById(id);
        //错误处理
        //如果查询的文章不存在，直接跳到异常,提示查看的文章不存在
        if (article ==null){
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        User user = (User) request.getSession().getAttribute("user");
        //包含用户信息的问题对象
        ArticleDTO articleDTO = articleService.getById(id);
        //评论合集
        List<CommentDTO> comments = commentService.listByArticleId(id);
        List<ArticleDTO> relatedArticle= articleService.findRelatedArticle(articleDTO);
       //增加阅读数
        articleService.updateViewById(id,user);
        model.addAttribute("article", articleDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedArticle);
        return "article";
    }

}
