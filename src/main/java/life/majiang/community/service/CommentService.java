package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.*;
import life.majiang.community.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    //插入评论
    //开启事务的注解，表示一起成功或者失败，比如下面的插入评论和增加文章评论数，出现问题或者异常会回滚，AOP原理
    @Transactional
    public void insert(Comment comment) {
        //如果其父id找不到，说明父文章或者评论不见了，抛出异常提示
        //int不能判断空，转为Long去判断
        if (new Long((long)comment.getParentId()) == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //如果评论的类型不存在，即其类型不是文章页不是评论，抛出异常提示
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断是回复评论还是 回复文章
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            //当前回复的评论不存在抛出异常提示
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 插入评论
            commentMapper.insert(comment);
            // 增加评论数
            commentMapper.increaseCommentCountByComment(dbComment);
            // 创建通知
            //如果回复的是评论，显然通知的接收者应该是评论的创建人，所有传入该评论的创建者id
            //同时回复的只能是一级评论，所以该评论的父id一定是文章，不可能是二级评论
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);
        } else {
            // 回复文章
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            //如果文章不存在
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setType(2);
            //插入文章评论
            commentMapper.insert(comment);
            //将文章的评论数加1
            questionExtMapper.incCommentCount(question);

            // 创建通知
            //回复的是文章，那么接收者应该是文章的创建者，其父id应该是文章id
            createNotify(comment, question.getCreator(), NotificationTypeEnum.REPLY_ARTICLE);
        }
    }

    private void createNotify(Comment comment, int receiver,  NotificationTypeEnum notificationType) {
        //如果自己评论自己，那就不用通知
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        //评论创建时间
        notification.setGmtCreate(System.currentTimeMillis());
        //评论的类别
        notification.setType(notificationType.getType());
        //评论的创建人
        notification.setNotifier(comment.getCommentator());
        //评论关联的文章
        notification.setArticleId(comment.getParentId());
        //通知的状态
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        //接收者
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByQuestionId(int id) {
        //通过文章id找到所有回复
        List<Comment> comments=commentMapper.getById(id);
        //创建要给CommentDTO的list
        List<CommentDTO> lists=new ArrayList<>();
        //遍历每个Comment
        for(Comment comment:comments){
            //找到回复人
            User user=userMapper.findById(comment.getCommentator());
            CommentDTO commentDTO=new CommentDTO();
            //将第一个元素复制到第二个元素中
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            lists.add(commentDTO);
        }
        return lists;
}

    public List<CommentDTO> listCommentByParentId(Long id) {
        //通过父级评论id找到下面的所有回复
        List<Comment> comments=commentMapper.getCommentByParentId(id);
        //创建要给CommentDTO的list
        List<CommentDTO> lists=new ArrayList<>();
        //遍历每个Comment
        for(Comment comment:comments){
            //找到回复人
            User user=userMapper.findById(comment.getCommentator());
            CommentDTO commentDTO=new CommentDTO();
            //将第一个元素复制到第二个元素中
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            lists.add(commentDTO);
        }
        return lists;

    }




}
