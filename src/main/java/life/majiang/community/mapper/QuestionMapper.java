package life.majiang.community.mapper;

import life.majiang.community.Entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

   /* @Select("select * from question limit #{offset},#{size}")
    List<Question> List(int offset, int size);*/

    @Select("select * from question limit #{offset},#{size}")
    List<Question> List(@Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select count(1) from question")
    int count();

}
