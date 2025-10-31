package com.redis_timeseries.utility;


public enum Exchanges {
	
	// 1 -Cash, 2- derivative
	GE(0),NSE(1), BSE(2), MCX(3), NCDEX(4), MF(5), GLOBAL(9), NC(11), NF(12), RN(13), BC(21), BF(22), RB(23), MX(32), RM(33), NX(42), WI(91),NONE(1000),
	// Indices Company Master
	IBC(211),INC(111),IRN(131),IMX(321),INX(421),CM(100),INF(121),HM(101),NewsCAMaster(3016),COMM(30),SETTINGS(1001),CorpAction(1002),MF_AMC(51),
	MF_SCHEME(52),MF_RESEARCH(53),MF_KEYWORD(56),HambMenu(61),SplTradingDays(102), MarketTimings(103),Research(104),
	SplitOrderNF(122),SplitOrderBF(222),SplitOrderMX(322),SplitOrderRN(132),EO(62),SM(106),BTTM(107);	
	
	
	private final int id;

	Exchanges(int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}

	public static Exchanges fromInteger(int x) {
		switch (x) {
		case 1:
			return NSE;
		case 2:
			return BSE;
		case 3:
			return MCX;
		case 4:
			return NCDEX;
		case 5:
			return MF;
		case 11:
			return NC;
		case 12:
			return NF;
		case 13:
			return RN;
		case 21:
			return BC;
		case 22:
			return BF;
		case 23:
			return RB;
		case 31:
			return MX;
		case 32:
			return MX;
		case 33:
			return RM;
		case 41:
			return NX;
		case 42:
			return NX;
		case 91:
			return WI;			
		case 1000:
			return NONE;
			//IBC(211),INC(111),IRN(131),IMX(321),INX(421);
		case 211:
			return IBC;
		case 111:
			return INC;
		case 131:
			return IRN;
		case 321:
			return IMX;
		case 421:
			return INX;
		case 121:
			return INF;
		case 100:
			return CM;
		case 101:
			return HM;
		case 3016:
			return NewsCAMaster;
		case 30:
			return COMM;
		case 1001:
			return SETTINGS;
		case 1002:
			return CorpAction;
		case 51:
			return MF_AMC;	
		case 52:
			return MF_SCHEME;	
		case 53:
			return MF_RESEARCH;
		case 56:
			return MF_KEYWORD;
		case 61:
			return HambMenu;
		case 102:
			return SplTradingDays;
		case 103:
			return MarketTimings;
		case 104:
			return Research;
		case 62:
			return EO;
		case 106:
			return SM;
		case 107:
			return BTTM;
		}
		return null;
	}

	public static Exchanges fromShortString(String s) {
		switch (s) {

		case "NSE":
			return NSE;
		case "BSE":
			return BSE;
		case "MCX":
			return MCX;
		case "NCDEX":
			return NCDEX;
		case "NC":
			return NC;
		case "NF":
			return NF;
		case "RN":
			return RN;
		case "BC":
			return BC;
		case "BF":
			return BF;
		case "RB":
			return RB;
		case "MX":
			return MX;
		case "RM":
			return RM;
		case "NX":
			return NX;
		case "WI":
			return WI;
		case "Research":
			return WI;
		case "SplTradingDays":
			return SplTradingDays;	
		case "MarketTimings":
			return MarketTimings;	
		case "EO":
			return EO;
		case "SM":
			return SM;
		case "BTTM":
			return BTTM;
		}
		return GE;
	}

	public static Exchanges fromLongString(String s) {
		switch (s) {
		case "NSE":
			return NSE;
		case "BSE":
			return BSE;
		case "MCX":
			return MCX;
		case "NCDEX":
			return NCDEX;
		case "NSECASH":
			return NC;
		case "NSEFO":
			return NF;
		case "NSECURR":
			return RN;
		case "BSECASH":
			return BC;
		case "BSEFO":
			return BF;
		case "BSECURR":
			return RB;
		case "MCXFO":
			return MX;
		case "MCXCURR":
			return RM;
		case "NCDEXFO":
			return NX;
		//saravanan  added for Order
		case "NCCURR":
			return RN;
		case "SM":
			return SM;
		case "BTTM":
			return BTTM;
		}
		return null;
	}
	
	public static Exchanges getExchnageFromString(String s) {
		switch (s) {
		case "NSE":
			return NC;
		case "BSE":
			return BC;
		case "MCX":
			return MX;
		case "NCDEX":
			return NX;
		case "NSECASH":
			return NC;
		case "NSEFO":
			return NF;
		case "NSECURR":
			return RN;
		case "BSECASH":
			return BC;
		case "BSEFO":
			return BF;
		case "BSECURR":
			return RB;
		case "MCXFO":
			return MX;
		case "MCXCURR":
			return RM;
		case "NCDEXFO":
			return NX;
		//saravanan  added for Order
		case "NCCURR":
			return RN;			
		case "NC":
			return NC;
		case "BC":
			return BC;
		case "MX":
			return MX;
		case "NX":
			return NX;		
		case "NF":
			return NF;
		case "RN":
			return RN;		
		case "BF":
			return BF;
		case "RB":
			return RB;		
		case "RM":
			return RM;	
		case "SM":
			return SM;
		case "BTTM":
			return BTTM;
		}
		return null;
	}
	
	public static String getExchange(int i) {

		Exchanges ex = Exchanges.fromInteger(i);
		if(ex!=null){
		switch (ex) {
		case NC:
			return "NSE";
		case NF:
			return "NSEFO";
		case RN:
			return "NSECURR";
		case BC:
			return "BSE";
		case BF:
			return "BSEFO";
		case RB:
			return "BSECURR";
		case MX:
			return "MCXFO";
		case RM:
			return "MCXCURR";
		case NX:
			return "NCDEXFO";
		case COMM:
			return "COMM";
		case SM:
			return "SM";
		case BTTM:
			return "BTTM";
		default:
			break;
		}
		}
		return "";
	}
	
	
	public static String getExchangeForReport(int i) {
		Exchanges ex = Exchanges.fromInteger(i);
		switch (ex) {
		case NC:
			return "NSECASH";
		case NF:
			return "NSEFO";
		case RN:
			return "NSECURR";
		case BC:
			return "BSECASH";
		case BF:
			return "BSEFO";
		case RB:
			return "BSECURR";
		case MX:
			return "MCXFO";
		case RM:
			return "MCXCURR";
		case NX:
			return "NCDEXFO";
		case SM:
			return "SM";
		case BTTM:
			return "BTTM";
		default:
			break;
		}
		return "";
	}
	
	
	public static String getExchangeShort(int exchange) {
        String exChange = "";
        switch (exchange) {
            case 5:
                exChange = "MF";
                break;
            case 11:
                exChange = "NC";
                break;
            case 12:
                exChange = "NF";
                break;
            case 13:
                exChange = "RN";
                break;
            case 21:
                exChange = "BC";
                break;
            case 22:
                exChange = "BF";
                break;
            case 32:
                exChange = "MX";
                break;
            case 33:
                exChange = "RM";
                break;
            case 42:
                exChange = "NX";
                break;
            case 51:
                exChange = "51";
                break;
            case 52:
                exChange = "52";
                break;
            case 53:
                exChange = "53";
                break;
            case 56:
                exChange = "56";
                break;
            case 91:
                exChange = "WI";
                break;
            case 106:
            	exChange="SM";
            	break;
            case 107:
            	exChange="BTTM";
            	break;
            
          /*  case 100:
                exChange = "CM";
                break;
            case 101:
                exChange = "HM";
                break;
            case 3016:
                exChange = "NEWS";*/
            default:
                break;
        }
        return exChange;
    }
	
	public static int  getPriceFactorForNewLeapReportData(String exchange) {
		int fact = 2;
		if(exchange.equalsIgnoreCase(getExchangeForReport(RN.getValue()))) {
			fact=2;
		}
		return fact;
	}
}
