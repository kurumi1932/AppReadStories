package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class TaiKhoan{

	@SerializedName("mataikhoan")
	private int mataikhoan;

	@SerializedName("matkhau")
	private String matkhau;

	@SerializedName("taikhoan")
	private String taikhoan;

	@SerializedName("email")
	private String email;

	@SerializedName("tenhienthi")
	private String tenhienthi;

	@SerializedName("accountsuccess")
	private int accountsuccess;

	public void setMataikhoan(int mataikhoan){
		this.mataikhoan = mataikhoan;
	}

	public int getMataikhoan(){
		return mataikhoan;
	}

	public void setMatkhau(String matkhau){
		this.matkhau = matkhau;
	}

	public String getMatkhau(){
		return matkhau;
	}

	public void setTaikhoan(String taikhoan){
		this.taikhoan = taikhoan;
	}

	public String getTaikhoan(){
		return taikhoan;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setTenhienthi(String tenhienthi){
		this.tenhienthi = tenhienthi;
	}

	public String getTenhienthi(){
		return tenhienthi;
	}

	public int getAccountsuccess() {
		return accountsuccess;
	}

	public void setAccountsuccess(int accountsuccess) {
		this.accountsuccess = accountsuccess;
	}
}