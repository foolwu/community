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

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") int offset, @Param(value = "size") int size);

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

}
