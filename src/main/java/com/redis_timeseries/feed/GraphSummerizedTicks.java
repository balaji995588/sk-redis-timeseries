package com.redis_timeseries.feed;

import java.io.Serializable;
import java.util.Date;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redis_timeseries.utility.Exchanges;
import com.redis_timeseries.utility.ServiceConstants;
import com.redis_timeseries.utility.Utility;

public class GraphSummerizedTicks implements IBaseStruct, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger();

	private int exchange;
	private int scripCode;
	private long ltt;
	private float ltp;
	private long qty;
	private float avgPrice;

	private float open;
	private float close;
	private float high;
	private float low;

	public String MinTime;
	private String tickType = "";

	// List<GraphSummerizedTicks> data = new ArrayList<>();

	/*
	 * public List<GraphSummerizedTicks> getData() { return data; }
	 */

	public String getTickType() {
		return tickType;
	}

	public void setTickType(String tickType) {
		this.tickType = tickType;
	}

	public int getExchange() {
		return exchange;
	}

	public void setExchange(int exchange) {
		this.exchange = exchange;
	}

	public int getScripCode() {
		return scripCode;
	}

	public void setScripCode(int scripCode) {
		this.scripCode = scripCode;
	}

	public long getLtt() {
		return ltt;
	}

	public void setLtt(long ltt) {
		this.ltt = ltt;
		Date dt = Utility.getDateFromLong(ltt);
		MinTime = ServiceConstants.tf.format(dt);
	}

	public float getLtp() {
		return ltp;
	}

	public void setLtp(float ltp) {
		this.ltp = ltp;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public float getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(float avgPrice) {
		this.avgPrice = avgPrice;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public GraphSummerizedTicks(String exchange, int scripCode, String data) {
		try {

			Exchanges ex = Exchanges.fromShortString(exchange);
			if (ex == null)
				setExchange(0);
			else
				setExchange(ex.getValue());

			AtomicInteger currentIndex = new AtomicInteger(0);
			// int currentIndex = 0; // Maintain position
			setScripCode(scripCode);
			setLtt(Utility.getLongFromTime(Utility.getNextValue(data, currentIndex)));
			setTickType(Utility.getNextValue(data, currentIndex));
			setOpen(Utility.getFloat(Utility.getNextValue(data, currentIndex)));
			setHigh(Utility.getFloat(Utility.getNextValue(data, currentIndex)));
			setLow(Utility.getFloat(Utility.getNextValue(data, currentIndex)));
			setClose(Utility.getFloat(Utility.getNextValue(data, currentIndex)));
			setQty(Utility.getLong(Utility.getNextValue(data, currentIndex)));
			setAvgPrice(Utility.getFloat(Utility.getNextValue(data, currentIndex)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public GraphSummerizedTicks() {

	}

//	public GraphSummerizedTicks(String exchange, int scripCode, String graphdata)
//	{
//		//graphdata = 15:15:51|C|257.85|257.85|257.85|257.85|10|257.05|    : 18:52:36|C|1.3462|1.3462|1.3462|1.3462|5|1.3544|
////		LOGGER.info(graphdata);
//		try {
//			List<String> sFeed = new ArrayList<String>();
//			StringTokenizer st = new StringTokenizer(graphdata,
//					ServiceConstants.Pipe, false);
//
//			while (st.hasMoreElements()) {
//				String data = st.nextElement().toString();
//				sFeed.add(data);
//			}
//
//			Exchanges ex = Exchanges.fromShortString(exchange);
//
//			if (ex == null)
//				setExchange(0);
//			else
//				setExchange(ex.getValue());
//
//			
//			setScripCode(scripCode);
//			setLtt(Utility.getLongFromTime(sFeed.get(0)));
//			setTickType(sFeed.get(1));
//			setOpen(Utility.getFloat(sFeed.get(2)));
//			setHigh(Utility.getFloat(sFeed.get(3)));
//			setLow(Utility.getFloat(sFeed.get(4)));
//			setClose(Utility.getFloat(sFeed.get(5)));
//			setQty(Utility.getLong(sFeed.get(6)));
//			setAvgPrice(Utility.getFloat(sFeed.get(7)));
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//	
//	}
	@Override
	public void ByteToStruct(byte[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] StructToByte() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "GraphSummerizedTicks [exchange=" + exchange + ", scripCode=" + scripCode + ", ltt=" + ltt + ", ltp="
				+ ltp + ", qty=" + qty + ", avgPrice=" + avgPrice + ", open=" + open + ", close=" + close + ", high="
				+ high + ", low=" + low + "]";
	}

}
