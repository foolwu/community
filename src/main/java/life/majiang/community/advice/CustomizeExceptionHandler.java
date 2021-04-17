package life.majiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//处理页面级的跳转
@ControllerAdvice
public class CustomizeExceptionHandler {
    //返回mdleo而不是json
    //出现错误后跳转到定义的页面
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        //通过request的contentype来判断是application/json传输还是html页面的方式
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            // 返回 JSON
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
//                log.error("handle error", e);
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                    //设置response内容
                  response.setContentType("application/json");
                   response.setStatus(200);
                   //改变字符集
                   response.setCharacterEncoding("utf-8");
                   PrintWriter writer = response.getWriter();
                   //把resultDTO变成Json对象
                   writer.write(JSON.toJSONString(resultDTO));
                   writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }else {
            // 错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
//                log.error("handle error", e);
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
