package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component的作用是将当前类紧紧的初始化到spring的上下文
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
               .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()){
            String string=response.body().string();
            //先通过&号截取得到前面有token的部分，再通过=号截取得到token
            String token=string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e){
            e.printStackTrace();
        }
        //如果返回的格式不是access_token=?&scope=?&token_type=bearer，那就抛异常
        //抛异常就返回null，在前端判断一下就行了
        return null;
    }

    //通过accessToken获得用户信息
    public GithubUser getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //旧的方式将导致无法通过access_token获取用户，会报错返回null
                /*.url("https://api.github.com/user?access_token="+accessToken)
                .build();*/
                //改为如下方式
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            //把String的json对象转换成Java的类对象，不用自己去解析
            String string = response.body().string();
            GithubUser githubUser= JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        //上一步未成功直接返回null对象
        return null;
    }
}


