package com.fool.community.dto;


public class FileDTO {
    //0|1，0表示上传失败，1表示上传成功
    private int success;
    //提示的信息，上传成功或者上传失败以及错误等信息
    private String message;
    //图片地址，上传成功才返回
    private String url;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
