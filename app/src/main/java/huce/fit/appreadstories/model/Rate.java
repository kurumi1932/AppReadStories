package huce.fit.appreadstories.model;

import com.google.gson.annotations.SerializedName;

public class Rate extends Account {

	public Rate() {
	}

	public Rate(int rateId, int ratePoint, String rate, int success) {
		this.rateId = rateId;
		this.ratePoint = ratePoint;
		this.rate = rate;
		this.success = success;
	}

	@SerializedName("madanhgia")
	private int rateId;

	@SerializedName("diemdanhgia")
	private int ratePoint;

	@SerializedName("danhgia")
	private String rate;

	@SerializedName("ratesuccess")
	private int success;

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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@Override
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
}