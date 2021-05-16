package com.fool.community.mapper;


import com.fool.community.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ArticleExtMapper {
    int incView(Article record);

    @Update("update article set comment_count=comment_count+1 where id = #{id}")
    void incCommentCount(Article article);

    List<Article> selectRelated(Article article);

//    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

//    List<Article> selectBySearch(QuestionQueryDTO questionQueryDTO);

    List<Article> selectSticky();
}