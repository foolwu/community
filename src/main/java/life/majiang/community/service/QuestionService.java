package life.majiang.community.service;

import life.majiang.community.entity.Question;
import life.majiang.community.entity.User;
import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PageDTO list(int page, int size,String search,String tag) {

        //文章总数
        int totalCount;
        //如果搜索参数不为空
        if(StringUtils.isNotBlank(search)){
            totalCount=questionMapper.countBySearch(search);
        }else if(StringUtils.isNotBlank(tag)){
            totalCount=questionMapper.countByTag(tag);
        }else {
            totalCount=questionMapper.count();
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
        //拿到Question对象
        List<Question> questions;
        if(StringUtils.isNotBlank(search)){
            questions=questionMapper.listBySearch(search,offset, size);
        }else if(StringUtils.isNotBlank(tag)){
            questions=questionMapper.listByTag(tag,offset, size);
        }else {
            questions=questionMapper.list(offset, size);
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //遍历Question对象，将User对象一起输入到QuestionDTO
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //利用spring内置的工具类将question的属性复制给questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将QuestionDTO放到pageDTO对象，便于前端通过pageDTO拿到文章、页码等数据
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }


    public PageDTO listByUserId(int userId,int page, int size) {

        PageDTO pageDTO = new PageDTO();

        //文章总数
        int totalCount = questionMapper.countByUserId(userId);

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
        //拿到Question对象
        List<Question> questions = questionMapper.listByUserId(userId,offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //遍历Question对象，将User对象一起输入到QuestionDTO
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //利用spring内置的工具类将question的属性复制给questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //将QuestionDTO放到pageDTO对象，便于前端通过pageDTO拿到文章、页码等数据
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(int id) {
        Question question=questionMapper.getById(id);
        //错误处理
        //如果查询的文章不存在，直接跳到异常并进行相应提示
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==0){
            //插入新文章
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else {
            //更新文章
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }


    //更新阅读数
    public void updateViewById(int id,User user) {
        //如果用户自己看自己的文章，不增加阅读数
        if(user==null||user.getId()!=questionMapper.getCreatorIdById(id))
        {
            questionMapper.updateViewById(id);
        }
    }

    public List<QuestionDTO> findRelatedArticle(QuestionDTO questionDTO) {
        //如果该文章没有相关的标签，返回空List
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }
        List<Question>  questions=new ArrayList<>(10);
        //通过逗号将标签分割
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        for (String tag:tags
             ) {
            //将不同的tag查询得到的question对象集合保存到list，可能有重复值
           questions.addAll(questionMapper.findRelatedTag(tag));

        }

        //拿到的对象可能有重复的数据，根据id去重
        List<Question> unique = questions.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Question::getId))),ArrayList::new));
        //将question赋值questionDTO
        List<QuestionDTO> questionDTOS = unique.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());

        return questionDTOS;

    }
}
