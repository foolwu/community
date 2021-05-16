package com.fool.community.service;

import com.fool.community.mapper.NavMapper;
import com.fool.community.entity.Nav;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NavService {
    @Autowired
    private NavMapper navMapper;

    public List<Nav> list() {
       /* NavExample navExample = new NavExample();
        navExample.createCriteria()
                .andStatusEqualTo(1);
        navExample.setOrderByClause("priority desc");
        List<Nav> navs = navMapper.selectByExample(navExample);
        return navs;*/
       return null;
    }
}
