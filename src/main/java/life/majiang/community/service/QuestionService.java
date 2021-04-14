package life.majiang.community.service;

import life.majiang.community.Entity.Question;
import life.majiang.community.Entity.User;
import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PageDTO List(int page, int size) {

        PageDTO pageDTO = new PageDTO();

        //文章总数
        int totalCount = questionMapper.count();

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
        List<Question> questions = questionMapper.List(offset, size);
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
        pageDTO.setQuestions(questionDTOList);
        return pageDTO;
    }
}
