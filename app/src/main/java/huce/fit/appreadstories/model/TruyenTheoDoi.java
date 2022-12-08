package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class TruyenTheoDoi extends Truyen {

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

}