package com.fool.community.dto;

import com.fool.community.exception.CustomizeErrorCode;
import com.fool.community.exception.CustomizeException;

//用T的原因是传进来的类型不确定，所以使用泛型
public class ResultDTO<T> {
    //类似400的状态码
    private Integer code;
    //发送消息
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    //仅返回成功标志
    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    //这个是用来返回带数组或者其他类型的方法
    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    //仅返回成功标志
    public static ResultDTO noOkOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(2001);
        resultDTO.setMessage("未登录");
        return resultDTO;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
