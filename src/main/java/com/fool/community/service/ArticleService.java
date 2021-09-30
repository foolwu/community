package com.fool.community.service;

import com.fool.community.dto.ArticleDTO;
import com.fool.community.dto.PageDTO;
import com.fool.community.entity.Article;
import com.fool.community.entity.User;
import com.fool.community.exception.CustomizeErrorCode;
import com.fool.community.exception.CustomizeException;
import com.fool.community.mapper.ArticleMapper;
import com.fool.community.mapper.NotificationMapper;
import com.fool.community.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    public PageDTO list(int page, int size, String search, String tag) {

        //文章总数
        int totalCount;
        //如果搜索参数不为空
        if(StringUtils.isNotBlank(search)){
            totalCount= articleMapper.countBySearch(search);
        }else if(StringUtils.isNotBlank(tag)){
            totalCount= articleMapper.countByTag(tag);
        }else {
            totalCount= articleMapper.count();
        }

        PageDTO pageDTO = new PageDTO();

        //先将数据输入，计算得到page的各项数据，例如总页数、是否有前一页后一页等
        pageDTO.setPagination(totalCount, page, size);
        //将非法页数修正，例如-1，或者大于总数页的页数
        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }
        //size*{page-1}，到数据库取数据
        int offset = size * (page - 1);
        //拿到Article对象
        List<Article> articles;
        if(StringUtils.isNotBlank(search)){
            articles = articleMapper.listBySearch(search,offset, size);
        }else if(StringUtils.isNotBlank(tag)){
            articles = articleMapper.listByTag(tag,offset, size);
        }else {
            articles = articleMapper.list(offset, size);
        }
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        //遍历Article对象，将User对象一起输入到ArticleDTO
        for (Article article : articles) {
            User user = userMapper.findById(article.getCreator());
            ArticleDTO articleDTO = new ArticleDTO();
            //利用spring内置的工具类将article的属性复制给articleDTO
            BeanUtils.copyProperties(article, articleDTO);
            articleDTO.setUser(user);
            articleDTOList.add(articleDTO);
        }
        //将articleDTO放到articleDTO对象，便于前端通过pageDTO拿到文章、页码等数据
        pageDTO.setData(articleDTOList);
        return pageDTO;
    }


    public PageDTO listByUserId(int userId,int page, int size) {

        PageDTO pageDTO = new PageDTO();

        //文章总数
        int totalCount = articleMapper.countByUserId(userId);

        //先将数据输入，计算得到page的各项数据，例如总页数、是否有前一页后一页等
        pageDTO.setPagination(totalCount, page, size);
        //将非法页数修正，例如-1，或者大于总数页的页数
        if (page < 1) {
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }
        if (page < 1) {
            page = 1;
        }
        //size*{page-1}，到数据库取数据
        int offset = size * (page - 1);
        //拿到article对象
        List<Article> articles = articleMapper.listByUserId(userId,offset, size);
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        //遍历article对象，将User对象一起输入到ArticleDTO
        for (Article article : articles) {
            User user = userMapper.findById(article.getCreator());
            ArticleDTO articleDTO = new ArticleDTO();
            //利用spring内置的工具类将article的属性复制给articleDTO
            BeanUtils.copyProperties(article, articleDTO);
            articleDTO.setUser(user);
            articleDTOList.add(articleDTO);
        }
        //将articleDTO放到pageDTO对象，便于前端通过pageDTO拿到文章、页码等数据
        pageDTO.setData(articleDTOList);
        return pageDTO;
    }

    public ArticleDTO getById(int id) {
        Article article = articleMapper.getById(id);
        //错误处理
        //如果查询的文章不存在，直接跳到异常并进行相应提示
        if (article ==null){
            throw new CustomizeException(CustomizeErrorCode.ARTICLE_NOT_FOUND);
        }
        ArticleDTO articleDTO =new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        User user=userMapper.findById(article.getCreator());
        articleDTO.setUser(user);
        return articleDTO;
    }

    public void createOrUpdate(Article article) {
        if (article.getId()==0){
            //插入新文章
            article.setGmtCreate(System.currentTimeMillis());
            article.setGmtModified(article.getGmtCreate());
            articleMapper.create(article);
        }else {
            //更新文章
            article.setGmtModified(System.currentTimeMillis());
            articleMapper.update(article);
        }
    }


    //更新阅读数
    public void updateViewById(int id,User user) {
        //如果用户自己看自己的文章，不增加阅读数
        if(user==null||user.getId()!= articleMapper.getCreatorIdById(id))
        {
            articleMapper.updateViewById(id);
        }
    }

    public List<ArticleDTO> findRelatedArticle(ArticleDTO articleDTO) {
        //如果该文章没有相关的标签，返回空List
        if (StringUtils.isBlank(articleDTO.getTag())) {
            return new ArrayList<>();
        }
        List<Article> articles =new ArrayList<>(10);
        //通过逗号将标签分割
        String[] tags = StringUtils.split(articleDTO.getTag(), ",");
        for (String tag:tags
             ) {
            //将不同的tag查询得到的question对象集合保存到list，可能有重复值
           articles.addAll(articleMapper.findRelatedTag(tag));

        }

        //拿到的对象可能有重复的数据，根据id去重
        List<Article> unique = articles.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Article::getId))),ArrayList::new));
        //将question赋值questionDTO
        List<ArticleDTO> articleDTOS = unique.stream().map(q -> {
            ArticleDTO articleDTO1 = new ArticleDTO();
            BeanUtils.copyProperties(q, articleDTO1);
            return articleDTO1;
        }).collect(Collectors.toList());

        return articleDTOS;

    }

    public List<Article> getTopTenArticle() {
        List<Article> articles=articleMapper.getTopTenArticle();
        return articles;
    }

    public void deleteArticleById(int id) {
        articleMapper.deleteArticleByArticleId(id);
        notificationMapper.modifyArticleStatusByArticleId(id);
    }

    public void addLikeNumber(Article article) {
        articleMapper.addLikeNumber(article);
    }
}
