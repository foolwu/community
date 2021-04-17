package life.majiang.community.dto;


import life.majiang.community.entity.Question;
import life.majiang.community.entity.User;

public class NotificationDTO {
    private Long id;
    //时间
    private Long gmtCreate;
    private Integer status;
    //通知的人
    private int notifier;
    private String notifierName;
    //外部显示的文章名字
    private String outerTitle;
    private int outerId;
    private String typeName;
    private Integer type;
    private int ArticleId;
    //存储消息通知的对象
    private User user;
    //存储消息相关的文章
    private Question question;

    public int getArticleId() {
        return ArticleId;
    }

    public void setArticleId(int articleId) {
        ArticleId = articleId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getNotifier() {
        return notifier;
    }

    public void setNotifier(int notifier) {
        this.notifier = notifier;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public String getOuterTitle() {
        return outerTitle;
    }

    public void setOuterTitle(String outerTitle) {
        this.outerTitle = outerTitle;
    }

    public int getOuterId() {
        return outerId;
    }

    public void setOuterId(int outerId) {
        this.outerId = outerId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
