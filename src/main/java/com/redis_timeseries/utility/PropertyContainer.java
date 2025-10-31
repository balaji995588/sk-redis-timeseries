package com.redis_timeseries.utility;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyContainer {	
	
	private Properties prop = new Properties();
	private InputStream inputStream;
	private static  PropertyContainer propertyContainer=null;	
	public ConcurrentHashMap<String,String> config;
		
	
	
	private PropertyContainer(){
		//System.out.print("test");
		config=new ConcurrentHashMap<String,String>();			
		setPropValues(config,"config.properties");			
					
	}
	
	private void setPropValues(Map<String,String> map,String propFileName){
		try {					 
			 inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);		 
			   if (inputStream != null) {		  
				  prop.load(inputStream);				  
				  for (final String name: prop.stringPropertyNames())
//					     if(name.equalsIgnoreCase("FO_EXPIREDATE")){
//					    	 map.put(name,String.valueOf(Utility.getExpireDate()));
//					     }else if(name.equalsIgnoreCase("CURR_EXPIREDATE")){
//					    	 map.put(name,String.valueOf(Utility.getExpireDate()));
//					     }else if(name.equalsIgnoreCase("COM_EXPIREDATE")){
//					    	 map.put(name,String.valueOf(Utility.getExpireDate()));
//					     }else if(name.equalsIgnoreCase("CURRENT_TRADING_DATE")){
//					    	 map.put(name,String.valueOf(Utility.getExpireDate()));		 
//					     }else{
//					    	 map.put(name, prop.getProperty(name)); 
//					     }
					  map.put(name, prop.getProperty(name)); 
					     				  			  
			     } else {
			         throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			        }
			   
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	
	public static PropertyContainer getInstance(){		
		if(propertyContainer==null){
			propertyContainer=new PropertyContainer();
		}		
		return propertyContainer;		
	}
	
	
	public void  refreshProperty(){		
		if(propertyContainer!=null){
			propertyContainer=null;
			propertyContainer=new PropertyContainer();
		}		
				
	}
	
	
	
	

}
