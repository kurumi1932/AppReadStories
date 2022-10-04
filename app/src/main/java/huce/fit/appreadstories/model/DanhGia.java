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

	public void setDiemdanhgia(int diemdanhgia){
		this.diemdanhgia = diemdanhgia;
	}

	public int getDiemdanhgia(){
		return diemdanhgia;
	}

	public void setMatruyen(int matruyen){
		this.matruyen = matruyen;
	}

	public int getMatruyen(){
		return matruyen;
	}

	public void setDanhgia(String danhgia){
		this.danhgia = danhgia;
	}

	public String getDanhgia(){
		return danhgia;
	}

	public void setMadanhgia(int madanhgia){
		this.madanhgia = madanhgia;
	}

	public int getMadanhgia(){
		return madanhgia;
	}

	public int getRatesuccess() {
		return ratesuccess;
	}

	public void setRatesuccess(int ratesuccess) {
		this.ratesuccess = ratesuccess;
	}
}