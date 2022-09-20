package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class BinhLuan {

    @SerializedName("mabinhluan")
    private int mabinhluan;

    @SerializedName("matruyen")
    private int matruyen;

    @SerializedName("mataikhoan")
    private int mataikhoan;

    @SerializedName("tenhienthi")
    private String tenhienthi;

    @SerializedName("binhluan")
    private String binhluan;

    @SerializedName("success")
    private int success;

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

    public int getMataikhoan() {
        return mataikhoan;
    }

    public String getTenhienthi() {
        return tenhienthi;
    }

    public void setTenhienthi(String tenhienthi) {
        this.tenhienthi = tenhienthi;
    }

    public void setMataikhoan(int mataikhoan) {
        this.mataikhoan = mataikhoan;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}