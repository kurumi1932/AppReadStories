package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class ChuongTruyen{

	@SerializedName("matruyen")
	private int matruyen;

	@SerializedName("machuong")
	private int machuong;

	@SerializedName("sochuong")
	private String sochuong;

	@SerializedName("tenchuong")
	private String tenchuong;

	@SerializedName("nguoidang")
	private String nguoidang;

	@SerializedName("thoigiandang")
	private String thoigiandang;

	@SerializedName("noidung")
	private String noidung;

	@SerializedName("thaydoichuong")
	private int thaydoichuong;//thay đổi chương

	@SerializedName("so")
	private int so;//phân biệt danh sách chương đã đọc và chương đang đọc

	public int getMatruyen(){
		return matruyen;
	}

	public int getMachuong(){
		return machuong;
	}

	public String getSochuong() {
		return sochuong;
	}

	public String getTenchuong(){
		return tenchuong;
	}

	public String getNguoidang(){
		return nguoidang;
	}

	public String getThoigiandang(){
		return thoigiandang;
	}

	public String getNoidung(){
		return noidung;
	}
}