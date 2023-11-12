package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Truyen {

	@SerializedName("matruyen")
	private int matruyen;

	@SerializedName("tentruyen")
	private String tentruyen;

	@SerializedName("tongchuong")
	private int tongchuong;

	@SerializedName("tacgia")
	private String tacgia;

	@SerializedName("trangthai")
	private String trangthai;

	@SerializedName("theloai")
	private String theloai;

	@SerializedName("thoigiancapnhat")
	private String thoigiancapnhat;

	@SerializedName("anh")
	private String anh;

	@SerializedName("gioithieu")
	private String gioithieu;

	@SerializedName("diemdanhgia")
	private float diemdanhgia;

	@SerializedName("luotxem")
	private int luotxem;

	@SerializedName("luotthich")
	private int luotthich;

	@SerializedName("luotbinhluan")
	private int luotbinhluan;

	@SerializedName("gioihantuoi")
	private int gioihantuoi;

	@SerializedName("tylechuongdadoc")
	private String tylechuongdadoc;

	@SerializedName("storySuccess")
	private int storySuccess;

	public int getMatruyen() {
		return matruyen;
	}

	public String getTentruyen() {
		return tentruyen;
	}

	public int getTongchuong() {
		return tongchuong;
	}

	public String getTacgia() {
		return tacgia;
	}

	public String getTrangthai() {
		return trangthai;
	}

	public String getTheloai() {
		return theloai;
	}

	public String getThoigiancapnhat() {
		return thoigiancapnhat;
	}

	public String getAnh() {
		return anh;
	}

	public String getGioithieu() {
		return gioithieu;
	}

	public float getDiemdanhgia() {
		return diemdanhgia;
	}

	public int getLuotxem() {
		return luotxem;
	}

	public int getLuotthich() {
		return luotthich;
	}

	public int getLuotbinhluan() {
		return luotbinhluan;
	}

	public int getGioihantuoi() {
		return gioihantuoi;
	}

	public String getTylechuongdadoc() {
		return tylechuongdadoc;
	}

	public int getStorySuccess() {
		return storySuccess;
	}
}