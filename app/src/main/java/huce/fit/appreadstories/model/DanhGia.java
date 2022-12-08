package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class DanhGia extends TaiKhoan{

	@SerializedName("diemdanhgia")
	private int diemdanhgia;

	@SerializedName("matruyen")
	private int matruyen;

	@SerializedName("danhgia")
	private String danhgia;

	@SerializedName("madanhgia")
	private int madanhgia;

	@SerializedName("ratesuccess")
	private int ratesuccess;

	public int getDiemdanhgia(){
		return diemdanhgia;
	}

	public String getDanhgia(){
		return danhgia;
	}

	public int getMadanhgia(){
		return madanhgia;
	}

	public int getRatesuccess() {
		return ratesuccess;
	}
}