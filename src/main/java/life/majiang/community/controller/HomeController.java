package life.majiang.community.controller;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.entity.User;
//import life.majiang.community.service.NotificationService;
import life.majiang.community.service.NotificationService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/home/{action}")
    public String home(HttpServletRequest request,
                       @PathVariable(name = "action") String action,
                       Model model,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        long unReadCount=notificationService.countUnReadByUserId(user.getId());
        //如果action等于articles，就指向articles页面
        if ("articles".equals(action)) {
            model.addAttribute("section", "articles");
            model.addAttribute("sectionName", "我的文章");
            PageDTO pageDTO = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("pageDTO", pageDTO);
        } else if ("message".equals(action)) {
            model.addAttribute("section", "message");
            model.addAttribute("sectionName", "最新消息");
            PageDTO pageDTO = notificationService.listByUserId(user.getId(), page, size);
             model.addAttribute("pageDTO", pageDTO);
        }
//        model.addAttribute("unReadCount",unReadCount);
        return "home";
    }
}

