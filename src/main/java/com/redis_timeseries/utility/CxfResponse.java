package com.redis_timeseries.utility;

import org.springframework.stereotype.Component;

@Component
public class CxfResponse {

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	private int resCode;
	private String Message;
	// private String ResponseData;

	/**
	 * @return the resCode
	 */

	public CxfResponse() {

	}
}
