package com.fool.community.mapper;

import com.fool.community.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url,bio,status) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl},#{bio},#{status})")
    void insert(User user);

    @Select("select * from user where token= #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id= #{id}")
    User findById(@Param("id") int id);

    @Select("select * from user where account_id= #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl},status=#{status} where account_id = #{accountId}")
    void update(User dbUser);

    @Select("select * from user where email= #{email} and password= #{password}")
    User findUser(User user);

    @Insert("insert into user (name,token,gmt_create,avatar_url,email,password) values (#{name},#{token},#{gmtCreate},#{avatarUrl},#{email},#{password})")
    void insertNewUser(User user);

    @Select("select *  from user where email=#{email}")
    User findUserByEmail(User user);

    @Update("update user set status='0' where email=#{email}")
    void changeStatusByUser(User user);

    @Update("update user set token=#{token},status='1' where id=#{id}")
    void setTokenAndStatusByUser(User user);

    @Update("update user set name=#{name},bio=#{bio},gmt_modified=#{gmtModified},password=#{password},avatar_url=#{avatarUrl} where id=#{id}")
    void updateUserByUser(User user);
}
