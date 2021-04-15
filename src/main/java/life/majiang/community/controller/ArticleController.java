package life.majiang.community.controller;

import org.springframework.stereotype.Controller;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private QuestionService questionService;

    //@Autowired
   // private CommentService commentService;

    @GetMapping("/article/{id}")
    public String article(@PathVariable(name = "id") int id, Model model) {
        /*Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }*/
        QuestionDTO questionDTO = questionService.getById(id);
       /* List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);*/
       //增加阅读数
        questionService.updateViewById(id);
        model.addAttribute("question", questionDTO);
        /*model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);*/
        return "article";
    }

}
