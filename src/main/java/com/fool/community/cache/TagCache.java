package com.fool.community.cache;

import com.fool.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        //开发语言类别
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("JavaScript", "Php", "Css", "Html", "Html5", "Java", "Node.js", "Python", "C++", "C", "Golang", "objective-c", "TypeScript", "shell", "swift", "C#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOS.add(program);

        //平台框架类别
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "Spring", "Express", "Django", "Flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOS.add(framework);


        //服务器类别
        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux", "Nginx", "docker", "Apache", "Ubuntu", "Centos", "缓存 Tomcat", "负载均衡", "Unix", "Hadoop", "windows-server"));
        tagDTOS.add(server);

        //数据库类别
        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("MySQL", "Redis", "Mongodb", "SQL", "Oracle", "nosql memcached", "SqlServer", "Postgresql", "Sqlite"));
        tagDTOS.add(db);

        //开发工具类别
        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("Git", "Github", "Visual-studio-code", "Vim", "sublime-text", "xcode intellij-idea", "Eclipse", "Naven", "IDE", "SVN", "Visual-Studio", "atom emacs", "textmate", "hg"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

}
