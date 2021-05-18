package com.fool.community.controller;

import com.fool.community.dto.PageDTO;
import com.fool.community.entity.User;
//import NotificationService;
import com.fool.community.service.NotificationService;
import com.fool.community.service.ArticleService;
import com.fool.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

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
            PageDTO pageDTO = articleService.listByUserId(user.getId(), page, size);
            model.addAttribute("pageDTO", pageDTO);
        } else if ("message".equals(action)) {
            model.addAttribute("section", "message");
            model.addAttribute("sectionName", "最新消息");
            PageDTO pageDTO = notificationService.listByUserId(user.getId(), page, size);
             model.addAttribute("pageDTO", pageDTO);
        } else if("profile".equals(action)){
            model.addAttribute("section","profile");
            model.addAttribute("sectionName","我的资料");
        }
//        model.addAttribute("unReadCount",unReadCount);
        return "home";
    }

    @GetMapping("/home/articles/{id}")
    public String delete(HttpServletRequest request,
                         @PathVariable(name="id") int id,
                         Model model,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        //删除文章
        articleService.deleteArticleById(id);
        //显示删除文章后的新列表
        model.addAttribute("section", "articles");
        model.addAttribute("sectionName", "我的文章");
        PageDTO pageDTO = articleService.listByUserId(user.getId(), page, size);
        model.addAttribute("pageDTO", pageDTO);
        return "home";
    }

    @PostMapping("/modifyProfile")
    public String modifyProfile(HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        //获得当前用户
        User user = (User) request.getSession().getAttribute("user");

        //接收数据
        String username= request.getParameter("newName");
        String personal =request.getParameter("personal");
        String avatar=request.getParameter("avatarUrl");
        user.setName(StringUtils.isBlank(username)?user.getName():username);
        user.setBio(StringUtils.isBlank(personal)?user.getBio():personal);
        user.setAvatarUrl(StringUtils.isBlank(avatar)?user.getAvatarUrl():avatar);
        user.setGmtModified(System.currentTimeMillis());
        //更新数据
        userService.updateUserByUser(user);
        //将更新数据后的新数据查出来再给前端
        User newUser=userService.findUser(user);
        request.getSession().setAttribute("user",newUser);
        model.addAttribute("section","profile");
        model.addAttribute("sectionName","我的资料");
        return "home";
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        //获得当前用户
        User user = (User) request.getSession().getAttribute("user");

        //接收数据
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        //两次密码不相等，返回提示，前端已经验证过，再验证一次
        if (password1.equals(password2)==false){
            model.addAttribute("error","两次密码不一致");
        }else{
            //填充数据
            user.setGmtModified(System.currentTimeMillis());
            user.setPassword(password1);
            //更新数据
            userService.updateUserByUser(user);
        }
        //跳转登录页
        return "redirect:/login";
    }


}

