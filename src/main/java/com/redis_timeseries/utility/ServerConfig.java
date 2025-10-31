package com.redis_timeseries.utility;


public final class ServerConfig {

	public static int ProcessThread;
	public static int DBConnPoolCount;

	public static String SqlDriver;
	public static String SqlServerURL;
	public static String SqlUserName;
	public static String SqlPassword;

	public static String NCIP;
	public static int NCPPort;
	public static int NCSPort;

	public static String NFIP;
	public static int NFPPort;
	public static int NFSPort;

	public static String RNIP;
	public static int RNPPort;
	public static int RNSPort;

	public static String BSEIP;
	public static int BSEPPort;
	public static int BSESPort;

	public static String COMMIP;
	public static int COMMPPort;
	public static int COMMSPort;

	public static String AuthURL;
	public static String BseCashOrderURL;
	public static String NseCashOrderURL;
	public static String NseFnoOrderURL;
	public static String MCXOrderURL;
	public static String NCDEXOrderURL;
	public static String CommReportURL;
	public static String EqReportURL;
	public static String WatchlistURL;

	public static String AuthServiceURL;
	
	public static String OpenAccountURL;

	public static Boolean IsLoaded = false;

	public static String AckServerIP = "192.168.10.43";
	public static int AckServerPort = 7001;

	public static String DynaAckServerIP = "";
	public static int DynaAckServerPort = 0;

	public static void LoadConfig() {

		try {
			/*Properties prop = new Properties();
			InputStream inputStream = ServerConfig.class.getClassLoader().getResourceAsStream("/config.properties");
			prop.load(inputStream);*/

			SqlDriver = PropertyContainer.getInstance().config.get("driver");
			SqlServerURL = PropertyContainer.getInstance().config.get("url");
			SqlUserName = PropertyContainer.getInstance().config.get("user");
			SqlPassword = PropertyContainer.getInstance().config.get("password");

			NCIP = PropertyContainer.getInstance().config.get("NCIP");
			NCPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("NCPPort"));
			NCSPort = Integer.parseInt(PropertyContainer.getInstance().config.get("NCSPort"));

			NFIP = PropertyContainer.getInstance().config.get("NFIP");
			NFPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("NFPPort"));
			NFSPort = Integer.parseInt(PropertyContainer.getInstance().config.get("NFSPort"));
			RNIP = PropertyContainer.getInstance().config.get("RNIP");
			RNPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("RNPPort"));
			RNSPort = Integer.parseInt(PropertyContainer.getInstance().config.get("RNSPort"));
			BSEIP = PropertyContainer.getInstance().config.get("BSEIP");
			BSEPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("BSEPPort"));
			BSESPort = Integer.parseInt(PropertyContainer.getInstance().config.get("BSESPort"));
			COMMIP = PropertyContainer.getInstance().config.get("COMMIP");
			COMMPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("COMMPPort"));
			COMMSPort = Integer.parseInt(PropertyContainer.getInstance().config.get("COMMSPort"));
			ProcessThread = Integer.parseInt(PropertyContainer.getInstance().config.get("ProcessThread"));
//			DBConnPoolCount = Integer.parseInt(PropertyContainer.getInstance().config.get("DBPoolCount"));
			AuthURL = PropertyContainer.getInstance().config.get("Auth");
			BseCashOrderURL = PropertyContainer.getInstance().config.get("BSEOrder");
			NseCashOrderURL = PropertyContainer.getInstance().config.get("NSEOrder");
			NseFnoOrderURL = PropertyContainer.getInstance().config.get("NSEFOOrder");
			MCXOrderURL = PropertyContainer.getInstance().config.get("MCXFOOrder");
			NCDEXOrderURL = PropertyContainer.getInstance().config.get("NCDEXFOOrder");
			CommReportURL = PropertyContainer.getInstance().config.get("CommStmt");
			EqReportURL = PropertyContainer.getInstance().config.get("EqStmt");
			WatchlistURL = PropertyContainer.getInstance().config.get("Watchlist");
			AuthServiceURL = PropertyContainer.getInstance().config.get("AuthService");
			OpenAccountURL= PropertyContainer.getInstance().config.get("OpenAccount");			
			AckServerIP = PropertyContainer.getInstance().config.get("AckServerIP");
//			AckServerPort = Integer.parseInt(PropertyContainer.getInstance().config.get("AckServerPort"));
			DynaAckServerIP = PropertyContainer.getInstance().config.get("DynaAckServerIP");
//			DynaAckServerPort = Integer.parseInt(PropertyContainer.getInstance().config.get("DynaAckServerPort"));
			IsLoaded = true;
			BSEIP = PropertyContainer.getInstance().config.get("BFIP");
			BSEPPort = Integer.parseInt(PropertyContainer.getInstance().config.get("BFPPort"));
			BSESPort = Integer.parseInt(PropertyContainer.getInstance().config.get("BFSPort"));
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
}
