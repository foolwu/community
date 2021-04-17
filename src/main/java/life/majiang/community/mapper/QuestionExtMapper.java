package life.majiang.community.mapper;


import life.majiang.community.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface QuestionExtMapper {
    int incView(Question record);

    @Update("update question set comment_count=comment_count+1 where id = #{id}")
    void incCommentCount(Question question);

    List<Question> selectRelated(Question question);

//    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

//    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectSticky();
}