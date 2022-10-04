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

    public void setMabinhluan(int mabinhluan) {
        this.mabinhluan = mabinhluan;
    }

    public int getMatruyen() {
        return matruyen;
    }

    public void setMatruyen(int matruyen) {
        this.matruyen = matruyen;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }

    public int getCommentsuccess() {
        return commentsuccess;
    }

    public void setCommentsuccess(int commentsuccess) {
        this.commentsuccess = commentsuccess;
    }
}