package com.redis_timeseries.utility;

import java.text.SimpleDateFormat;

public class ServiceConstants {

	public static String CharAt = "^";
	public static String Endline = "!";
	public static String Pipe = "|";
	public static String Comma = ",";
	public static String DoublePipe = "||";
	public static String DoublePipeWGap = "| |";	
	public static String EndElimeter="###";

	public static SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
	public static SimpleDateFormat tsf = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat tf = new SimpleDateFormat("HH:mm");

	public static int maxChartDataCount = 1200;
	
	
	public static String apiKey="2d7203e9fadc3300db4ab8634786df05";
	
	public static String research="Research";
	
	public static int wiTransCode=5346;
	
	public static String StartElimeter="~~~";
	
	public static String dollar="$";
	
	
}

