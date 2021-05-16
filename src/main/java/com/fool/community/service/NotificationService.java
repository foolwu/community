package com.fool.community.service;

import com.fool.community.dto.NotificationDTO;
import com.fool.community.dto.PageDTO;
import com.fool.community.entity.Article;
import com.fool.community.entity.Notification;
import com.fool.community.entity.User;
import com.fool.community.enums.NotificationStatusEnum;
import com.fool.community.enums.NotificationTypeEnum;
import com.fool.community.mapper.NotificationMapper;
import com.fool.community.mapper.ArticleMapper;
import com.fool.community.mapper.UserMapper;
import com.fool.community.exception.CustomizeErrorCode;
import com.fool.community.exception.CustomizeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;

    public PageDTO listByUserId(int userId, int page, int size) {

        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();

        //消息总数
        int totalCount=notificationMapper.countByUserId(userId);
        pageDTO.setPagination(totalCount, page, size);
        //将非法页数修正，例如-1，或者大于总数页的页数
        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }
        int offset = size * (page - 1);

        //通过id去找到发通知的的消息的相关信息，我们就得到了创建者id,文章id,创建时间等
        List<Notification> notifications = notificationMapper.listByUserId(userId,offset, size);

        List<NotificationDTO> notificationDTOList = new ArrayList<>();


        //我需要什么,消息的创建人（通过创建者id拿到），文章的名字（通过文章的id拿到），消息的种类（通过其type判断）
        for (Notification notification : notifications) {
            //通过消息里的创建者id得到创建者的
            User user = userMapper.findById(notification.getNotifier());
            Article article = articleMapper.findById(notification.getArticleId());
            NotificationDTO notificationDTO = new NotificationDTO();
            //利用spring内置的工具类将notification的属性复制给notificationDTO
            BeanUtils.copyProperties(notification, notificationDTO);
            //将创建者对象放进去
            notificationDTO.setUser(user);
            //将文章对象放进去
            notificationDTO.setArticle(article);
            //将通知放到通知集合里
            notificationDTOList.add(notificationDTO);
        }
        //pageDTO里得到的数据是分页相关数据以及其属性容器得到的消息对象集合
        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }

    public int countUnReadByUserId(int userId) {
        return notificationMapper.countUnReadByUserId(userId);

    }

    public NotificationDTO read(Long id, User user) {
        //先查询这个评论是否存在
        Notification notification = notificationMapper.findByNotificationId(id);
        //不存在直接进行异常提示
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //判断消息的接受人是否是当前页面用户，不是的话直接进行异常提示
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //将该消息的状态设置为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        //到数据库修改状态
        notificationMapper.updateNotificationByNotification(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
