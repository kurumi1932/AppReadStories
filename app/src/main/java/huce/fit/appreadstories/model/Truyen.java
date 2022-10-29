package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Truyen {

	@SerializedName("matruyen")
	private int matruyen;

	@SerializedName("theloai")
	private String theloai;

	@SerializedName("thoigiancapnhat")
	private String thoigiancapnhat;

	@SerializedName("gioithieu")
	private String gioithieu;

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

	@SerializedName("luotxem")
	private int luotxem;

	@SerializedName("luotthich")
	private int luotthich;

	@SerializedName("luotbinhluan")
	private int luotbinhluan;

	@SerializedName("diemdanhgia")
	private float diemdanhgia;

	@SerializedName("chuongdangdoc")
	private int chuongdangdoc;

	@SerializedName("storysuccess")
	private int storysuccess;

	public void setMatruyen(int matruyen){
		this.matruyen = matruyen;
	}

	public int getMatruyen(){
		return matruyen;
	}

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

	public int getLuotxem() {
		return luotxem;
	}

	public void setLuotxem(int luotxem) {
		this.luotxem = luotxem;
	}

	public int getLuotthich() {
		return luotthich;
	}

	public void setLuotthich(int luotthich) {
		this.luotthich = luotthich;
	}

	public int getLuotbinhluan() {
		return luotbinhluan;
	}

	public void setLuotbinhluan(int luotbinhluan) {
		this.luotbinhluan = luotbinhluan;
	}

	public float getDiemdanhgia() {
		return diemdanhgia;
	}

	public void setDiemdanhgia(float diemdanhgia) {
		this.diemdanhgia = diemdanhgia;
	}

	public int getChuongdangdoc() {
		return chuongdangdoc;
	}

	public void setChuongdangdoc(int chuongdangdoc) {
		this.chuongdangdoc = chuongdangdoc;
	}

	public int getStorysuccess() {
		return storysuccess;
	}

	public void setStorysuccess(int storysuccess) {
		this.storysuccess = storysuccess;
	}
}