
/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=2859958f9f059979ed3a&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 得到二级评论的展开状态
    //如果collapse为1则展开二级评论，否则收缩
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        //判断点击评论回复图片时里面的元素，如果超过1，说明已经加载，就不用去重新加载
        //目的是当用户进入文章页打开过二级评论时，再次点击直接显示就行了，不用刷新到数据库加载
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            //点击评论时，先不做评论而是先获取到请求以后再去做
            $.getJSON("/comment/" + id, function (data) {
                //拼接标签
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {

                    //拼接左边显示头像标签
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-thumbnail",
                        "src": comment.user.avatarUrl
                    }));

                    //拼接显示右边的标签
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html" : moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));
                    //moement用来转换js里的时间，可以将其转换想要的时间格式

                    //整体
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    var c= $("<div>/",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                        html:comment.content
                    })
                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}


function showSelectTag() {
    $("#select-tag").show();
    console.log("进入点击事件");
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    //获取当前的标签内容
    var previous = $("#tag").val();
    console.log(2);

    if (previous) {
        var index = 0;
        var appear = false; //记录value是否已经作为一个独立的标签出现过
        while (true) {
            index = previous.indexOf(value, index); //value字符串在previous中出现的位置
            if (index == -1) break;
            //判断previous中出现的value是否是另一个标签的一部分
            //即value的前一个和后一个字符都是逗号","或者没有字符时，才说明value是一个独立的标签
            if ((index == 0 || previous.charAt(index - 1) == ",")
                && (index + value.length == previous.length || previous.charAt(index + value.length) == ",")
               ) {
                appear = true;
                break;
            }
            index++; //用于搜索下一个出现位置
        }
        if (!appear) {
            //若value没有作为一个独立的标签出现过
            $("#tag").val(previous + ',' + value);
        }
    }
    else {
        $("#tag").val(value);
    }
}