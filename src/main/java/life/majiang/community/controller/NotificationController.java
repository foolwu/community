package life.majiang.community.controller;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.entity.User;
import life.majiang.community.service.NotificationService;
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
        //判断是不是跳转到文章
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_ARTICLE.getType() == notificationDTO.getType()) {
            return "redirect:/article/" + notificationDTO.getArticleId();
        } else {
            return "redirect:/";
        }
    }
}
