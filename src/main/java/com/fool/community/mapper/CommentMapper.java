package com.fool.community.mapper;

import com.fool.community.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment selectByPrimaryKey(@Param(value = "parentId") int parentId);

    @Select("select * from comment where parent_id = #{id} and type='1' order by gmt_create desc")
    List<Comment> getCommentById(@Param(value = "id") int id);

    @Select("select * from comment where parent_id = #{id} and type='2' order by gmt_create desc")
    List<Comment> getCommentByParentId(Long id);

    @Update("update comment set comment_count=comment_count+1 where id = #{id}")
    void increaseCommentCountByComment(Comment dbComment);
}
