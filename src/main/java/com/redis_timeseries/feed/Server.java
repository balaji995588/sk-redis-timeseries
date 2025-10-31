package com.redis_timeseries.feed;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.redis_timeseries.domain.ClosePriceData;
import com.redis_timeseries.utility.Exchanges;

@Component
public class Server {

	private static final Logger logger = LogManager.getLogger();

	public ExchangeServer NCServer;
	public ExchangeServer NFServer;
	public ExchangeServer RNServer;
	public ExchangeServer BCServer;
	public ExchangeServer BFServer;
	public ExchangeServer MXServer;
	public ExchangeServer RMServer;
	public ExchangeServer NXServer;

	private void createExchange() {

		NCServer = new ExchangeServer(Exchanges.NC.getValue());
		NFServer = new ExchangeServer(Exchanges.NF.getValue());
		RNServer = new ExchangeServer(Exchanges.RN.getValue());
		BCServer = new ExchangeServer(Exchanges.BC.getValue());
		BFServer = new ExchangeServer(Exchanges.BF.getValue());
		RMServer = new ExchangeServer(Exchanges.RM.getValue());
		MXServer = new ExchangeServer(Exchanges.MX.getValue());
		NXServer = new ExchangeServer(Exchanges.NX.getValue());

	}

	public void Init() {

		createExchange();

		NCServer.Start();

		NFServer.Start();

		RNServer.Start();

		BCServer.Start();

		BFServer.Start();

		RMServer.Start();

		MXServer.Start();

		NXServer.Start();

	}

	public Server() {
//		NCServer = new ExchangeServer(Exchanges.NC.getValue());
//		NFServer = new ExchangeServer(Exchanges.NF.getValue());
//		RNServer = new ExchangeServer(Exchanges.RN.getValue());
//		BCServer = new ExchangeServer(Exchanges.BC.getValue());
//		BFServer = new ExchangeServer(Exchanges.BF.getValue());
//		RMServer = new ExchangeServer(Exchanges.RM.getValue());
//		MXServer = new ExchangeServer(Exchanges.MX.getValue());
//		NXServer = new ExchangeServer(Exchanges.NX.getValue());
	}

	public void pushIntoExchangeServer(LTPData ltpData) {
		Exchanges ex = Exchanges.fromShortString(ltpData.getExchange());
		if (ex != null && ltpData != null) {
			switch (ex) {
			case NC:
				NCServer.FeedQueue1.EnQueue(ltpData);
				break;
			case NF:
				NFServer.FeedQueue1.EnQueue(ltpData);
				break;
			case RN:
				RNServer.FeedQueue1.EnQueue(ltpData);
				break;
			case BC:
				BCServer.FeedQueue1.EnQueue(ltpData);
				break;
			case BF:
				BFServer.FeedQueue1.EnQueue(ltpData);
				break;
			case MX:
				if (ltpData.getExchange().equalsIgnoreCase("MX")) {
					MXServer.FeedQueue1.EnQueue(ltpData);
				} else if (ltpData.getExchange().equalsIgnoreCase("NX")) {
					NXServer.FeedQueue1.EnQueue(ltpData);
				}
				break;
			case RM:
				RMServer.FeedQueue1.EnQueue(ltpData);
				break;
			case NX:
				NXServer.FeedQueue1.EnQueue(ltpData);
				break;
			case WI:
				NCServer.FeedQueue1.EnQueue(ltpData);
				break;
			default:
				break;
			}
		}
	}
	
	public void pushIntoExchangeServer(FPSDataIn fpsData){
		Exchanges ex = Exchanges.fromShortString(fpsData.Exchange);
		if (ex != null&&fpsData!=null) {
			switch (ex) {
			case NC:
				NCServer.FeedQueue.EnQueue(fpsData);
				break;
			case NF:
				NFServer.FeedQueue.EnQueue(fpsData);				
				break;
			case RN:
				RNServer.FeedQueue.EnQueue(fpsData);				
				break;
			case BC:
				BCServer.FeedQueue.EnQueue(fpsData);
				break;
			case BF:
				BFServer.FeedQueue.EnQueue(fpsData);
				break;
			case MX:				
				if(fpsData.IsParsed&&fpsData.Exchange.equalsIgnoreCase("MX")){
				MXServer.FeedQueue.EnQueue(fpsData);
				}else if(fpsData.IsParsed&&fpsData.Exchange.equalsIgnoreCase("NX")){
				NXServer.FeedQueue.EnQueue(fpsData);
				}
				break;
			case RM:
				RMServer.FeedQueue.EnQueue(fpsData);
				break;
			case NX:
				NXServer.FeedQueue.EnQueue(fpsData);
				break;
			case WI:
				NCServer.FeedQueue.EnQueue(fpsData);
				break;
			default:
				break;
			}
	}
	}

	public void UpdateFeedDataByExch(List<ClosePriceData> data, int exchange) {
		Exchanges exchEnum = Exchanges.fromInteger(exchange);
		switch (exchEnum) {
		case NC:
			changeFeedOHLC(data, NCServer.getFeedData(), Exchanges.NC.getValue(), NCServer);
			break;
		case BC:
			changeFeedOHLC(data, BCServer.getFeedData(), Exchanges.BC.getValue(), BCServer);
			break;
		case NF:
			changeFeedOHLC(data, NFServer.getFeedData(), Exchanges.NF.getValue(), NFServer);
			break;
		case RN:
			changeFeedOHLC(data, RNServer.getFeedData(), Exchanges.RN.getValue(), RNServer);
			break;
		case MX:
			changeFeedOHLC(data, MXServer.getFeedData(), Exchanges.MX.getValue(), MXServer);
			break;
		case BF:
			changeFeedOHLC(data, BFServer.getFeedData(), Exchanges.BF.getValue(), BFServer);
			break;
		}
	}

	private void changeFeedOHLC(List<ClosePriceData> data, ConcurrentMap<Integer, FeedInfo> feedMap, int exchange,
			ExchangeServer exchServer) {
		int feedUpdCount = 0, feedNotFound = 0;
		FeedInfo feedInfo = new FeedInfo();
		try {
			if (feedMap != null && data != null) {
				for (ClosePriceData updFeedData : data) {
					if (feedMap.containsKey(updFeedData.getScripCode())) {
						feedInfo = feedMap.get(updFeedData.getScripCode());

						if (feedInfo != null) {
							feedInfo.setOpen(updFeedData.getOpenPrice());
							feedInfo.setHigh(updFeedData.getHighPrice());
							feedInfo.setLow(updFeedData.getLowPrice());
							feedInfo.setClose(updFeedData.getClosePrice());
							// feedInfo.setLtp(updFeedData.getClosePrice());
							exchServer.getFeedData().put(updFeedData.getScripCode(), feedInfo);
							// LOGGER.info("Previous ClosePriceData updated in Feed obj : " +
							// feedInfo.toString());
							feedUpdCount++;
						} else {
							logger.info("Previous ClosePriceData not updated in Feed obj : " + exchange + " ::> "
									+ updFeedData.toString());
							feedNotFound++;
						}
					} else {
						feedInfo.setScripCode(updFeedData.getScripCode());
						feedInfo.setExchange(exchange);
						feedInfo.setOpen(updFeedData.getOpenPrice());
						feedInfo.setHigh(updFeedData.getHighPrice());
						feedInfo.setLow(updFeedData.getLowPrice());
						feedInfo.setClose(updFeedData.getClosePrice());
						exchServer.getFeedData().put(updFeedData.getScripCode(), feedInfo);
						logger.info("Previous ClosePriceData inserted in Feed obj :: " + exchange + " ::> " + feedInfo);
					}
				}
			}
		} catch (Exception e) {
			logger.error("changeFeedOHLC failed to update :: " + e.getMessage());

		}
		logger.info("Close Price Updated for exchange " + exchange + " Updated Count: " + feedUpdCount
				+ " and Not Updated Count: " + feedNotFound);

	}
	
	public FeedInfo getFeedData(int exchange, int scripCode) {
		FeedInfo feedinfo = null;
		// int scripCode=0;
		Exchanges ex = Exchanges.fromInteger(exchange);
		boolean res;
		switch (ex) {
		case NC:
			if (scripCode > 0) {
				feedinfo = NCServer.GetFeedData(scripCode);
				// For IO need check BSE Feed
				if (feedinfo == null) {
					feedinfo = BCServer.GetFeedData(scripCode);

				}
			}
			break;
		case NF:
			if (scripCode > 0) {
				feedinfo = NFServer.GetFeedData(scripCode);
			}
			break;
		case RN:
			if (scripCode > 0) {
				feedinfo = RNServer.GetFeedData(scripCode);
			}
			break;
		case BC:
			if (scripCode > 0) {
				feedinfo = BCServer.GetFeedData(scripCode);
			}
			break;
		case BF:
			if (scripCode > 0) {
				feedinfo = BFServer.GetFeedData(scripCode);
			}
			break;
		case MX:
			if (scripCode > 0) {
				feedinfo = MXServer.GetFeedData(scripCode);
			}
			break;
		case RM:
			if (scripCode > 0) {
				feedinfo = RMServer.GetFeedData(scripCode);
			}
			break;
		case NX:
			if (scripCode > 0) {
				feedinfo = NXServer.GetFeedData(scripCode);
			}
			break;
		case WI:

			break;
		default:

			break;
		}
		return feedinfo;
	}
}
