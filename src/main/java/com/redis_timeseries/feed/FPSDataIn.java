package com.redis_timeseries.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redis_timeseries.utility.ServiceConstants;
import com.redis_timeseries.utility.Utility;

public class FPSDataIn {
	// String charAtln = "^";
	public boolean IsParsed;
	public String Exchange;
	public int TranCode;
	public int ScripCode;
	public String Data;
	List<String> feedData = new ArrayList<String>();

	private static final Logger LOGGER = LogManager.getLogger(FPSDataIn.class);

	public FPSDataIn(String inData) {
		// LOGGER.error(inData);
		// System.out.println("FBS DATA"+inData);
		IsParsed = false;
		try {

			StringTokenizer st = new StringTokenizer(inData, ServiceConstants.CharAt, false);
			// System.out.println(inData);

			while (st.hasMoreElements()) {
				feedData.add(st.nextToken());
			}

			if (inData.startsWith(ServiceConstants.research)) {
				// LOGGER.info("FPS DATA:"+inData);
				if (feedData.size() < 3) {
					Exchange = feedData.get(0);
					TranCode = ServiceConstants.wiTransCode;
					Data = feedData.get(1);
					IsParsed = true;
				} else {
					Exchange = feedData.get(0);
					TranCode = Utility.getInt(feedData.get(1));
					if (TranCode == ServiceConstants.wiTransCode) {
						ScripCode = ServiceConstants.wiTransCode;
					} else {
						ScripCode = Utility.getInt(feedData.get(2));
					}
					Data = feedData.get(3);
					IsParsed = true;

				}
			} else {
				if (feedData.size() > 2) {
					Exchange = feedData.get(0);
					TranCode = Utility.getInt(feedData.get(1));
					ScripCode = Utility.getInt(feedData.get(2));
					Data = feedData.get(3);
					IsParsed = true;
				}
			}
			feedData.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
