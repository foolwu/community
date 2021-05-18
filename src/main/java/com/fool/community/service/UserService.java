package com.fool.community.service;

import com.fool.community.entity.User;
import com.fool.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //针对GitHub账号登录的处理方法
    public void createOrUpdate(User user) {
        User dbUser=userMapper.findByAccountId(user.getAccountId());
        if(dbUser==null){
            //插入新用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setStatus(1);
            userMapper.insert(user);
        }else{
            //老用户，更新token、头像、名字
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            dbUser.setStatus(1);
            userMapper.update(dbUser);
        }
    }

    public User findUser(User user) {
        User newUser=userMapper.findUser(user);
        return newUser;
    }

    public void updateUserByUser(User user) {
        userMapper.updateUserByUser(user);
    }

}
