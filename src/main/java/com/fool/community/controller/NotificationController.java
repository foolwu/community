package com.fool.community.controller;

import com.fool.community.dto.NotificationDTO;
import com.fool.community.enums.NotificationTypeEnum;
import com.fool.community.entity.User;
import com.fool.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


//当点击通知后，将消息的状态改为已读
@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        //用户未登录直接跳转首页
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        //将消息状态该为已读
        NotificationDTO notificationDTO = notificationService.read(id, user);
        long unReadCount=notificationService.countUnReadByUserId(user.getId());
        request.getSession().setAttribute("unReadCount",unReadCount);
        //判断是不是跳转到文章
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_ARTICLE.getType() == notificationDTO.getType()) {
            return "redirect:/article/" + notificationDTO.getArticleId();
        } else {
            return "redirect:/";
        }
    }
}
