package com.redis_timeseries.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.redis_timeseries.utility.PropertyContainer;
import com.redis_timeseries.utility.Utility;

@Component
@Scope(value = "prototype")
public class ExchangeServer {

	int exchange = 0;

	transient public Queue<LTPData> FeedQueue1 = new Queue<LTPData>();

	public Queue<FPSDataIn> FeedQueue = new Queue<FPSDataIn>();

	transient public Queue<FeedInfo> AllFeedInfoQ = new Queue<FeedInfo>();
	transient public Queue<FPSData> AllFeedQ = new Queue<FPSData>();

	transient public Queue<FeedInfo> graphTicksQueue = new Queue<FeedInfo>();

	public ConcurrentMap<Integer, FeedInfo> feedData = new ConcurrentHashMap<Integer, FeedInfo>();

	transient Queue<GraphSummerizedTicks> GraphSummeryTicksQueue = new Queue<GraphSummerizedTicks>();

	List<Thread> feedProcess = new ArrayList<Thread>();
	List<Thread> threadProcess = new ArrayList<Thread>();

	private static final Logger logger = LogManager.getLogger();

	public ExchangeServer(int exchangeCode) {
		this.exchange = exchangeCode;
		Init(exchangeCode);
	}

	public void Init(int exchangeCode) {
		this.exchange = exchangeCode;
		feedData = new ConcurrentHashMap<Integer, FeedInfo>();
		AllFeedInfoQ = new Queue<FeedInfo>();
		AllFeedQ = new Queue<FPSData>();
	}

	Runnable processFPSFeed = new Runnable() {
		@Override
		public void run() {

			try {
				while (true) {
					try {
//						FOR GRPC FEED
//						LTPData ltpData = FeedQueue1.DeQueue();
////						logger.info("Dequeue feed data for exchange {}, {}",ltpData.getExchange(),ltpData.getLtp());
//
//						FeedInfo feed = new FeedInfo(ltpData);
						// LOGGER.info(dataIn.Data);

//						FOR TCP FEED
						FPSDataIn dataIn = FeedQueue.DeQueue();
						switch (dataIn.TranCode) {
						case 7208:
							FeedInfo feed = new FeedInfo(dataIn.Data);
							// LOGGER.info(dataIn.Data);
							putInQ(feed.getExchange(), 7208, feed);
							break;

						case 2010: // graph Data
							GraphSummerizedTicks GraphSummerizedTicks = new GraphSummerizedTicks(dataIn.Exchange,
									dataIn.ScripCode, dataIn.Data);
							putInQ(GraphSummerizedTicks.getExchange(), 2010, GraphSummerizedTicks);
							break;
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
	};

	void putInQ(FeedInfo data) {
		AllFeedInfoQ.EnQueue(data);
	}

	void putInQ(int exchange, int transCode, IBaseStruct data) {
		FPSData fpsData = new FPSData();
		fpsData.Transcode = transCode;
		fpsData.Data = data;
		AllFeedQ.EnQueue(fpsData);

	}

	Runnable processFeedInfo = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					FeedInfo feedInfo = AllFeedInfoQ.DeQueue();
//					logger.info("Dequeue feed data for exchange {}, {}",feedInfo.getExchange(),feedInfo.getLtp());
					feedData.put(feedInfo.getScripCode(), feedInfo);
//					graphTicksQueue.EnQueue(feedInfo);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}

	};

	Runnable processFeed = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					FPSData data = AllFeedQ.DeQueue();
					if (data != null) {
						switch (data.Transcode) {
						case 7208:
							FeedInfo feedInfo = (FeedInfo) data.Data;
							feedData.put(feedInfo.getScripCode(), feedInfo);
							break;
						case 2010: // graph data
							GraphSummerizedTicks GraphSummerizedTicks = (GraphSummerizedTicks) data.Data;
							GraphSummeryTicksQueue.EnQueue(GraphSummerizedTicks);
							break;
						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}

	};

	Runnable processIntradayChart = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					GraphSummerizedTicks data = GraphSummeryTicksQueue.DeQueue();		
					
						if (!intraChartdataRepo.containsKey(data.getScripCode())) {
							IntradayChartMapRepo newData = new IntradayChartMapRepo();
							intraChartdataRepo.put(data.getScripCode(), newData);
						}
						intraChartdataRepo.get(data.getScripCode()).processIntraChart(data);		
					
				} catch (Exception e) {
					DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO, e.getMessage(), LOGGER);
				}
			}
		}
	};
	
//	public ConcurrentMap<Integer, IntradayChartMapRepo> intraChartdataRepo = new ConcurrentHashMap<Integer, IntradayChartMapRepo>();
//
//
//	Runnable processIntradayChart = new Runnable() {
//		@Override
//		public void run() {
//			while (true) {
//				try {
//					FeedInfo data = graphTicksQueue.DeQueue();
//
//					if (!intraChartdataRepo.containsKey(data.getScripCode())) {
//						IntradayChartMapRepo newData = new IntradayChartMapRepo();
//						intraChartdataRepo.put(data.getScripCode(), newData);
//					}
//					intraChartdataRepo.get(data.getScripCode()).processIntraChart(data);
//
//				} catch (Exception e) {
//					DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO, e.getMessage(), LOGGER);
//				} finally {
//				}
//			}
//		}
//	};

	public FeedInfo GetFeedData(int scripCode) {
		FeedInfo info = null;
		if (feedData.containsKey(scripCode)) {
			info = feedData.get(scripCode);
		}
		return info;
	}

	public void Start() {
		startThread();
		logger.info(exchange + " Started...");
	}

	public void startThread() {

		threadProcess = new ArrayList<Thread>();
		int processThread = Integer.parseInt(PropertyContainer.getInstance().config.get("ProcessThread"));
		if (exchange != 11 && exchange != 12) {
			processThread = (processThread - 4) > 0 ? processThread - 4 : 5;
		}

		for (int i = 0; i < processThread; i++) {
			String name = exchange + " Process Daemon Thread " + i + "-"
					+ PropertyContainer.getInstance().config.get("INSTANCE");
			Thread old = Utility.getThreadByName(name);
			if (old != null) {
				if (old.isAlive())
					old.interrupt();
				logger.info(exchange + " Process Daemon Thread Stoped...");
			}
			Thread t = new Thread(processFeed);
			t.setName(exchange + " Process Daemon Thread " + i + "-"
					+ PropertyContainer.getInstance().config.get("INSTANCE"));
			t.setDaemon(true);
			threadProcess.add(t);
		}

		for (Thread thread : threadProcess) {
			try {
				thread.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		feedProcess = new ArrayList<Thread>();
		int feedProcessThread = Integer.parseInt(PropertyContainer.getInstance().config.get("ProcessThread"));
		if (exchange != 11 && exchange != 12) {
			feedProcessThread = (feedProcessThread - 4) > 0 ? feedProcessThread - 4 : 5;
		}

		for (int i = 0; i < feedProcessThread; i++) {
			String name = exchange + " FPS Feed Process Daemon Thread " + i + "-"
					+ PropertyContainer.getInstance().config.get("INSTANCE");
			Thread old = Utility.getThreadByName(name);
			if (old != null) {
				if (old.isAlive())
					old.interrupt();
				logger.info(exchange + " Process Daemon Thread Stoped...");
			}
			Thread t = new Thread(processFPSFeed);
			t.setName(exchange + " FPS Feed Process Daemon Thread " + i + "-"
					+ PropertyContainer.getInstance().config.get("INSTANCE"));
			t.setDaemon(true);
			feedProcess.add(t);
		}
		for (Thread thread : feedProcess) {
			try {
				thread.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public ConcurrentMap<Integer, FeedInfo> getFeedData() {
		return feedData;
	}
}