package com.redis_timeseries.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis_timeseries.feed.Server;
import com.redis_timeseries.utility.CxfResponse;
import com.redis_timeseries.utility.ServerConfig;
import com.redis_timeseries.utilityenum.RServerExchanges;

@Service
public class FeedServerService {

	@Autowired
	private CxfResponse cxfResponse;

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private Server server;

	public CxfResponse startExchangeByName(String exchange, Boolean start, String ip, int port, int sport) {

		logger.info(" ~~~ startExchangeByName -->" + exchange + "&" + start + sport);
		boolean result;
		try {
			if (!ServerConfig.IsLoaded)
				ServerConfig.LoadConfig();
			StringBuilder sb = new StringBuilder();

			switch (exchange) {
			case "NC":
				result = server.Start(RServerExchanges.NC.getValue(), start, ip, port, sport);
				sb.append(server.isNCServerFlag()).append("|").append(server.clientNCP.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;
			case "NF":
				result = server.Start(RServerExchanges.NF.getValue(), start, ip, port, sport);
				sb.append(server.isNFServerFlag()).append("|").append(server.clientNFP.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				// cxfResponse.setMessage(String.valueOf(server.isNFServerFlag()));
				break;
			case "RN":
				result = server.Start(RServerExchanges.RN.getValue(), start, ip, port, sport);
				// cxfResponse.setMessage(String.valueOf(server.isRNServerFlag()));
				sb.append(server.isNFServerFlag()).append("|").append(server.clientRNP.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;
			case "BSE":
				result = server.Start(RServerExchanges.BSE.getValue(), start, ip, port, sport);
				// cxfResponse.setMessage(String.valueOf(server.isBSEServerFlag()));
				sb.append(server.isBSEServerFlag()).append("|").append(server.clientBSES.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;
			case "BF":
				result = server.Start(RServerExchanges.BF.getValue(), start, ip, port, sport);
				// cxfResponse.setMessage(String.valueOf(server.isBSEServerFlag()));
				sb.append(server.isBFServerFlag()).append("|").append(server.clientBFP.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;
			case "RM":
				result = server.Start(RServerExchanges.RM.getValue(), start, ip, port, sport);
				// cxfResponse.setMessage(String.valueOf(server.isBSEServerFlag()));
				sb.append(server.isRMServerFlag()).append("|").append(server.clientRMS.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;
			case "COMM":
				result = server.Start(RServerExchanges.COMM.getValue(), start, ip, port, sport);
				// cxfResponse.setMessage(String.valueOf(server.isCOMMServerFlag()));
				sb.append(server.isCOMMServerFlag()).append("|").append(server.clientCOMMP.getServerAddress());
				cxfResponse.setMessage(sb.toString());
				break;

			}

			cxfResponse.setResCode(0);
			cxfResponse.setMessage("true");

		} catch (Exception e) {
			cxfResponse.setResCode(1);
			cxfResponse.setMessage("false");

		}
		logger.info(" ### startExchangeByName -->" + exchange + "&" + start);
		return cxfResponse;
	}
}
