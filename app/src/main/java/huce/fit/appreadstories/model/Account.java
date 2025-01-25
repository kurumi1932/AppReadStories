package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Account {

	@SerializedName("mataikhoan")
	private int accountId;

	@SerializedName("matkhau")
	private String password;

	@SerializedName("taikhoan")
	private String username;

	@SerializedName("email")
	private String email;

	@SerializedName("tenhienthi")
	private String displayName;

	@SerializedName("ngaysinh")
	private String birthday;

	@SerializedName("accountsuccess")
	private int success;

	public int getAccountId() {
		return accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getSuccess() {
		return success;
	}
}