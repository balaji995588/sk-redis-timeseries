//package com.redis_timeseries.domain;
//
//import java.io.Serializable;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.StringTokenizer;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import com.redis_timeseries.utility.Utility;
//
//
//
//public class ChartData implements Serializable {
//
//	
//	private static final Logger LOGGER = LogManager.getLogger();
//	private static final long serialVersionUID = 1L;
//	
//	private  int tradeDate;
//	private  int time;
//	@SerializedName("t")
//	@Expose private long lTime;
//	@SerializedName("o")
//	@Expose private float openPrice;
//	@SerializedName("h")
//	@Expose private float highPrice;
//	@SerializedName("l")
//	@Expose private float lowPrice;
//	@SerializedName("c")
//	@Expose private float closePrice;
//	@SerializedName("q")
//	@Expose private long tradeQty;
//	@SerializedName("v")
//	@Expose private  float tradeValue;
//	
//	public  String MinTime;
//	public  String TrdTime;
//	public  String TrdDate;
//	public  int weekNumber;
//	private  String closeTime;
//	private  String tradeTime;
//	
//	private long Open_Int;
////	private  float openp;
////	private  float closep;
//	
//// for Daily chart data 
////	private  float openPrice;
////	private  float closePrice;
////	private  float highPrice;
////	private  float lowPrice;
////	private  long tradeQty;
////	private  float tradeValue;
//
////	public float getHighPrice() {
////		return highPrice;
////	}
////	public void setHighPrice(float highPrice) {
////		//this.highPrice = highPrice;
////		setHigh(highPrice);
////	}
////	public float getLowPrice() {
////		return lowPrice;
////	}
////	public void setLowPrice(float lowPrice) {
////		//this.lowPrice = lowPrice;
////		setLow(lowPrice);
////	}
////	public long getTradeQty() {
////		return tradeQty;
////	}
////	public void setTradeQty(long tradeQty) {
////		//this.tradeQty = tradeQty;
////		setQty(tradeQty);
////	}
////	public float getTradeValue() {
////		return tradeValue;
////	}
////	public void setTradeValue(float tradeValue) {
////		//this.tradeValue = tradeValue;
////		setTradedValue(tradeValue);
////	}
////	public float getOpenPrice() {
////		return openPrice;
////	}
////	public void setOpenPrice(float openPrice) {
////		//this.openPrice = openPrice;
////		setOpen(openPrice);
////	}
////	public float getClosePrice() {
////		return closePrice;
////	}
////	public void setClosePrice(float closePrice) {
////		//this.closePrice = closePrice;
////		setClose(closePrice);
////	}
//
////	public float getClosep() {
////		return closep;
////	}
////	public void setClosep(float closep) {
////		this.closep = closep;
////		setClose(closep);
////	}
////	public float getOpenp() {
////		return openp;
////	}
////	public void setOpenp(float openp) {
////		this.openp = openp;
////		setOpen(openp);
////	}
//	public String getTradeTime() {
//		return tradeTime;
//	}
//	public void setTradeTime(String tradeTime) {
//		this.tradeTime = tradeTime;
//		Integer iTime = Integer.parseInt(tradeTime.replace(":", ""));
//		setTime(iTime);
//		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		Date currDate=null;
//		try {
//			currDate = format.parse(getTrdDate());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		if(currDate!=null){
//		try {
//			StringTokenizer st = new StringTokenizer(tradeTime,
//					":", false);
//			int i = 0;
//
//			while (st.hasMoreElements()) {
//				switch (i) {
//				case 0:
//					currDate.setHours(Integer.parseInt(st
//							.nextElement().toString()));
//					break;
//				case 1:
//					currDate.setMinutes(Integer.parseInt(st
//							.nextElement().toString()));
//					break;
//				case 2:
//					currDate.setSeconds(Integer.parseInt(st
//							.nextElement().toString()));
//					break;
//				default:
//					break;
//				}
//				i++;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		
//		setlTime(currDate.getTime() / 1000);
//		}
//	}
//	public String getCloseTime() {
//		return closeTime;
//	}
//	public void setCloseTime(String closeTime) {
//		this.closeTime = closeTime;
//		setTime(Integer.parseInt(closeTime.replace(":", "")));
//	}
//	public String getTrdDate() {
//		return TrdDate;
//	}
//	public void setTrdDate(String trdDate) {
//		TrdDate = trdDate;		
//		setTradeDate(Integer.parseInt(trdDate));
//	}
//	public int getTime() {
//		return time;
//	}    
//	public void setTime(int time) {
//		this.time = time;
//	}
//
//	
//
//	public float getOpenPrice() {
//		return openPrice;
//	}
//	public void setOpenPrice(float openPrice) {
//		this.openPrice = openPrice;
//	}
//	public float getHighPrice() {
//		return highPrice;
//	}
//	public void setHighPrice(float highPrice) {
//		this.highPrice = highPrice;
//	}
//	public float getLowPrice() {
//		return lowPrice;
//	}
//	public void setLowPrice(float lowPrice) {
//		this.lowPrice = lowPrice;
//	}
//	public float getClosePrice() {
//		return closePrice;
//	}
//	public void setClosePrice(float closePrice) {
//		this.closePrice = closePrice;
//	}
//	public long getTradeQty() {
//		return tradeQty;
//	}
//	public void setTradeQty(long tradeQty) {
//		this.tradeQty = tradeQty;
//	}
//	public float getTradeValue() {
//		return tradeValue;
//	}
//	public void setTradeValue(float tradeValue) {
//		this.tradeValue = tradeValue;
//	}
//	public int getTradeDate() {
//		return tradeDate;
//	}
//
//	public void setTradeDate(int tradeDate) {
//		try{
//		this.tradeDate = tradeDate;		
//		Calendar cal = Calendar.getInstance();
//		DateFormat format = new SimpleDateFormat("yyyyMMdd");
//		Date currDate=null;
//		try {
//			currDate = format.parse(String.valueOf(tradeDate));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(currDate!=null){
//		cal.setTime(currDate);
//		weekNumber = cal.get(Calendar.WEEK_OF_YEAR);	
//		setlTime(currDate.getTime() / 1000);
//		}		
//		}catch(Exception e){
//			LOGGER.info("tradeDate",e);
//		}
//	}
//
//	public int getWeekNumber() {
//		return weekNumber;
//	}
//	public void setWeekNumber(int weekNumber) {
//		this.weekNumber = weekNumber;
//	}
//
//	
//
////	@Override
////	public String toString() {
////		return "ChartData [lTime=" +Utility.getINDDateFromLong(lTime)+ ", open=" + open + ", high="
////				+ high + ", low=" + low + ", close=" + close + ", qty=" + qty
////				+ ", tradedValue=" + tradedValue +", WeekNumber=" + getWeekNumber() + "]";
////	}
//	
//	public ChartData() {
//	}
//
////	public ChartData(byte[] data, int factor) {
////
////		int count = 0;
////		tradeDate = ByteUtil.getInt(Arrays.copyOfRange(data, count, 4));	
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		time = ByteUtil.getInt(Arrays.copyOfRange(data, count, 4));
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		open = ByteUtil.getFloat(Arrays.copyOfRange(data, count, 4), factor);
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		high = ByteUtil.getFloat(Arrays.copyOfRange(data, count, 4), factor);
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		low = ByteUtil.getFloat(Arrays.copyOfRange(data, count, 4), factor);
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		close = ByteUtil.getFloat(Arrays.copyOfRange(data, count, 4), factor);
////		count += 4;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		qty = ByteUtil.getLong(Arrays.copyOfRange(data, count, 8));
////		count += 8;
////		data = Arrays.copyOfRange(data, count, data.length);
////
////		tradedValue = ByteUtil.getLong(Arrays.copyOfRange(data, count, 8)) / 100f;
////	}
//
//	public long getlTime() {
//		return lTime;
//	}
//
//	public void setlTime(long lTime) {
//		this.lTime = lTime;
//		Date dt = Utility.getDateFromLong(lTime);		
//		SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
//		SimpleDateFormat tsf = new SimpleDateFormat("HH:mm:ss");
//		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
//		TrdDate =df.format(dt);
//		MinTime = tf.format(dt);
//		TrdTime = tsf.format(dt);
//	}
//
//	public Date getDateTime() {
//		return Utility.getDateFromLong(lTime);
//	}
//	
//	
//	
//	
//	
//
//	
//	
//	
//	public long getOpen_Int() {
//		return Open_Int;
//	}
//	public void setOpen_Int(long open_Int) {
//		Open_Int = open_Int;
//	}
//
//	//public static void main(String[] args) {
//		// ch=new ChartData();
//		//ch.setTradeDate(20171006);
//		//System.out.println(ch.toString());
//	//}
//}
