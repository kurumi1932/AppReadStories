package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Comment extends Account {

    @SerializedName("mabinhluan")
    private int commentId;

    @SerializedName("binhluan")
    private String commentContent;

    @SerializedName("commentsuccess")
    private int commentSuccess;

    public Comment() {
    }

    public Comment(Comment comment) {
        commentId = comment.commentId;
        commentContent = comment.commentContent;
        commentSuccess = comment.commentSuccess;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public int getCommentSuccess() {
        return commentSuccess;
    }
}