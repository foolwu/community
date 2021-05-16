package com.fool.community.enums;

//这个枚举类用来判断是回复的是文章还是评论
public enum CommentTypeEnum {
    //文章类型
    QUESTION(1),
    //评论类型
    COMMENT(2);
    private Integer type;


    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
