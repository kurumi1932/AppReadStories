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

	@SerializedName("ngaysinh")
	private String ngaysinh;

	@SerializedName("accountsuccess")
	private int accountsuccess;

	public int getMataikhoan(){
		return mataikhoan;
	}

	public String getMatkhau(){
		return matkhau;
	}

	public String getEmail(){
		return email;
	}

	public String getTenhienthi(){
		return tenhienthi;
	}

	public String getNgaysinh() {
		return ngaysinh;
	}

	public int getAccountsuccess() {
		return accountsuccess;
	}
}