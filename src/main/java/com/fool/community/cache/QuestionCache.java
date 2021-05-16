package com.fool.community.cache;

import com.fool.community.mapper.ArticleExtMapper;
import com.fool.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionCache {
    @Autowired
    private ArticleExtMapper articleExtMapper;
    @Autowired
    private UserMapper userMapper;

    /*private static Cache<String, List<ArticleDTO>> cacheQuestions = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .removalListener(entity -> log.info("QUESTIONS_CACHE_REMOVE:{}", entity.getKey()))
            .build();*/

    /*public List<ArticleDTO> getStickies() {
        List<ArticleDTO> stickies;
        try {
            stickies = cacheQuestions.get("sticky", () -> {
                List<Article> questions = articleExtMapper.selectSticky();
                if (questions != null && questions.size() != 0) {
                    List<ArticleDTO> questionDTOS = new ArrayList<>();
                    for (Article question : questions) {
                        User user = userMapper.selectByPrimaryKey(question.getCreator());
                        ArticleDTO questionDTO = new ArticleDTO();
                        BeanUtils.copyProperties(question, questionDTO);
                        questionDTO.setUser(user);
                        questionDTO.setDescription("");
                        questionDTOS.add(questionDTO);
                    }
                    return questionDTOS;
                } else {
                    return Lists.newArrayList();
                }
            });
        } catch (Exception e) {
            log.error("getStickies error", e);
            return Lists.newArrayList();
        }
        return stickies;
    }*/
}
