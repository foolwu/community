package com.fool.community.mapper;

import com.fool.community.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("insert into article (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Article article);

   /* @Select("select * from question limit #{offset},#{size}")
    List<Article> List(int offset, int size);*/

    @Select("select * from article where status='1' order by id desc limit #{offset},#{size}")
    List<Article> list(@Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from article where status='1' and title like  CONCAT('%',#{search},'%') or description like CONCAT('%',#{search},'%') order by id desc limit #{offset},#{size}")
    List<Article> listBySearch(@Param(value = "search")String search, @Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from article where status='1' and tag like CONCAT('%',#{tag},'%') order by id desc limit #{offset},#{size}")
    List<Article> listByTag(@Param(value = "tag")String tag, int offset, int size);

    @Select("select count(1) from article where status='1'")
    int count();

    @Select("select * from article where creator = #{userId} and status='1' order by gmt_modified limit #{offset},#{size}")
    List<Article> listByUserId(@Param(value = "userId") int userId, @Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select count(1) from article where creator=#{userId} and status='1'")
    int countByUserId(@Param(value = "userId") int userId);

    @Select("select * from article where id = #{id}")
    Article getById(@Param(value = "id")int  id);

    @Update("update article set title=#{title},description=#{description}, gmt_modified=#{gmtModified},tag=#{tag} where id = #{id}")
    void update(Article article);

    @Update("update article set view_count=view_count+1 where id = #{id}")
    void updateViewById(@Param(value = "id")int  id);

    @Select("select * from article where id = #{parentId}")
    Article selectByPrimaryKey(@Param(value = "parentId")int parentId);

    @Select("select count(1) from article where status='1' and title like  CONCAT('%',#{search},'%') or description like CONCAT('%',#{search},'%')")
    int countBySearch(@Param(value = "search")String search);

    @Select("select count(1) from article where status='1' and tag like CONCAT('%',#{tag},'%')")
    int countByTag(@Param(value = "tag")String tag);

    @Select("select * from article where tag like CONCAT('%',#{tag},'%')")
    List<Article> findRelatedArticle(Article article);

    @Select("select * from article where tag like CONCAT('%',#{tag},'%')")
    List<Article> findRelatedTag(@Param(value = "tag")String tag);

    @Select("select * from article where id=#{articleId}")
    Article findById(int articleId);

    @Select("select creator from article where id=#{id}")
    int getCreatorIdById(int id);

    @Select("select * from article where status='1' order by view_count desc limit 0,10")
    List<Article> getTopTenArticle();

    @Update("update article set status='0' where id=#{id}")
    void deleteArticleByArticleId(int id);

    @Update("update article set comment_count=comment_count+1 where id = #{id}")
    void increaseCommentNumber(Article article);

    @Update("update article set like_count=like_count+1 where id = #{id}")
    void addLikeNumber(Article article);
}
