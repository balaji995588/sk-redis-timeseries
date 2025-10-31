package com.redis_timeseries.domain;

public class ClosePriceData {

	private String exChange = "";
	private int scripCode = 0;
	private int tradeDate = 0;
	private float openPrice = 0.00f;
	private float highPrice = 0.00f;
	private float lowPrice = 0.00f;
	private float closePrice = 0.00f;
	private long tradedQuantity = 0;
	private long tradedValue = 0;
	private String scripSymbol = "";
	private String optType = "";
	private float strikePrice = 0.00f;
	private int expDate = 0;
	private float avgPrice = 0.00f;

	public String getExChange() {
		return exChange;
	}

	public void setExChange(String exChange) {
		this.exChange = exChange;
	}

	public int getScripCode() {
		return scripCode;
	}

	public void setScripCode(int scripCode) {
		this.scripCode = scripCode;
	}

	public int getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(int tradeDate) {
		this.tradeDate = tradeDate;
	}

	public float getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(float openPrice) {
		this.openPrice = openPrice;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public float getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(float closePrice) {
		this.closePrice = closePrice;
	}

	public long getTradedQuantity() {
		return tradedQuantity;
	}

	public void setTradedQuantity(long tradedQuantity) {
		this.tradedQuantity = tradedQuantity;
	}

	public long getTradedValue() {
		return tradedValue;
	}

	public void setTradedValue(long tradedValue) {
		this.tradedValue = tradedValue;
	}

	public String getScripSymbol() {
		return scripSymbol;
	}

	public void setScripSymbol(String scripSymbol) {
		this.scripSymbol = scripSymbol;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public float getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(float strikePrice) {
		this.strikePrice = strikePrice;
	}

	public int getExpDate() {
		return expDate;
	}

	public void setExpDate(int expDate) {
		this.expDate = expDate;
	}

	public float getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(float avgPrice) {
		this.avgPrice = avgPrice;
	}

}
