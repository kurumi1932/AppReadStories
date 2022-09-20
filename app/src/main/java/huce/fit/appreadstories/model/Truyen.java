package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Truyen {

	@SerializedName("theloai")
	private String theloai;

	@SerializedName("thoigiancapnhat")
	private String thoigiancapnhat;

	@SerializedName("gioithieu")
	private String gioithieu;

	@SerializedName("matruyen")
	private int matruyen;

	@SerializedName("trangthai")
	private String trangthai;

	@SerializedName("anh")
	private String anh;

	@SerializedName("tentruyen")
	private String tentruyen;

	@SerializedName("tacgia")
	private String tacgia;

	@SerializedName("sochuong")
	private int sochuong;

	public void setTheloai(String theloai){
		this.theloai = theloai;
	}

	public String getTheloai(){
		return theloai;
	}

	public void setThoigiancapnhat(String thoigiancapnhat){
		this.thoigiancapnhat = thoigiancapnhat;
	}

	public String getThoigiancapnhat(){
		return thoigiancapnhat;
	}

	public void setGioithieu(String gioithieu){
		this.gioithieu = gioithieu;
	}

	public String getGioithieu(){
		return gioithieu;
	}

	public void setMatruyen(int matruyen){
		this.matruyen = matruyen;
	}

	public int getMatruyen(){
		return matruyen;
	}

	public void setTrangthai(String trangthai){
		this.trangthai = trangthai;
	}

	public String getTrangthai(){
		return trangthai;
	}

	public void setAnh(String anh){
		this.anh = anh;
	}

	public String getAnh(){
		return anh;
	}

	public void setTentruyen(String tentruyen){
		this.tentruyen = tentruyen;
	}

	public String getTentruyen(){
		return tentruyen;
	}

	public void setTacgia(String tacgia){
		this.tacgia = tacgia;
	}

	public String getTacgia(){
		return tacgia;
	}

	public void setSochuong(int sochuong){
		this.sochuong = sochuong;
	}

	public int getSochuong(){
		return sochuong;
	}
}