package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.entity.User;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.service.CommentService;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{id}")
    public String article(@PathVariable(name = "id") int id, Model model,HttpServletRequest request) {
        /*Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }*/
        User user = (User) request.getSession().getAttribute("user");
        //包含用户信息的问题对象
        QuestionDTO questionDTO = questionService.getById(id);
        //评论合集
        List<CommentDTO> comments = commentService.listByQuestionId(id);
        List<QuestionDTO> relatedArticle=questionService.findRelatedArticle(questionDTO);
       //增加阅读数
        questionService.updateViewById(id,user);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedArticle);
        return "article";
    }

}
