package com.fool.community.mapper;

import java.util.List;

import com.fool.community.entity.Notification;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier,receiver,type,gmt_create,status,article_id) values (#{notifier},#{receiver},#{type},#{gmtCreate},#{status},#{articleId})")
    void insert(Notification notification);

    @Select("select count(1) from notification where receiver = #{userId} and article_status='1'")
    int countByUserId(@Param(value="userId")int userId);

    @Select("select * from notification where receiver = #{userId} and article_status='1' order by gmt_create desc limit #{offset},#{size}")
    List<Notification> listByUserId(@Param(value = "userId") int userId, @Param(value = "offset") int offset, @Param(value = "size") int size);

    @Select("select * from notification where id = #{id}")
    Notification findByNotificationId(@Param(value = "id")Long id);

    @Update("update notification set status=#{status} where id=#{id}")
    void updateNotificationByNotification(Notification notification);

    @Select("select count(1) from notification where receiver = #{userId} and status ='0'")
    int countUnReadByUserId(@Param(value = "userId") int userId);

    @Update("update notification set article_status='0' where article_id=#{id}")
    void modifyArticleStatusByArticleId(int id);
}