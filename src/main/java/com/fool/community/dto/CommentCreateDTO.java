package com.fool.community.dto;



public class CommentCreateDTO {
    private int parentId;
    private String content;
    private Integer type;
    //进行二级评论的时候需不但需要记录其父id，还要记录其所属的文章id，用来记录到消息里
    private int notificationParentArticle;

    public int getNotificationParentArticle() {
        return notificationParentArticle;
    }

    public void setNotificationParentArticle(int notificationParentArticle) {
        this.notificationParentArticle = notificationParentArticle;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
