package com.redis_timeseries.feed;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public SocketClient clientNCP;
	@Autowired
	public SocketClient clientNCS;
	@Autowired
	public SocketClient clientNFP;
	@Autowired
	public SocketClient clientNFS;
	@Autowired
	public SocketClient clientRNP;
	@Autowired
	public SocketClient clientRNS;
	@Autowired
	public SocketClient clientBSEP;
	@Autowired
	public SocketClient clientBSES;
	@Autowired
	public SocketClient clientCOMMP;
	@Autowired
	public SocketClient clientCOMMS;
	@Autowired
	public SocketClient clientRMP;
	@Autowired
	public SocketClient clientRMS;
	@Autowired
	public SocketClient clientBFP;
	@Autowired
	public SocketClient clientBFS;

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

	public boolean isNCServerFlag() {
		// NCServerFlag=clientNCP.IsConnected;
		return clientNCP.IsConnected;
	}

	private Boolean startNC(Boolean start, String ip, int port, int sport) {
		Boolean result = false;
		if (start == true && isNCServerFlag() == false) {
			// setIP(ip);
			clientNCP.setServerAddress(ip);
			clientNCS.setServerAddress(ip);
			clientNCP.setPort(port);
			clientNCS.setPort(sport);
			result = clientNCP.Connect();
			if (!result) {
				return result;
			}
			result = clientNCS.Connect();
			if (!result) {
				return result;
			}
		} else {
			if (start == false && isNCServerFlag() == true) {
				clientNCP.DisConnect();
				clientNCS.DisConnect();
				result = false;
			}
		}
		return result;
	}

	private boolean NCServerFlag = false;
	private boolean NFServerFlag = false;
	private boolean RNServerFlag = false;
	private boolean BSEServerFlag = false;
	private boolean COMMServerFlag = false;
	private boolean ACKServerFlag = false;
	private boolean ServerFlag = false;
	private boolean RMServerFlag = false;
	private boolean priceAlertServer = false;
	private boolean BFServerFlag = false;

	public boolean isPriceAlertServer() {
		return priceAlertServer;
	}

	public void setPriceAlertServer(boolean priceAlertServer) {
		this.priceAlertServer = priceAlertServer;
	}

	public void setNCServerFlag(boolean nCServerFlag) {
		this.NCServerFlag = nCServerFlag;
	}

	public boolean isNFServerFlag() {
		return NFServerFlag;
	}

	public void setNFServerFlag(boolean nFServerFlag) {
		this.NFServerFlag = nFServerFlag;
	}

	public boolean isRNServerFlag() {
		return RNServerFlag;
	}

	public void setRNServerFlag(boolean rNServerFlag) {
		this.RNServerFlag = rNServerFlag;
	}

	public boolean isBSEServerFlag() {
		return BSEServerFlag;
	}

	public void setBSEServerFlag(boolean bSEServerFlag) {
		this.BSEServerFlag = bSEServerFlag;
	}

	public boolean isCOMMServerFlag() {
		return COMMServerFlag;
	}

	public void setCOMMServerFlag(boolean cOMMServerFlag) {
		this.COMMServerFlag = cOMMServerFlag;
	}

	public boolean isACKServerFlag() {
		return ACKServerFlag;
	}

	public void setACKServerFlag(boolean aCKServerFlag) {
		this.ACKServerFlag = aCKServerFlag;
	}

	public boolean isServerFlag() {
		return ServerFlag;
	}

	public void setServerFlag(boolean serverFlag) {
		this.ServerFlag = serverFlag;
	}

	public boolean isRMServerFlag() {
		return RMServerFlag;
	}

	public void setRMServerFlag(boolean rMServerFlag) {
		this.RMServerFlag = rMServerFlag;
	}

	public boolean isBFServerFlag() {
		return BFServerFlag;
	}

	public void setBFServerFlag(boolean bFServerFlag) {
		this.BFServerFlag = bFServerFlag;
	}

	public Boolean Start(String exchange, Boolean start, String ip, int port, int sport) {
		boolean result;
		switch (exchange) {
		case "NC":
			result = startNC(start, ip, port, sport);
			setNCServerFlag(result);
			return result;
		case "NF":
			result = startNF(start, ip, port, sport);
			setNFServerFlag(result);
			// return startNF(start);
			return result;
		case "RN":
			result = startRN(start, ip, port, sport);
			setRNServerFlag(result);
			// return startRN(start);
			return result;
		case "BSE":
			result = startBSE(start, ip, port, sport);
			setBSEServerFlag(result);
			// return startBSE(start);
			return result;
		case "BF":
			result = startBF(start, ip, port, sport);
			setBFServerFlag(result);
			// return startNF(start);
			return result;
		case "RM":
			result = startRM(start, ip, port, sport);
			setRMServerFlag(result);
			// return startBSE(start);
			return result;
		case "COMM":
			result = startCOMM(start, ip, port, sport);
			setCOMMServerFlag(result);
			// return startCOMM(start);
			return result;

		default:
			break;
		}
		return false;
	}

	private Boolean startBF(Boolean start, String ip, int port, int sport) {
		Boolean result = false;

		if (start == true && isNFServerFlag() == false) {

			/*
			 * clientNFP.serverAddress = ServerConfig.NFIP; clientNFS.serverAddress =
			 * ServerConfig.NFIP;
			 * 
			 * clientNFP.port = ServerConfig.NFPPort; clientNFS.port = ServerConfig.NFSPort;
			 */
			// setIP(ip);
			clientBFP.setServerAddress(ip);
			clientBFS.setServerAddress(ip);
			clientBFP.setPort(port);
			clientBFS.setPort(sport);

			result = clientBFP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientBFS.Connect();
			if (result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}
		} else {
			if (start == false && isBFServerFlag() == true) {
				clientBFP.DisConnect();
				clientBFS.DisConnect();
				result = false;
			}
		}
		return result;
	}

	private Boolean startBSE(Boolean start, String ip, int port, int sport) {
		Boolean result = false;

		if (start == true && isBSEServerFlag() == false) {
			/*
			 * clientBSEP.serverAddress = ServerConfig.BSEIP; clientBSES.serverAddress =
			 * ServerConfig.BSEIP;
			 * 
			 * clientBSEP.port = ServerConfig.BSEPPort; clientBSES.port =
			 * ServerConfig.BSESPort;
			 */
			// setIP(ip);
			clientBSEP.setServerAddress(ip);
			clientBSES.setServerAddress(ip);
			clientBSEP.setPort(port);
			clientBSES.setPort(sport);

			result = clientBSEP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientBSES.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

		} else {
			if (start == false && isBSEServerFlag() == true) {
				clientBSEP.DisConnect();
				clientBSES.DisConnect();
				result = false;
			}
		}

		return result;
	}

	private Boolean startCOMM(Boolean start, String ip, int port, int sport) {
		Boolean result = false;

		if (start == true && isCOMMServerFlag() == false) {

			/*
			 * clientCOMMP.serverAddress = ServerConfig.COMMIP; clientCOMMS.serverAddress =
			 * ServerConfig.COMMIP;
			 * 
			 * clientCOMMP.port = ServerConfig.COMMPPort; clientCOMMS.port =
			 * ServerConfig.COMMSPort;
			 */
			// setIP(ip);
			clientCOMMP.setServerAddress(ip);
			clientCOMMS.setServerAddress(ip);
			clientCOMMP.setPort(port);
			clientCOMMS.setPort(sport);

			result = clientCOMMP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientCOMMS.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}
		} else {
			if (start == false && isCOMMServerFlag() == true) {
				clientCOMMP.DisConnect();
				clientCOMMS.DisConnect();
				result = false;
			}
		}
		return result;
	}

	private Boolean startRN(Boolean start, String ip, int port, int sport) {

		Boolean result = false;

		if (start == true && isRNServerFlag() == false) {

			/*
			 * clientRNP.serverAddress = ServerConfig.RNIP; clientRNS.serverAddress =
			 * ServerConfig.RNIP;
			 * 
			 * clientRNP.port = ServerConfig.RNPPort; clientRNS.port = ServerConfig.RNSPort;
			 */
			// setIP(ip);
			clientRNP.setServerAddress(ip);
			clientRNS.setServerAddress(ip);
			clientRNP.setPort(port);
			clientRNS.setPort(sport);

			result = clientRNP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientRNS.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}
		} else {
			if (start == false && isRNServerFlag() == true) {
				clientRNP.DisConnect();
				clientRNS.DisConnect();
				result = false;
			}
		}
		return result;
	}

	private Boolean startNF(Boolean start, String ip, int port, int sport) {
		Boolean result = false;

		if (start == true && isNFServerFlag() == false) {

			/*
			 * clientNFP.serverAddress = ServerConfig.NFIP; clientNFS.serverAddress =
			 * ServerConfig.NFIP;
			 * 
			 * clientNFP.port = ServerConfig.NFPPort; clientNFS.port = ServerConfig.NFSPort;
			 */
			// setIP(ip);
			clientNFP.setServerAddress(ip);
			clientNFS.setServerAddress(ip);
			clientNFP.setPort(port);
			clientNFS.setPort(sport);

			result = clientNFP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientNFS.Connect();
			if (result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}
		} else {
			if (start == false && isNFServerFlag() == true) {
				clientNFP.DisConnect();
				clientNFS.DisConnect();
				result = false;
			}
		}
		return result;
	}

	private Boolean startRM(Boolean start, String ip, int port, int sport) {
		Boolean result = false;
		if (start == true && isRMServerFlag() == false) {
			clientRMP.setServerAddress(ip);
			clientRMS.setServerAddress(ip);
			clientRMP.setPort(port);
			clientRMS.setPort(sport);
			result = clientRMP.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}

			result = clientRMS.Connect();
			if (!result) {
				return result;
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Connected");
			}
		} else {
			if (start == false && isCOMMServerFlag() == true) {
				clientRMP.DisConnect();
				clientRMS.DisConnect();
				result = false;
			}
		}
		return result;
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

	public void pushIntoExchangeServer(FPSDataIn fpsData) {
		Exchanges ex = Exchanges.fromShortString(fpsData.Exchange);
		if (ex != null && fpsData != null) {
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
				if (fpsData.IsParsed && fpsData.Exchange.equalsIgnoreCase("MX")) {
					MXServer.FeedQueue.EnQueue(fpsData);
				} else if (fpsData.IsParsed && fpsData.Exchange.equalsIgnoreCase("NX")) {
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
