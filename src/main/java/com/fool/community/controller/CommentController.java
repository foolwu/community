package com.fool.community.controller;

import com.fool.community.dto.CommentCreateDTO;
import com.fool.community.dto.CommentDTO;
import com.fool.community.dto.ResultDTO;
import com.fool.community.entity.Comment;
import com.fool.community.entity.User;
import com.fool.community.exception.CustomizeErrorCode;
import com.fool.community.mapper.CommentMapper;
import com.fool.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    //插入评论
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //如果user为空，提示未登录
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,commentCreateDTO);
        //返回登录成功
        return ResultDTO.okOf();
    }

    //显示二级评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        //这里得到了父级评论的id，根据这个id去得到它下面的二级评论
       List<CommentDTO> commentDTOS = commentService.listCommentByParentId(id);
       //将list通过ResultDTO的okOf()传到前端
        return ResultDTO.okOf(commentDTOS);
    }

}
