package life.majiang.community.mapper;

import java.util.List;

import life.majiang.community.entity.Comment;
import life.majiang.community.entity.Notification;
import life.majiang.community.entity.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier,receiver,type,gmt_create,status,article_id) values (#{notifier},#{receiver},#{type},#{gmtCreate},#{status},#{articleId})")
    void insert(Notification notification);

    @Select("select count(1) from notification where receiver = #{userId}")
    int countByUserId(@Param(value="userId")int userId);

//    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
//    List<Question> listByUserId(@Param(value = "userId") int userId, @Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from notification where receiver = #{userId} limit #{offset},#{size}")
    List<Notification> listByUserId(@Param(value = "userId") int userId, @Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from notification where id = #{id}")
    Notification findByNotificationId(@Param(value = "id")Long id);

    @Update("update notification set status=#{status} where id=#{id}")
    void updateNotificationByNotification(Notification notification);
    @Select("select count(1) from notification where receiver = #{userId} and status ='0'")
    int countUnReadByUserId(@Param(value = "userId") int userId);
}