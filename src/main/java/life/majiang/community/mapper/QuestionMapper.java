package life.majiang.community.mapper;

import life.majiang.community.entity.Question;
import life.majiang.community.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

   /* @Select("select * from question limit #{offset},#{size}")
    List<Question> List(int offset, int size);*/

    @Select("select * from question order by id desc limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from question where title like  CONCAT('%',#{search},'%') or description like CONCAT('%',#{search},'%') order by id desc limit #{offset},#{size}")
    List<Question> listBySearch(@Param(value = "search")String search,@Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from question where tag like CONCAT('%',#{tag},'%') order by id desc limit #{offset},#{size}")
    List<Question> listByTag(@Param(value = "tag")String tag, int offset, int size);

    @Select("select count(1) from question")
    int count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") int userId,@Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select count(1) from question where creator=#{userId}")
    int countByUserId(@Param(value = "userId") int userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id")int  id);

    @Update("update question set title=#{title},description=#{description}, gmt_modified=#{gmtModified},tag=#{tag} where id = #{id}")
    void update(Question question);

    @Update("update question set view_count=view_count+1 where id = #{id}")
    void updateViewById(@Param(value = "id")int  id);

    @Select("select * from question where id = #{parentId}")
    Question selectByPrimaryKey(@Param(value = "parentId")int parentId);

    @Select("select count(1) from question where title like  CONCAT('%',#{search},'%') or description like CONCAT('%',#{search},'%')")
    int countBySearch(@Param(value = "search")String search);

    @Select("select count(1) from question where tag like CONCAT('%',#{tag},'%')")
    int countByTag(@Param(value = "tag")String tag);

    @Select("select * from question where tag like CONCAT('%',#{tag},'%')")
    List<Question> findRelatedArticle(Question question);

    @Select("select * from question where tag like CONCAT('%',#{tag},'%')")
    List<Question> findRelatedTag(@Param(value = "tag")String tag);

    @Select("select * from question where id=#{articleId}")
    Question findById(int articleId);

    @Select("select creator from question where id=#{id}")
    int getCreatorIdById(int id);
}
