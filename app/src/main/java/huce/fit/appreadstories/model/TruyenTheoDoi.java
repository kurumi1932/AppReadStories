package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class TruyenTheoDoi extends Truyen {

    @SerializedName("mataikhoan")
    private int mataikhoan;

    @SerializedName("success")
    private int success;

    public void setMataikhoan(int mataikhoan){
        this.mataikhoan = mataikhoan;
    }

    public int getMataikhoan(){
        return mataikhoan;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

}