package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Comment extends Account {

    @SerializedName("mabinhluan")
    private int commentId;

    @SerializedName("matruyen")
    private int storyId;

    @SerializedName("binhluan")
    private String comment;

    @SerializedName("commentsuccess")
    private int success;

    public int getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int getSuccess() {
        return success;
    }
}