package com.redis_timeseries.feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redis_timeseries.utility.Exchanges;
import com.redis_timeseries.utility.PropertyContainer;
import com.redis_timeseries.utility.ServiceConstants;
import com.redis_timeseries.utility.Utility;

public class FeedInfo implements IBaseStruct, Serializable {

	private static final Logger LOGGER = LogManager.getLogger();

	static final long serialVersionUID = 1l;

	private int exchange = 0;
	private int scripCode = 0;
	private long ltt = 0;
	private Date lttDate;
	private float ltp = 0.00f;
	private long qty = 0;
	private float avgPrice = 0.00f;
	private float bidPrice = 0.00f;
	private long bidQty = 0;
	private float offPrice = 0.00f;
	private long offQty = 0;
	private float open = 0.00f;
	private float close = 0.00f;
	private float high = 0.00f;
	private float low = 0.00f;
	private float yrHigh = 0.00f;
	private float yrLow = 0.00f;
	private float rsChange = 0.00f;
	private float OIChange = 0.00f;
	private int OIDiff = 0;
	private long CurrentOI = 0;
	private long totalBuyQty = 0;
	private long totalSellQty = 0;
	private long oiHigh = 0;
	private long oiLow = 0;
	private float coc = 0.00f;
	private long oiTime = 0;
	private float bidCoc = 0.00f;
	private float offCoc = 0.00f;
	private float perChange = 0.00f;
	private String Index = " ";
	private float upperCkt = 0;
	private float lowerCkt = 0;
	private long lastUpdTime = 0;
	private float spotPrice = 0.00f;
	private float priceDiff = 0.00f;
	private float percentageDiff = 0.00f;
	public long ltq = 0;

	// private static List<String> sortedList = new
	// LinkedList<String>(Arrays.asList(new String[] {"exchange", "scripCode",
	// "ltt", "ltp","qty" }));

	public long getLtq() {
		return ltq;
	}

	public void setLtq(long ltq) {
		this.ltq = ltq;
	}

	public float getSpotPrice() {
		return spotPrice;
	}

	public void setSpotPrice(float spotPrice) {
		this.spotPrice = spotPrice;
	}

	public long getLastUpdTime() {
		return lastUpdTime;
	}

	public void setLastUpdTime(long lastUpdTime) {
		this.lastUpdTime = lastUpdTime;
	}

	public float getUpperCkt() {
		return upperCkt;
	}

	public void setUpperCkt(float upperCkt) {
		this.upperCkt = upperCkt;
	}

	public float getLowerCkt() {
		return lowerCkt;
	}

	public void setLowerCkt(float lowerCkt) {
		this.lowerCkt = lowerCkt;
	}

	public float getBidCoc() {
		return bidCoc;
	}

	public void setBidCoc(float bidCoc) {
		this.bidCoc = bidCoc;
	}

	public float getOffCoc() {
		return offCoc;
	}

	public void setOffCoc(float offCoc) {
		this.offCoc = offCoc;
	}

	public long getOiTime() {
		return oiTime;
	}

	public void setOiTime(long oiTime) {
		this.oiTime = oiTime;
	}

	public long getTotalBuyQty() {
		return totalBuyQty;
	}

	public void setTotalBuyQty(long totalBuyQty) {
		this.totalBuyQty = totalBuyQty;
	}

	public long getTotalSellQty() {
		return totalSellQty;
	}

	public void setTotalSellQty(long totalSellQty) {
		this.totalSellQty = totalSellQty;
	}

	public long getOiHigh() {
		return oiHigh;
	}

	public void setOiHigh(long oiHigh) {
		this.oiHigh = oiHigh;
	}

	public long getOiLow() {
		return oiLow;
	}

	public void setOiLow(long oiLow) {
		this.oiLow = oiLow;
	}

	public float getCoc() {
		return coc;
	}

	public void setCoc(float coc) {
		this.coc = coc;
	}

	public Date getLttDate() {
		return lttDate;
	}

	public void setLttDate(Date lttDate) {
		this.lttDate = lttDate;
	}

	public String getIndex() {
		return Index;
	}

	public void setIndex(String index) {
		Index = index;
		setScripsCode(index);
	}

	public float getRsChange() {
		float out = getLtp() - getClose();
		setRsChange(out);
		return rsChange;
	}

	public void setRsChange(float rsChange) {
		this.rsChange = rsChange;
	}

	public float getOIChange() {
		return OIChange;
	}

	public void setOIChange(float oIChange) {
		OIChange = oIChange;
	}

	public int getOIDiff() {
		return OIDiff;
	}

	public void setOIDiff(int oIDiff) {
		OIDiff = oIDiff;
	}

	public long getCurrentOI() {
		return CurrentOI;
	}

	public void setCurrentOI(long currentOI) {
		CurrentOI = currentOI;
	}

	public String getEndln() {
		return Endln;
	}

	public void setEndln(String endln) {
		Endln = endln;
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
		setLttDate(Utility.getDateFromLong(ltt));
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

	public float getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(float bidPrice) {
		this.bidPrice = bidPrice;
	}

	public long getBidQty() {
		return bidQty;
	}

	public void setBidQty(long bidQty) {
		this.bidQty = bidQty;
	}

	public float getOffPrice() {
		return offPrice;
	}

	public void setOffPrice(float offPrice) {
		this.offPrice = offPrice;
	}

	public long getOffQty() {
		return offQty;
	}

	public void setOffQty(long offQty) {
		this.offQty = offQty;
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

	public float getYrHigh() {
		return yrHigh;
	}

	public void setYrHigh(float yrHigh) {
		if (getHigh() > yrHigh) {
			this.yrHigh = getHigh();
		} else {
			this.yrHigh = yrHigh;
		}
	}

	public float getYrLow() {
		return yrLow;
	}

	public void setYrLow(float yrLow) {
		if (getLow() < yrLow) {
			this.yrLow = getLow();
		} else {
			this.yrLow = yrLow;
		}
	}

	public float getPriceDiff() {
		return priceDiff;
	}

	public void setPriceDiff(float priceDiff) {
		this.priceDiff = priceDiff;
	}

	public float getPercentageDiff() {
		return percentageDiff;
	}

	public void setPercentageDiff(float percentageDiff) {
		this.percentageDiff = percentageDiff;
	}

	public FeedInfo() {
	}

	String Endln = "|";

	public FeedInfo(String feed) {

		// String[] sFeed = feed.split(Endln);
		while (feed.contains(ServiceConstants.DoublePipe))
			feed = feed.replace(ServiceConstants.DoublePipe, ServiceConstants.DoublePipeWGap);
		List<String> sFeed = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(feed, Endln, false);

		while (st.hasMoreElements()) {
			String data = st.nextElement().toString();
			sFeed.add(data);
		}

		/*
		 * if(sFeed.get(5).equalsIgnoreCase("BC")){ // LOGGER.error("Echange"); }
		 */
		Exchanges ex = Exchanges.fromShortString(sFeed.get(5));
		if (ex == null)
			setExchange(0);
		else {
			setExchange(ex.getValue());
			// setLastUpdTime(Utility.getCurrentDateLong());
			setLastUpdTime(Utility.getLongFromDate(sFeed.get(6)) * 1000);
			switch (ex) {
			case NC: {
				// 8,11,12,16..27
				// ScripCode, Qty, LTP, LTT, Avg Price, BidQ, BidP, OffQ, OffP,
				// C,
				// O, H, L, 52WH, 52WL
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 11)));

				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
//				setRsChange(Utility.getFloat(getFeedDataByList(sFeed,15)));
				setRsChange(Utility.getFloat(getFeedDataByList(sFeed, 14)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 16)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 18)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 22)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 23)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 24)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 27)));
				setUpperCkt(Utility.getFloat(getFeedDataByList(sFeed, 28)));
				setLowerCkt(Utility.getFloat(getFeedDataByList(sFeed, 29)));
//				if(getScripCode()==6400||getScripCode()==11536){
//					LOGGER.info(feed+"------"+toString());
//				}

			}
				break;
			case NF: {

				// 8,11,12,16..25
				// ScripCode, Qty, LTP, LTT, Avg Price, BidQ, BidP, OffQ, OffP,
				// C,
				// O, H, L, (52WH, 52WL - Not Available)

				// LOGGER.error(sFeed.toString());
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 11)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
//				setRsChange(Utility.getFloat(getFeedDataByList(sFeed,15)));
				setRsChange(Utility.getFloat(getFeedDataByList(sFeed, 14)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 16)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 18)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 22)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 23)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 24)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 30)));
				setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setOiLow(Utility.getLong(getFeedDataByList(sFeed, 32)));
				setOIChange(Utility.getFloat(getFeedDataByList(sFeed, 37)));
				setOIDiff(Utility.getInt(getFeedDataByList(sFeed, 36)));
				setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 38)));
				setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 39)));
				setOiTime(Utility.getLongFromOITime(getFeedDataByList(sFeed, 29)));
				setBidCoc(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setOffCoc(Utility.getFloat(getFeedDataByList(sFeed, 27)));

				// setYrHigh(Utility.getFloat(getFeedDataByList(sFeed,26)));
				// setYrLow(Utility.getFloat(getFeedDataByList(sFeed,27)));
			}
				break;
			case RN: {
				// 8,11,12,16..25
				// ScripCode, Qty, LTP, LTT, Avg Price, BidQ, BidP, OffQ, OffP,
				// C,
				// O, H, L, (52WH, 52WL - Not Available)
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 11)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
//				setRsChange(Utility.getFloat(getFeedDataByList(sFeed,15)));
				setRsChange(Utility.getFloat(getFeedDataByList(sFeed, 14)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 16)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 18)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 22)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 23)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 24)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 30)));
				setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setOiLow(Utility.getLong(getFeedDataByList(sFeed, 32)));
				setOIChange(Utility.getFloat(getFeedDataByList(sFeed, 37)));
				setOIDiff(Utility.getInt(getFeedDataByList(sFeed, 36)));
				setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 38)));
				setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 39)));
				setOiTime(Utility.getLongFromOITime(getFeedDataByList(sFeed, 29)));
				setBidCoc(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setOffCoc(Utility.getFloat(getFeedDataByList(sFeed, 27)));
				// setYrHigh(Utility.getFloat(getFeedDataByList(sFeed,26)));
				// setYrLow(Utility.getFloat(getFeedDataByList(sFeed,27)));
			}
				break;
			case BC: {
				// 6,8..12,14,17,26..30,32,33
				// LTT, ScripCode, O, C, H, L, Qty, LTP, Avg Price, BidP, BidQ,
				// OffP, OffQ, 52WH, 52WL
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 6)));
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 9)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 10)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 11)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 12)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 16)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 27)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 28)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 29)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 30)));
				setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 32)));
				setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 33)));

				setUpperCkt(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setLowerCkt(Utility.getFloat(getFeedDataByList(sFeed, 24)));

				// LOGGER.error("FeedInfo "+getExchange()+getScripCode());

			}
				break;

			case BF: {
				if (Boolean.parseBoolean(PropertyContainer.getInstance().config.get("IS_NEW_FEED_STRUCT"))) {
					setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 6)));
					setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
					setOpen(Utility.getFloat(getFeedDataByList(sFeed, 9)));
					setClose(Utility.getFloat(getFeedDataByList(sFeed, 10)));
					setHigh(Utility.getFloat(getFeedDataByList(sFeed, 11)));
					setLow(Utility.getFloat(getFeedDataByList(sFeed, 12)));
					setQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
					setLtq(Utility.getLong(getFeedDataByList(sFeed, 16)));
					setLtp(Utility.getFloat(getFeedDataByList(sFeed, 17)));
					setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 26)));
					setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 27)));
					setBidQty(Utility.getLong(getFeedDataByList(sFeed, 28)));
					setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 29)));
					setOffQty(Utility.getLong(getFeedDataByList(sFeed, 30)));
					setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 32)));
					setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 33)));
					setUpperCkt(Utility.getFloat(getFeedDataByList(sFeed, 25)));
					setLowerCkt(Utility.getFloat(getFeedDataByList(sFeed, 24)));
					setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 18)));
					setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 19)));
					setPriceDiff(Utility.getFloat(getFeedDataByList(sFeed, 37)));
					setPerChange(Utility.getFloat(getFeedDataByList(sFeed, 38)));
					setSpotPrice(Utility.getFloat(getFeedDataByList(sFeed, 39)));
					setBidCoc(Utility.getFloat(getFeedDataByList(sFeed, 40)));
					setOffCoc(Utility.getFloat(getFeedDataByList(sFeed, 41)));

					setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 43)));
					setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 44)));
					setOiLow(Utility.getLong(getFeedDataByList(sFeed, 45)));
					setOIDiff(Utility.getInt(getFeedDataByList(sFeed, 47)));
					setOIChange(Utility.getFloat(getFeedDataByList(sFeed, 48))); // percentage change
				} else {
					setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 6)));
					setScripCode(Utility.getInt(getFeedDataByList(sFeed, 8)));
					setOpen(Utility.getFloat(getFeedDataByList(sFeed, 9)));
					setClose(Utility.getFloat(getFeedDataByList(sFeed, 10)));
					setHigh(Utility.getFloat(getFeedDataByList(sFeed, 11)));
					setLow(Utility.getFloat(getFeedDataByList(sFeed, 12)));
					setQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
					setLtq(Utility.getLong(getFeedDataByList(sFeed, 16)));
					setLtp(Utility.getFloat(getFeedDataByList(sFeed, 17)));
					setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 26)));
					setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 27)));
					setBidQty(Utility.getLong(getFeedDataByList(sFeed, 28)));
					setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 29)));
					setOffQty(Utility.getLong(getFeedDataByList(sFeed, 30)));
					setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 32)));
					setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 33)));
					setUpperCkt(Utility.getFloat(getFeedDataByList(sFeed, 25)));
					setLowerCkt(Utility.getFloat(getFeedDataByList(sFeed, 24)));
				}

			}
				break;
			case MX: {
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 9)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 11)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 13)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 16)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 18)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 28)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 29)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 30)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 35)));
				setOiLow(Utility.getLong(getFeedDataByList(sFeed, 36)));
				setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
				setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setOIChange(Utility.getFloat(getFeedDataByList(sFeed, 39)));
				setOIDiff(Utility.getInt(getFeedDataByList(sFeed, 38)));
				setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 23)));
				setPriceRange(getFeedDataByList(sFeed, 24));

			}
				break;
			case RM: {
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 9)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 11)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 13)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 16)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 18)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 28)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 29)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 30)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setRsChange(Utility.getFloat(getFeedDataByList(sFeed, 15)));
				setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 23)));
				setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setOiLow(Utility.getLong(getFeedDataByList(sFeed, 32)));
				// setOIChange(Utility.getFloat(getFeedDataByList(sFeed,37)));
				// setOIDiff(Utility.getFloat(getFeedDataByList(sFeed,36)));
				setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
				setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setOiTime(Utility.getLongFromOITime(getFeedDataByList(sFeed, 29)));
				setBidCoc(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setOffCoc(Utility.getFloat(getFeedDataByList(sFeed, 27)));
				setPriceRange(getFeedDataByList(sFeed, 24));

			}
				break;
			case NX: {
				setScripCode(Utility.getInt(getFeedDataByList(sFeed, 9)));
				setLtt(Utility.getLongFromDate(getFeedDataByList(sFeed, 11)));
				setLtp(Utility.getFloat(getFeedDataByList(sFeed, 12)));
				setLtq(Utility.getLong(getFeedDataByList(sFeed, 13)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 16)));
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 17)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 18)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 19)));
				setQty(Utility.getLong(getFeedDataByList(sFeed, 20)));
				setAvgPrice(Utility.getFloat(getFeedDataByList(sFeed, 21)));
				setYrHigh(Utility.getFloat(getFeedDataByList(sFeed, 25)));
				setYrLow(Utility.getFloat(getFeedDataByList(sFeed, 26)));
				setBidPrice(Utility.getFloat(getFeedDataByList(sFeed, 28)));
				setBidQty(Utility.getLong(getFeedDataByList(sFeed, 29)));
				setOffPrice(Utility.getFloat(getFeedDataByList(sFeed, 30)));
				setOffQty(Utility.getLong(getFeedDataByList(sFeed, 31)));
				setOiHigh(Utility.getLong(getFeedDataByList(sFeed, 35)));
				setOiLow(Utility.getLong(getFeedDataByList(sFeed, 36)));
				setTotalBuyQty(Utility.getLong(getFeedDataByList(sFeed, 14)));
				setTotalSellQty(Utility.getLong(getFeedDataByList(sFeed, 15)));
				setOIChange(Utility.getFloat(getFeedDataByList(sFeed, 39)));
				setOIDiff(Utility.getInt(getFeedDataByList(sFeed, 38)));
				setCurrentOI(Utility.getLong(getFeedDataByList(sFeed, 23)));
				setPriceRange(getFeedDataByList(sFeed, 24));
			}
				break;
			case WI: {

				setIndex(getFeedDataByList(sFeed, 1).trim());
				setOpen(Utility.getFloat(getFeedDataByList(sFeed, 5)));
				setClose(Utility.getFloat(getFeedDataByList(sFeed, 5)) - Utility.getFloat(getFeedDataByList(sFeed, 9)));
				setHigh(Utility.getFloat(getFeedDataByList(sFeed, 7)));
				setLow(Utility.getFloat(getFeedDataByList(sFeed, 8)));
				// setChange(Utility.getFloat(getFeedDataByList(sFeed,9)));

			}
				break;
			default:
				break;
			}
		}
	}

	public FeedInfo(LTPData ltpData) {

		Exchanges ex = Exchanges.fromShortString(ltpData.getExchange());
		if (ex == null)
			setExchange(0);
		else {
			setExchange(ex.getValue());
			// setLastUpdTime(Utility.getCurrentDateLong());
			switch (ex) {
			case NC: {
				// 8,11,12,16..27
				// ScripCode, Qty, LTP, LTT, Avg Price, BidQ, BidP, OffQ, OffP,
				// C,
				// O, H, L, 52WH, 52WL
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case NF: {

				// 8,11,12,16..25
				// ScripCode, Qty, LTP, LTT, Avg Price, BidQ, BidP, OffQ, OffP,
				// C,
				// O, H, L, (52WH, 52WL - Not Available)

				// LOGGER.error(sFeed.toString());
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case RN: {
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case BC: {
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;

			case BF: {
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case RM: {
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case NX: {
				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			case WI: {

				setScripCode(ltpData.getScripcode());

				setLtp(ltpData.getLtp());
				setLtt(ltpData.getLtt());

			}
				break;
			default:
				break;
			}
		}
	}

//	@Override
//	public String toString() {
//		return "FeedInfo [exchange=" + exchange + ", scripCode=" + scripCode + ", ltt=" + ltt + ", lttDate=" + lttDate
//				+ ", ltp=" + ltp + ", qty=" + qty + ", avgPrice=" + avgPrice + ", bidPrice=" + bidPrice + ", bidQty="
//				+ bidQty + ", offPrice=" + offPrice + ", offQty=" + offQty + ", open=" + open + ", close=" + close
//				+ ", high=" + high + ", low=" + low + ", yrHigh=" + yrHigh + ", yrLow=" + yrLow + ", rsChange="
//				+ rsChange + ", OIChange=" + OIChange + ", OIDiff=" + OIDiff + ", CurrentOI=" + CurrentOI
//				+ ", totalBuyQty=" + totalBuyQty + ", totalSellQty=" + totalSellQty + ", oiHigh=" + oiHigh + ", oiLow="
//				+ oiLow + ", coc=" + coc + ", oiTime=" + oiTime + ", bidCoc=" + bidCoc + ", offCoc=" + offCoc
//				+ ", perChange=" + perChange + ", Index=" + Index + ", upperCkt=" + upperCkt + ", lowerCkt=" + lowerCkt
//				+ ", lastUpdTime=" + lastUpdTime + ", Endln=" + Endln + "]";
//	}

	/*
	 * sendBytes= ArrayUtils.addAll(sendBytes, ByteUtil.getBytes((short)
	 * concatBytes.length)); sendBytes= ArrayUtils.addAll(sendBytes,concatBytes);
	 */
	// LOGGER.error("Length--"+concatBytes.length);

	private void setScripsCode(String data) {
		switch (data) {
		case "AEX":
			setScripCode(1001);
			break;
		case "AORD":
			setScripCode(1002);
			break;
		case "BFX":
			setScripCode(1003);
			break;
		case "CSE":
			setScripCode(1004);
			break;
		case "DJI":
			setScripCode(1005);
			break;
		case "GDAXI":
			setScripCode(1006);
			break;
		case "HSI":
			setScripCode(1007);
			break;
		case "BVSP":
			setScripCode(1008);
			break;
		case "JKSE":
			setScripCode(1009);
			break;
		case "MXX":
			setScripCode(1010);
			break;
		case "N225":
			setScripCode(1011);
			break;
		case "FTSE":
			setScripCode(1012);
			break;
		case "FTMC":
			setScripCode(1013);
			break;
		case "SMSI":
			setScripCode(1014);
			break;
		case "KLSE":
			setScripCode(1015);
			break;
		case "MERV":
			setScripCode(1016);
			break;
		case "NDX":
			setScripCode(1017);
			break;
		case "IXIC":
			setScripCode(1018);
			break;
		case "NZ50":
			setScripCode(1019);
			break;
		case "FCHI":
			setScripCode(1020);
			break;
		case "PSI":
			setScripCode(1021);
			break;
		case "OEX":
			setScripCode(1022);
			break;
		case "GSPC":
			setScripCode(1023);
			break;
		case "IPSA":
			setScripCode(1024);
			break;
		case "KS11":
			setScripCode(1025);
			break;
		case "SSE":
			setScripCode(1026);
			break;
		case "SSI":
			setScripCode(1027);
			break;
		case "STI":
			setScripCode(1028);
			break;
		case "SSMI":
			setScripCode(1029);
			break;
		case "TWII":
			setScripCode(1030);
			break;
		case "TA100":
			setScripCode(1031);
			break;
		case "ATX":
			setScripCode(1032);
			break;

		default:
			setScripCode(0);
			break;
		}

	}

	private String getFeedDataByList(List<String> data, int position) {
		if (data.size() > position) {
			return data.get(position);
		}
		return "";
	}

	public float getPerChange() {
		float out = ((ltp - close) / close) * 100.00f;
		setPerChange(out);
		return perChange;
	}

	public void setPerChange(float perChange) {
		this.perChange = perChange;
	}

	private void setPriceRange(String dataDPR) {
		if (!dataDPR.isEmpty()) {
			String[] ulData = dataDPR.split("-");
			if (ulData.length == 2) {
				setLowerCkt(Utility.getFloat(ulData[0]));
				setUpperCkt(Utility.getFloat(ulData[1]));
			}
		}
	}

	@Override
	public void ByteToStruct(byte[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] StructToByte() {
		// TODO Auto-generated method stub
		return null;
	}

//	public static void main(String[] args) {
//		// FeedInfo info=new FeedInfo("~~~|0|0|0|U|NC|04/19/2018
//		// 12:00:24|7208|22|1|2|591089|1611.90|+|+2.44|1|04/19/2018
//		// 12:00:23|1606.52|41|1611.90|37|1612.95|1573.50|1605.00|1629.00|1587.00|1869.95|1485.45|1730.85|1416.15|04/19/2018
//		// 12:00:18|1|99685.00|94304.00|0|1611.90|###|");
//		FeedInfo info = new FeedInfo(
//				"BF^7208^512455^~~~|0|0|0|U|BF|07/11/2023 13:18:27|7208|512455|451.8|447.95|455.5|447.15|2610|146846|662|20|448.6|20112|55069|C|+|||358.4|537.5|451.17|448.6|488|448.9|16|7/11/2023 13:18:27|459.9|132.45|07/11/2023 13:18:19|20|448.6|0.65|0.15|0|4.89|10.31|17:49:14|705|0|0|240|465|193.75|###");
//		System.out.println(info.getLtp());
//
//	}

}
