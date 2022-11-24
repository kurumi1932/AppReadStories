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

	public void setMatruyen(int matruyen){
		this.matruyen = matruyen;
	}

	public int getMatruyen(){
		return matruyen;
	}

	public void setMachuong(int machuong){
		this.machuong = machuong;
	}

	public int getMachuong(){
		return machuong;
	}

	public String getSochuong() {
		return sochuong;
	}

	public void setSochuong(String sochuong) {
		this.sochuong = sochuong;
	}

	public void setTenchuong(String tenchuong){
		this.tenchuong = tenchuong;
	}

	public String getTenchuong(){
		return tenchuong;
	}

	public void setNguoidang(String nguoidang){
		this.nguoidang = nguoidang;
	}

	public String getNguoidang(){
		return nguoidang;
	}

	public void setThoigiandang(String thoigiandang){
		this.thoigiandang = thoigiandang;
	}

	public String getThoigiandang(){
		return thoigiandang;
	}

	public void setNoidung(String noidung){
		this.noidung = noidung;
	}

	public String getNoidung(){
		return noidung;
	}

	public int getThaydoichuong() {
		return thaydoichuong;
	}

	public void setThaydoichuong(int thaydoichuong) {
		this.thaydoichuong = thaydoichuong;
	}

	public int getSo() {
		return so;
	}

	public void setSo(int so) {
		this.so = so;
	}
}