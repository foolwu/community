package life.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {
    //表示什么都不输入的时候默认访问index
    @GetMapping("/")
    public String index(){return "index";}
}
