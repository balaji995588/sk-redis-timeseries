package com.redis_timeseries.utilityenum;

public enum RServerExchanges {

	NC("NC"), NF("NF"), RN("RN"), BSE("BSE"), COMM("COMM"),RM("RM"), BF("BF");

	private final String id;

	RServerExchanges(String id) {
		this.id = id;
	}

	public String getValue() {
		return id;
	}

}
