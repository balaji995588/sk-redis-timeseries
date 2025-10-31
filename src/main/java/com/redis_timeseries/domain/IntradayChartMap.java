package com.redis_timeseries.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redis_timeseries.feed.FeedInfo;
import com.redis_timeseries.feed.GraphSummerizedTicks;
import com.redis_timeseries.utility.Utility;

public class IntradayChartMap implements Serializable {

	private static final long serialVersionUID = 16542870153474572L;
	private static final Logger LOGGER = LogManager.getLogger();
	transient private static Object OBJ_LOCK = new Object();

	// List<IntradayChart> data = new ArrayList<>();
	// List<GraphSummerizedTicks> graphticks = null;
	// List<ChartData> oneMinData = null;

	// List<GraphSummerizedTicks> tickData = Collections.synchronizedList(new
	// ArrayList<GraphSummerizedTicks>());
	// List<ChartData> minuteData = Collections.synchronizedList(new
	// ArrayList<ChartData>());

	 List<GraphSummerizedTicks> tickData = null;
	public ConcurrentMap<Long, GraphSummerizedTicks> tickDataMap = new ConcurrentHashMap<Long, GraphSummerizedTicks>();

	public ConcurrentMap<Long, GraphSummerizedTicks> getTickDataMap() {
		return tickDataMap;
	}

	public void setTickDataMap(ConcurrentMap<Long, GraphSummerizedTicks> tickDataMap) {
		this.tickDataMap = tickDataMap;
	}

	List<ChartData> minuteData = null;

	long lastDuration = 0;
	int interval = 0;

	long exchangeStartTimeinMilliSec = 0;
	long exchangeClosingTimeinMilliSec = 0;

	// public List<GraphSummerizedTicks> getGraphticks() {
	// return graphticks;
	// }
	//
	// public void setGraphticks(List<GraphSummerizedTicks> graphticks) {
	// this.graphticks = graphticks;
	// }

	public IntradayChartMap() {
		this.tickData = new ArrayList<>();
		this.minuteData = new ArrayList<>();
		this.tickData = new CopyOnWriteArrayList<>();
		this.tickDataMap = new ConcurrentHashMap<Long, GraphSummerizedTicks>();
		this.minuteData = new CopyOnWriteArrayList<>();

	}

//	private Object readResolve() {
//        
//        return OBJ_LOCK =  new Object();
//    }

//	public List<GraphSummerizedTicks> getTickData() {
//		return tickData;
//	}
//
//	public void setTickData(List<GraphSummerizedTicks> tickData) {
//		this.tickData = tickData;
//	}

	public List<ChartData> getMinuteData() {
		return minuteData;
	}

	public void setMinuteData(List<ChartData> minuteData) {
		this.minuteData = minuteData;
	}

	// public void Add(IntradayChart chartData) {
	// //data.add(chartData);
	// if (oneMinData.size() == 0) {
	// ChartData cData = new ChartData();
	// cData.setlTime(chartData.getTime());
	// cData.setHighPrice(chartData.getLtp());
	// cData.setLowPrice(chartData.getLtp());
	// cData.setOpenPrice(chartData.getLtp());
	// cData.setClosePrice(chartData.getLtp());
	// cData.setTradeQty(chartData.getQty());
	// oneMinData.add(cData);
	// } else {
	// if (oneMinData.get(oneMinData.size() - 1).MinTime.equals(chartData.MinTime))
	// {
	// ChartData cData = oneMinData.get(oneMinData.size() - 1);
	// cData.setlTime(chartData.getTime());
	// if (cData.getHighPrice() < chartData.getLtp())
	// cData.setHighPrice(chartData.getLtp());
	//
	// if (cData.getLowPrice() > chartData.getLtp())
	// cData.setLowPrice(chartData.getLtp());
	//
	// cData.setClosePrice(chartData.getLtp());
	//
	// cData.setTradeQty(cData.getTradeQty() + chartData.getQty());
	//
	// oneMinData.set(oneMinData.size() - 1, cData);
	// } else {
	// ChartData cData = new ChartData();
	// cData.setlTime(chartData.getTime());
	// cData.setHighPrice(chartData.getLtp());
	// cData.setLowPrice(chartData.getLtp());
	// cData.setOpenPrice(chartData.getLtp());
	// cData.setClosePrice(chartData.getLtp());
	// cData.setTradeQty(chartData.getQty());
	// oneMinData.add(cData);
	// }
	// }
	// }

	/*
	 * public void AddGraphTick(GraphSummerizedTicks chartData) {
	 * graphticks.add(chartData); if (oneMinData.size() == 0) { ChartData cData =
	 * new ChartData(); cData.setlTime(chartData.getLtt());
	 * cData.setHighPrice(chartData.getHigh());
	 * cData.setLowPrice(chartData.getLow());
	 * cData.setOpenPrice(chartData.getOpen());
	 * cData.setClosePrice(chartData.getClose());
	 * cData.setTradeQty(chartData.getQty()); oneMinData.add(cData); } else { if
	 * (oneMinData.get(oneMinData.size() - 1).MinTime.equals(chartData.MinTime)) {
	 * ChartData cData = oneMinData.get(oneMinData.size() - 1);
	 * cData.setlTime(chartData.getLtt()); //new high // if (cData.getHighPrice() <
	 * chartData.getHigh()) // cData.setHighPrice(chartData.getHigh());
	 * cData.setHighPrice(Math.max(cData.getHighPrice(), chartData.getHigh())); //
	 * new low // if (cData.getLowPrice() > chartData.getLow()) //
	 * cData.setLowPrice(chartData.getLow());
	 * cData.setLowPrice(Math.max(cData.getLowPrice(), chartData.getLow()));
	 * cData.setClosePrice(chartData.getClose());
	 * 
	 * cData.setTradeQty(cData.getTradeQty() + chartData.getQty());
	 * 
	 * oneMinData.set(oneMinData.size() - 1, cData); } else { ChartData cData = new
	 * ChartData(); cData.setlTime(chartData.getLtt());
	 * cData.setHighPrice(chartData.getHigh());
	 * cData.setLowPrice(chartData.getLow());
	 * cData.setOpenPrice(chartData.getOpen());
	 * cData.setClosePrice(chartData.getClose());
	 * cData.setTradeQty(chartData.getQty()); oneMinData.add(cData); } } }
	 */

	/*
	 * public List<IntradayChart> getData(long lTime) {
	 * 
	 * List<IntradayChart> res = new ArrayList<IntradayChart>();
	 * 
	 * try { res = graphticks.stream().filter(cData -> cData.getTime() > lTime)
	 * .collect(Collectors.toList()); } catch (Exception ex) { ex.printStackTrace();
	 * }
	 * 
	 * return res; }
	 */

	// public List<IntradayChart> getDataWeb(long startIndex,long endIndex) {
	// List<IntradayChart> res = new ArrayList<IntradayChart>();
	// try {
	// res = data.stream().filter(cData -> cData.getTime() >= startIndex &&
	// cData.getTime() < endIndex)
	// .collect(Collectors.toList());
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return res;
	// }

	/*
	 * @SuppressWarnings("deprecation") public List<ChartData> getData(int interval,
	 * long lTime, int exchange) { List<ChartData> res = null; try {
	 * 
	 * // if (interval <= 1) { // res = getMinData(lTime, 0, 0); // return res; // }
	 * if (interval == 0) { res = getMinData(lTime, 0, 0); return res; } else {
	 * res=new ArrayList<ChartData>(); }
	 * 
	 * Date dtNow=null; if(lTime==0){ dtNow = new
	 * Date(oneMinData.get(0).getlTime()*1000); }else{ dtNow = new Date(); }
	 * 
	 * switch (exchange) { case 11: case 12: case 21: case 22: dtNow.setHours(9);
	 * dtNow.setMinutes(15); //dtNow.setMinutes(0); dtNow.setSeconds(0); break; case
	 * 23: case 33: case 13: case 32: case 42: dtNow.setHours(9);
	 * dtNow.setMinutes(0); dtNow.setSeconds(0); break;
	 * 
	 * default: dtNow.setHours(9); dtNow.setMinutes(0); dtNow.setSeconds(0); break;
	 * }
	 * 
	 * Calendar c = Calendar.getInstance(); c.setTime(dtNow); long startIndex =
	 * c.getTimeInMillis() / 1000; c.add(Calendar.MINUTE, interval); long endIndex =
	 * c.getTimeInMillis() / 1000;
	 * 
	 * Boolean end = false; do { List<ChartData> data = getMinData(startIndex,
	 * endIndex, lTime); if (data!=null&&data.size() > 0) { ChartData convertData =
	 * null; try { float open = data.get(0).getOpenPrice(); float close =
	 * data.get(data.size() - 1).getClosePrice(); long time = data.get(data.size() -
	 * 1).getlTime(); long qty = data.stream().mapToLong(b ->
	 * b.getTradeQty()).sum(); float tradedValue = (float)
	 * data.stream().mapToDouble(b -> b.getTradeValue()).sum(); float low =
	 * Collections.min(data, new Comparator<ChartData>() {
	 * 
	 * @Override public int compare(ChartData lhs, ChartData rhs) { if (lhs != null
	 * && rhs != null) { return Float.compare(lhs.getLowPrice(), rhs.getLowPrice());
	 * } else if (lhs != null) { return 1; } else { return -1; } } }).getLowPrice();
	 * 
	 * float low =
	 * Collections.min(data,Comparator.comparing(l->l.getLowPrice())).getLowPrice();
	 * 
	 * float high = Collections.max(data, new Comparator<ChartData>() {
	 * 
	 * @Override public int compare(ChartData lhs, ChartData rhs) { if (lhs != null
	 * && rhs != null) { return Float.compare(lhs.getHighPrice(),
	 * rhs.getHighPrice()); } else if (lhs != null) { return 1; } else { return -1;
	 * } } }).getHighPrice();
	 * 
	 * float high =
	 * Collections.max(data,Comparator.comparing(h->h.getHighPrice())).getHighPrice(
	 * );
	 * 
	 * convertData = new ChartData(); convertData.setlTime(time);
	 * convertData.setOpenPrice(open); convertData.setHighPrice(high);
	 * convertData.setLowPrice(low); convertData.setClosePrice(close);
	 * convertData.setTradeQty(qty); convertData.setTradeValue(tradedValue);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); convertData = null; }
	 * 
	 * if (convertData != null) { // issue fixed 21 Sep 2015. for // intraday.... if
	 * (convertData.getlTime() > lTime)
	 * if(lTime==0&&res.size()==ServiceConstants.maxChartDataCount){ end = true;
	 * }else{ res.add(convertData); //} }
	 * 
	 * } else { data = getMinData(endIndex, 0, lTime); if (data.size() == 0) { end =
	 * true; } else { LOGGER.info(" getData loop -> Exchange+"
	 * +exchange+" Interval "+interval+" Start Index :"+startIndex+" End Index : "
	 * +endIndex); } }
	 * 
	 * startIndex = c.getTimeInMillis() / 1000; c.add(Calendar.MINUTE, interval);
	 * endIndex = c.getTimeInMillis() / 1000;
	 * 
	 * } while (!end);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } return res; }
	 */

	/*
	 * List<ChartData> getMinData(long startIndex, long endIndex, long lTime) { //
	 * lTime added 21-Sep-2015 List<ChartData> newData = new ArrayList<ChartData>();
	 * 
	 * if(lTime==0){ newData = oneMinData .stream() .filter(cData ->cData.getlTime()
	 * > lTime) .collect(Collectors.toList());
	 * 
	 * }else if (endIndex > 0) { newData = oneMinData.stream().filter(cData ->
	 * cData.getlTime() >= startIndex && cData.getlTime() < endIndex &&
	 * cData.getlTime() > lTime) .collect(Collectors.toList()); } else { newData =
	 * oneMinData .stream() .filter(cData -> cData.getlTime() > startIndex &&
	 * cData.getlTime() > lTime) .collect(Collectors.toList()); } return newData; }
	 */

	/*
	 * public List<ChartData> getDataBySEIdx(int interval, long startIdx,long
	 * endIndex, int exchange) { List<ChartData> res = new ArrayList<ChartData>();
	 * Boolean end = false; Calendar c = Calendar.getInstance();
	 * c.setTime(Utility.getDateFromLong(startIdx)); long tempStartIndex =
	 * c.getTimeInMillis()/1000; c.add(Calendar.MINUTE, interval); long tempEndIndex
	 * = c.getTimeInMillis()/1000;
	 * 
	 * if (interval <= 1) { res = getMinData(tempStartIndex,0,0); return res; } do {
	 * List<ChartData> data = getMinDataIdx(tempStartIndex, tempEndIndex); if
	 * (data.size() > 0) { ChartData convertData = null; try { float open =
	 * data.get(0).getOpenPrice(); float close = data.get(data.size() -
	 * 1).getClosePrice(); long time = data.get(data.size() - 1).getlTime(); long
	 * qty = data.stream().mapToLong(b -> b.getTradeQty()).sum(); float tradedValue
	 * = (float) data.stream().mapToDouble(b -> b.getTradeValue()).sum(); float low
	 * = Collections.min(data,new Comparator<ChartData>() {
	 * 
	 * @Override public int compare(ChartData lhs, ChartData rhs) { if (lhs != null
	 * && rhs != null) { return Float.compare(lhs.getLowPrice(), rhs.getLowPrice());
	 * } else if (lhs != null) { return 1; } else { return -1; } } }).getLowPrice();
	 * float low =
	 * Collections.min(data,Comparator.comparing(l->l.getLowPrice())).getLowPrice();
	 * // float high = Collections.max(data,new Comparator<ChartData>() {
	 * // @Override // public int compare(ChartData lhs, // ChartData rhs) { // if
	 * (lhs != null && rhs != null) { // return Float.compare(lhs.getHighPrice(), //
	 * rhs.getHighPrice()); // } else if (lhs != null) { // return 1; // } else { //
	 * return -1; // } // } // }).getHighPrice(); float high =
	 * Collections.max(data,Comparator.comparing(h->h.getHighPrice())).getHighPrice(
	 * ); convertData = new ChartData(); convertData.setlTime(time);
	 * convertData.setOpenPrice(open); convertData.setHighPrice(high);
	 * convertData.setLowPrice(low); convertData.setClosePrice(close);
	 * convertData.setTradeQty(qty); convertData.setTradeValue(tradedValue); } catch
	 * (Exception ex) { convertData = null; } if (convertData != null) {
	 * res.add(convertData); } } else { end = true; } tempStartIndex = tempEndIndex;
	 * c.add(Calendar.MINUTE, interval); tempEndIndex = c.getTimeInMillis()/1000;
	 * if(tempEndIndex>endIndex){ end = true; } } while (!end);
	 * 
	 * 
	 * return res; }
	 */

	/*
	 * List<ChartData> getMinDataIdx(long startIndex, long endIndex) {
	 * List<ChartData> newData = new ArrayList<ChartData>(); newData =
	 * oneMinData.stream().filter(cData -> cData.getlTime() >= startIndex &&
	 * cData.getlTime() < endIndex).collect(Collectors.toList()); return newData; }
	 * 
	 * public List<GraphSummerizedTicks> GetGraphSummerizedTick(int start) {
	 * List<GraphSummerizedTicks> lst = null; lst = graphticks.stream().filter(cData
	 * -> cData.getLtt() > start) .collect(Collectors.toList()); return lst; }
	 */

	// Added on 2019-06-25

	public void AddMinuteTick(GraphSummerizedTicks tickData, int interval) {

		synchronized (OBJ_LOCK) {
			if (minuteData.size() == 0) {
				// Need to check 9:15 - 33300, 9:00 - 32400 nc closing time : 55800, Com closing
				// : 84600

				// long exchStartingTimeinMilliSec =
				// Utility.getExchangeStartTimeInMilliSec(tickData.getExchange());
				exchangeStartTimeinMilliSec = Utility.getExchangeStartTime(tickData.getExchange());
				if (tickData.getLtt() >= exchangeStartTimeinMilliSec) {

					// exchangeStartTime = Utility.getExchangeTimings(tickData.getExchange());
					exchangeClosingTimeinMilliSec = Utility.getExchangeCloseTimeInMilliSec(tickData.getExchange());

					// lastDuration = ((tickData.getLtt())
					// - ((tickData.getLtt() + (3600 - (exchangeStartTime % 3600))) % (interval *
					// 60))
					// + (interval * 60));

					// lastDuration = (tickData.getLtt()
					// - ((tickData.getLtt() - (3600 - (exchangeStartTime % (interval * 60)))) %
					// (interval * 60)))
					// + (interval * 60);

					lastDuration = (exchangeStartTimeinMilliSec
							+ (((tickData.getLtt() - exchangeStartTimeinMilliSec) / (interval * 60)) * (interval * 60)))
							+ (interval * 60);

					ChartData cData = new ChartData();
					cData.setlTime(tickData.getLtt());
					cData.setHighPrice(tickData.getHigh());
					cData.setLowPrice(tickData.getLow());
					cData.setOpenPrice(tickData.getOpen());
					cData.setClosePrice(tickData.getClose());
					cData.setTradeQty(tickData.getQty());
					minuteData.add(cData);
					// LOGGER.info("Add zero IntradayChartInfo :-" + tickData.getLtt() + " |
					// Interval:- " + interval + " | last duration : " + lastDuration + " |
					// exchangeClosingTimeinMilliSec -"+exchangeClosingTimeinMilliSec);
				}
			} else {
				// for Last candle need to consider all Exchange Timing
				if (tickData.getLtt() < lastDuration || tickData.getLtt() == exchangeClosingTimeinMilliSec) {
					ChartData cData = minuteData.get(minuteData.size() - 1);
					cData.setlTime(tickData.getLtt());
					// new high
					cData.setHighPrice(Math.max(cData.getHighPrice(), tickData.getHigh()));
					// new low
					cData.setLowPrice(Math.min(cData.getLowPrice(), tickData.getLow()));
					cData.setClosePrice(tickData.getClose());
					cData.setTradeQty(cData.getTradeQty() + tickData.getQty());
					// cData.setCurrMinute(chartData.getCurrMinute());
					minuteData.set(minuteData.size() - 1, cData);
					// LOGGER.info("Update IntradayChartInfo :-" + tickData.getLtt() + " |
					// Interval:- " + interval + " | last duration : " + lastDuration);
				} else {
					// lastDuration = ((tickData.getLtt())
					// - ((tickData.getLtt() + (3600 - (exchangeStartTime % 3600))) % (interval *
					// 60))
					// + (interval * 60));
					// lastDuration = (tickData.getLtt()
					// - ((tickData.getLtt() - (3600 - (exchangeStartTime % (interval * 60)))) %
					// (interval * 60)))
					// + (interval * 60);

					lastDuration = (exchangeStartTimeinMilliSec
							+ (((tickData.getLtt() - exchangeStartTimeinMilliSec) / (interval * 60)) * (interval * 60)))
							+ (interval * 60);
					ChartData cData = new ChartData();
					cData.setlTime(tickData.getLtt());
					cData.setHighPrice(tickData.getHigh());
					cData.setLowPrice(tickData.getLow());
					cData.setOpenPrice(tickData.getOpen());
					cData.setClosePrice(tickData.getClose());
					cData.setTradeQty(tickData.getQty());
					minuteData.add(cData);
					// LOGGER.info("Add IntradayChartInfo :-" + tickData.getLtt() + " | Interval :-
					// "+ interval+ " | last duration : " + lastDuration);
				}
			}
		}
	}

//	public void AddGraphTickNew(GraphSummerizedTicks graphtickData) {
//		tickData.add(graphtickData);
//	}

	public void AddGraphTickNew(GraphSummerizedTicks currentTick) {
		// tickData.add(currentTick);
		// Logic implemented as it is TT FeedServer
		synchronized (OBJ_LOCK) {
			if (tickDataMap.containsKey(currentTick.getLtt())) {
				GraphSummerizedTicks prevTick = tickDataMap.get(currentTick.getLtt());
				currentTick.setQty(currentTick.getQty() + prevTick.getQty());
				if (prevTick.getTickType().equalsIgnoreCase("A")) {
					currentTick.setOpen(prevTick.getOpen());
				}
				switch (currentTick.getTickType()) {
				case "LH":
				case "HL":
				case "EL":
				case "EH":
				case "E":
					if (prevTick.getHigh() > currentTick.getHigh()) { // 13:22:21|E|418.05|418.05|418.05|418.05|2|418.82|
						currentTick.setHigh(prevTick.getHigh());
					}
					if (prevTick.getLow() < currentTick.getLow()) {
						currentTick.setLow(prevTick.getLow());
					}
					break;
				case "H":
					if (prevTick.getHigh() > currentTick.getHigh()) {
						currentTick.setHigh(prevTick.getHigh());
					}
					break;
				case "L":
					if (prevTick.getLow() < currentTick.getLow()) {
						currentTick.setLow(prevTick.getLow());
					}
					break;
				default: // C
					switch (prevTick.getTickType()) {
					case "E":
					case "A":
					case "O":
						currentTick.setOpen(prevTick.getOpen());
					case "LH":
					case "HL":
					case "EL":
					case "EH":
						if (prevTick.getHigh() > currentTick.getHigh()) {
							currentTick.setHigh(prevTick.getHigh());
						}
						if (prevTick.getLow() < currentTick.getLow()) {
							currentTick.setLow(prevTick.getLow());
						}
						currentTick.setTickType(prevTick.getTickType());
						break;
					case "H":
						if (prevTick.getHigh() > currentTick.getHigh()) {
							currentTick.setHigh(prevTick.getHigh());
						}
						currentTick.setTickType(prevTick.getTickType());
						break;
					case "L":
						if (prevTick.getLow() < currentTick.getLow()) {
							currentTick.setLow(prevTick.getLow());

						}
						currentTick.setTickType(prevTick.getTickType());
						break;
					}

				}

			}
			tickDataMap.put(currentTick.getLtt(), currentTick);
		}
	}

	List<ChartData> getMinDataList(long startIndex, long endIndex) { // lTime added 21-Sep-2015

		List<ChartData> newData = null;

		if (startIndex == 0) {
			newData = minuteData.stream().collect(Collectors.toList());
			// newData = minuteData.stream().filter(cData -> cData.getlTime() >=
			// startIndex).collect(Collectors.toList());
			// newData =
			// newData.stream().sorted(Comparator.comparingLong(ChartData::getlTime)).collect(Collectors.toList());

		} else if (startIndex > 0 && endIndex == 0) {
			newData = minuteData.stream().filter(cData -> cData.getlTime() >= startIndex).collect(Collectors.toList());
			// newData =
			// newData.stream().sorted(Comparator.comparingLong(ChartData::getlTime)).collect(Collectors.toList());

		} else {
			newData = minuteData.stream().filter(cData -> cData.getlTime() >= startIndex && cData.getlTime() < endIndex)
					.collect(Collectors.toList());
			// newData =
			// newData.stream().sorted(Comparator.comparingLong(ChartData::getlTime)).collect(Collectors.toList());

		}
		return newData;
	}

	public List<GraphSummerizedTicks> getTickData(long startindex) {
		List<GraphSummerizedTicks> newData = null;
		try {
			if (startindex != 0) {
				// newData = tickDataMap.values().stream().filter(cData -> cData.getLtt() >
				// startindex).collect(Collectors.toList());
				newData = tickDataMap.values().stream().filter(cData -> cData.getLtt() > startindex)
						.sorted(Comparator.comparingLong(GraphSummerizedTicks::getLtt)).collect(Collectors.toList());
			} else {
				// newData = tickDataMap.values().stream().collect(Collectors.toList());
				newData = tickDataMap.values().stream().sorted(Comparator.comparingLong(GraphSummerizedTicks::getLtt))
						.collect(Collectors.toList());
			}

		} catch (Exception e) {
			LOGGER.error("Exception in intraday Tick data fetching process | " + e);
		}
		return newData;
	}

	public List<GraphSummerizedTicks> getTickData(long startIndex, long endIndex) {
		List<GraphSummerizedTicks> newData = null;
		try {
			if (startIndex == 0) {
				newData = tickDataMap.values().stream().collect(Collectors.toList());
			} else if (startIndex > 0 && endIndex == 0) {
				newData = tickDataMap.values().stream().filter(cData -> cData.getLtt() > startIndex)
						.collect(Collectors.toList());
			} else {
				newData = tickDataMap.values().stream()
						.filter(cData -> ((cData.getLtt() > startIndex) && (cData.getLtt() < endIndex)))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			LOGGER.error("Exception in intraday Tick data fetching process | " + e);
		}
		return newData;
	}

}
