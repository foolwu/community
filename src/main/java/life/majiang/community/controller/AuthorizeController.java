package life.majiang.community.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //@Value的作用是配置文件里读取相应名字的值并赋给当前变量
    @Value("${github.client.id}")
    private String  clientId;
    @Value("${github.client.secret}")
    private String  clientSecret;
    @Value("${github.redirect.uri}")
    private String  redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken=githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user=githubProvider.getUser(accessToken);
        if(user!=null){
            //登录成功，写kookie和session
            request.getSession().setAttribute("user",user);
            //重定向，如果不写redrect的话地址栏会带上参数，然后渲染成index。写了会去掉参数重定向到index
            return "redirect:/";
        }else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }

}