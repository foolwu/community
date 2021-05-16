package com.fool.community.interceptor;


import com.fool.community.entity.User;
import com.fool.community.mapper.UserMapper;
import com.fool.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    // @Autowired
    // private AdService adService;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        设置 context 级别的属性
//        request.getServletContext().setAttribute("redirectUri", redirectUri);
        // 没有登录的时候也可以查看导航
//        for (AdPosEnum adPos : AdPosEnum.values()) {
//            request.getServletContext().setAttribute(adPos.name(), adService.list(adPos.name()));
//        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //通过token查询用户是否存在
                    User user = userMapper.findByToken(token);
                    //如果用户不为空，再次设置session
                    if (user != null) {
//                        HttpSession session = request.getSession();
                        request.getSession().setAttribute("user", user);
                        int unReadCount = notificationService.countUnReadByUserId(user.getId());
                        request.getSession().setAttribute("unReadCount", unReadCount);
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
