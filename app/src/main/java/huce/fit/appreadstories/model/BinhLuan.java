package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class BinhLuan extends TaiKhoan{

    @SerializedName("mabinhluan")
    private int mabinhluan;

    @SerializedName("matruyen")
    private int matruyen;

    @SerializedName("binhluan")
    private String binhluan;

    @SerializedName("commentsuccess")
    private int commentsuccess;

    public int getMabinhluan() {
        return mabinhluan;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public int getCommentsuccess() {
        return commentsuccess;
    }
}