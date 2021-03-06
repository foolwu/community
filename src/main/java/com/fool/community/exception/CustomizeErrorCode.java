package com.fool.community.exception;


//错误信息封装在枚举类CustomizeErrorCode里，增加可重用性
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    //文章不存在
    ARTICLE_NOT_FOUND(2001, "你找到文章不在了，要不要换个试试？"),
    //未选中评论就进行回复
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    //未登录
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYS_ERROR(2004, "服务器冒烟了，要不然你稍后再试试！！！"),
    //评论的类型出错
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    //回复的评论不见了
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    INVALID_INPUT(2011, "非法输入"),
    INVALID_OPERATION(2012, "兄弟，是不是走错房间了？"),
    ;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
