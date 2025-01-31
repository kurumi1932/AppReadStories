package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Rate extends Account {

	@SerializedName("madanhgia")
	private int rateId;

	@SerializedName("diemdanhgia")
	private int ratePoint;

	@SerializedName("danhgia")
	private String rateContent;

	@SerializedName("ratesuccess")
	private int rateSuccess;

	public Rate() {}

	public Rate(Rate rate) {
		this.rateId = rate.rateId;
		this.ratePoint = rate.ratePoint;
		this.rateContent = rate.rateContent;
		this.rateSuccess = rate.rateSuccess;
	}

	public Rate(int rateId, int ratePoint, String rateContent) {
		this.rateId = rateId;
		this.ratePoint = ratePoint;
		this.rateContent = rateContent;
	}

	public int getRateId() {
		return rateId;
	}

	public void setRateId(int rateId) {
		this.rateId = rateId;
	}

	public int getRatePoint() {
		return ratePoint;
	}

	public void setRatePoint(int ratePoint) {
		this.ratePoint = ratePoint;
	}

	public String getRateContent() {
		return rateContent;
	}

	public void setRateContent(String rate) {
		this.rateContent = rateContent;
	}

	public int getRateSuccess() {
		return rateSuccess;
	}

	public void setRateSuccess(int success) {
		rateSuccess = success;
	}
}