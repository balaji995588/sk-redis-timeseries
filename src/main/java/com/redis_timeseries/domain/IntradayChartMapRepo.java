package com.redis_timeseries.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redis_timeseries.feed.GraphSummerizedTicks;
import com.redis_timeseries.utilityenum.ChartPeriod;

public class IntradayChartMapRepo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger();

	private IntradayChartMap zeroTickIntraday = new IntradayChartMap();
	private IntradayChartMap oneminIntraday = new IntradayChartMap();
	private IntradayChartMap threeminIntraday = new IntradayChartMap();
	private IntradayChartMap fiveminIntraday = new IntradayChartMap();
	private IntradayChartMap tenminIntraday = new IntradayChartMap();
	private IntradayChartMap fifteenminIntraday = new IntradayChartMap();
	private IntradayChartMap thirtyminIntraday = new IntradayChartMap();
	private IntradayChartMap sixtyminIntraday = new IntradayChartMap();
	private IntradayChartMap nintyminIntraday = new IntradayChartMap();
	private IntradayChartMap twohoursminIntraday = new IntradayChartMap();
	private IntradayChartMap threehourIntraday = new IntradayChartMap();
	private IntradayChartMap fourhoursIntraday = new IntradayChartMap();

	public void processIntraChart(GraphSummerizedTicks data) {
		zeroTickIntraday.AddGraphTickNew(data);
		oneminIntraday.AddMinuteTick(data, ChartPeriod.OneMinute.getValue());
		threeminIntraday.AddMinuteTick(data, ChartPeriod.ThreeMinutes.getValue());
		fiveminIntraday.AddMinuteTick(data, ChartPeriod.FiveMinutes.getValue());
		tenminIntraday.AddMinuteTick(data, ChartPeriod.TenMinutes.getValue());
		fifteenminIntraday.AddMinuteTick(data, ChartPeriod.FifteenMinutes.getValue());
		thirtyminIntraday.AddMinuteTick(data, ChartPeriod.ThirtyMinutes.getValue());
		sixtyminIntraday.AddMinuteTick(data, ChartPeriod.SixtyMinutes.getValue());
		nintyminIntraday.AddMinuteTick(data, ChartPeriod.NinetyMinutes.getValue());
		twohoursminIntraday.AddMinuteTick(data, ChartPeriod.TwoHours.getValue());
		threehourIntraday.AddMinuteTick(data, ChartPeriod.ThreeHours.getValue());
		fourhoursIntraday.AddMinuteTick(data, ChartPeriod.FourHours.getValue());
	}

	/*
	 * public List<GraphSummerizedTicks> getChartbyInterval(int interval,long
	 * startindex, long endindex) {
	 * 
	 * ChartPeriod period = ChartPeriod.fromInteger(interval);
	 * List<GraphSummerizedTicks> minutesData = null;
	 * 
	 * switch(period) {
	 * 
	 * case OneMinute: minutesData =
	 * oneminIntraday.getMinDataList(startindex,endindex); break; case ThreeMinutes:
	 * minutesData = threeminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case FiveMinutes: minutesData =
	 * fiveminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case TenMinutes: minutesData =
	 * tenminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case FifteenMinutes: minutesData =
	 * fifteenminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case ThirtyMinutes: minutesData =
	 * thirtyminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case SixtyMinutes: minutesData =
	 * sixtyminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case NinetyMinutes: minutesData =
	 * nintyminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case TwoHours: minutesData =
	 * twohoursminIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case ThreeHours: minutesData =
	 * threehourIntraday.getMinDataList(startindex,endindex);
	 * 
	 * break; case FourHours: minutesData =
	 * fourhoursIntraday.getMinDataList(startindex,endindex); break; }
	 * 
	 * return minutesData; }
	 */

	/*
	 * public List<GraphSummerizedTicks> getTickData(int Scripcode,long startindex,
	 * long endindex) {
	 * 
	 * 
	 * lst = zeroTickIntraday.graphticks.get(scripCode).stream().filter(cData ->
	 * cData.getLtt() > start) .collect(Collectors.toList());
	 * 
	 * ChartPeriod period = ChartPeriod.fromInteger(interval); List<ChartData>
	 * minutesData = null;
	 * 
	 * }
	 */

	public List<GraphSummerizedTicks> getTickData(long startindex) {
		return zeroTickIntraday.getTickData(startindex);
	}

	public List<GraphSummerizedTicks> getTickData(long startIndex, long endIndex) {
		return zeroTickIntraday.getTickData(startIndex, endIndex);
	}

	public List<ChartData> getChartbyInterval(int interval, long startindex, long endindex) {
		long starts = new Date().getTime();
		ChartPeriod period = ChartPeriod.fromInteger(interval);
		List<ChartData> minutesData = null;
		switch (period) {
		case OneMinute:
			minutesData = oneminIntraday.getMinDataList(startindex, endindex);
			break;
		case ThreeMinutes:
			minutesData = threeminIntraday.getMinDataList(startindex, endindex);

			break;
		case FiveMinutes:
			minutesData = fiveminIntraday.getMinDataList(startindex, endindex);

			break;
		case TenMinutes:
			minutesData = tenminIntraday.getMinDataList(startindex, endindex);

			break;
		case FifteenMinutes:
			minutesData = fifteenminIntraday.getMinDataList(startindex, endindex);

			break;
		case ThirtyMinutes:
			minutesData = thirtyminIntraday.getMinDataList(startindex, endindex);

			break;
		case SixtyMinutes:
			minutesData = sixtyminIntraday.getMinDataList(startindex, endindex);

			break;
		case NinetyMinutes:
			minutesData = nintyminIntraday.getMinDataList(startindex, endindex);

			break;
		case TwoHours:
			minutesData = twohoursminIntraday.getMinDataList(startindex, endindex);

			break;
		case ThreeHours:
			minutesData = threehourIntraday.getMinDataList(startindex, endindex);

			break;
		case FourHours:
			minutesData = fourhoursIntraday.getMinDataList(startindex, endindex);
			break;
		default:
			break;
		}
		long ends = new Date().getTime();
		LOGGER.info("Diff from getChartbyInterval - " + (ends - starts));
		return minutesData;
	}

	public IntradayChartMap getOneminIntraday() {
		return oneminIntraday;
	}

	public void setOneminIntraday(IntradayChartMap oneminIntraday) {
		this.oneminIntraday = oneminIntraday;
	}

	public IntradayChartMap getThreeminIntraday() {
		return threeminIntraday;
	}

	public void setThreeminIntraday(IntradayChartMap threeminIntraday) {
		this.threeminIntraday = threeminIntraday;
	}

	public IntradayChartMap getFiveminIntraday() {
		return fiveminIntraday;
	}

	public void setFiveminIntraday(IntradayChartMap fiveminIntraday) {
		this.fiveminIntraday = fiveminIntraday;
	}

	public IntradayChartMap getTenminIntraday() {
		return tenminIntraday;
	}

	public void setTenminIntraday(IntradayChartMap tenminIntraday) {
		this.tenminIntraday = tenminIntraday;
	}

	public IntradayChartMap getFifteenminIntraday() {
		return fifteenminIntraday;
	}

	public void setFifteenminIntraday(IntradayChartMap fifteenminIntraday) {
		this.fifteenminIntraday = fifteenminIntraday;
	}

	public IntradayChartMap getThirtyminIntraday() {
		return thirtyminIntraday;
	}

	public void setThirtyminIntraday(IntradayChartMap thirtyminIntraday) {
		this.thirtyminIntraday = thirtyminIntraday;
	}

	public IntradayChartMap getSixtyminIntraday() {
		return sixtyminIntraday;
	}

	public void setSixtyminIntraday(IntradayChartMap sixtyminIntraday) {
		this.sixtyminIntraday = sixtyminIntraday;
	}

}
