package com.redis_timeseries.feed;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.redis_timeseries.utility.ServiceConstants;
import com.redis_timeseries.utility.Utility;

@Component
@Scope(value = "prototype")
public class SocketClient {

	@Autowired
	private Server server;

	private static final Logger LOGGER = LogManager.getLogger();
	// int inBufferSize = 65535;
	// 1000Kb For InBuffer//
	int inBufferSize = 65536 * 10;
	public String Exchange = "";
	private int exchange = 0;
	Thread threadReceive = null;
	Thread threadProcess = null;

	Queue<String> data = new Queue<String>();
	DataInputStream dataInStream = null;
	Socket socket = null;
	public boolean IsConnected = false;

	public String serverAddress = "192.168.55.33"; // "192.168.86.87"; //
	public int port = 9001; // 16001; // 15000; //NC - 16001-2 // NF - 16003-4
							// // RN - 17001-2 // BSE - 8898-9 // COMM - 9001-2
	private long feedUpdatedTime = 0;
	private Date feedDate = new Date();

	public int getExchange() {
		return exchange;
	}

	public void setExchange(int exchange) {
		this.exchange = exchange;
	}

	public long getFeedUpdatedTime() {
		return feedUpdatedTime;
	}

	public void setFeedUpdatedTime(long feedUpdatedTime) {
		this.feedUpdatedTime = feedUpdatedTime;
	}

	Runnable receiveOld = new Runnable() {
		public void run() {
			try {
				InputStream inData = socket.getInputStream();
				dataInStream = new DataInputStream(inData);
				while (IsConnected) {
					try {
						String line = dataInStream.readUTF();
						if (line != null) {
							data.EnQueue(line);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} catch (Exception e) {
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,e.getMessage(),LOGGER);
			}
		}
	};

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	Runnable receive = new Runnable() {
		public void run() {
			try {
				char[] cb = new char[inBufferSize];
				/*
				 * InputStream inData = socket.getInputStream(); dataInStream = new
				 * DataInputStream(inData);
				 */
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (IsConnected) {
					int buffSize = 0;
					try {

						buffSize = inFromServer.read(cb);
						if (buffSize > 0) {
							/*
							 * String line = String.valueOf(cb, 0, buffSize); if (line != null) {
							 */
							setFeedUpdatedTime(new Date().getTime());
							data.EnQueue(String.valueOf(cb, 0, buffSize));
							// setFeedUpdatedTime(feedDate.getTime());
							// }
						} else {
							IsConnected = false;
						}
					} catch (SocketTimeoutException e) {
						// LOGGER.info(e.getMessage());
					} catch (Exception e) {
						// LOGGER.info(e.getMessage());
					}
					// added by saravanan
					if (buffSize > 0) {
						Arrays.fill(cb, 0, buffSize, '\0');
					}
					// cb = new char[inBufferSize];
				}
			} catch (Exception e) {
				// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,e.getMessage(),LOGGER);
			}
		}
	};
	/*
	 * Runnable receive = new Runnable() { public void run() { try { char[] cb = new
	 * char[inBufferSize]; InputStream inData = socket.getInputStream();
	 * dataInStream = new DataInputStream(inData); BufferedReader inFromServer = new
	 * BufferedReader(new InputStreamReader(socket.getInputStream())); while
	 * (IsConnected) { int a =0; try { a = inFromServer.read(cb); if (a > 0) {
	 * String line = String.valueOf(cb, 0, a); if (line != null) {
	 * data.EnQueue(line); } } } catch (SocketTimeoutException e) {
	 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,e.getMessage());
	 * }catch (Exception e) {
	 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,e.getMessage());
	 * } // added by saravanan Arrays.fill(cb, 0, a, '\0'); //cb = new
	 * char[inBufferSize]; } } catch (Exception e) {
	 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,e.getMessage());
	 * } } };
	 */
	Runnable process = new Runnable() {
		public void run() {
			try {
				// String Endln = "!";

				String Partial = null;
				while (IsConnected) {
					String inData = data.DeQueue();
					if (inData != null) {
						if (Partial != null) {
							inData = Partial.concat(inData);
						}

						boolean isPartial = false;
						if (inData.endsWith(ServiceConstants.Endline))
							isPartial = false;
						else
							isPartial = true;

						String[] allData = inData.split(ServiceConstants.Endline);

						for (int i = 0; i < allData.length; i++) {
							if (i == allData.length - 1 && isPartial) {
								// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Partial Data
								// Inside : "
								// +
								// allData[i]); // Omit the Last Data if
								// Partial
							} else {
								FPSDataIn dataIn = new FPSDataIn(allData[i]);

								//
								if (dataIn.IsParsed) {
									/// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.Data,LOGGER);
									/*
									 * if(dataIn.TranCode==5346){
									 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.Exchange+
									 * dataIn.ScripCode, LOGGER); }
									 */
									/*
									 * if(dataIn.ScripCode==26017){
									 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.Exchange+
									 * dataIn.ScripCode); }
									 */
									// Commanding the process & Directly we are pushing to exchange 2017-11-16 by
									// saravanan
									/// server.FeedQueue.EnQueue(dataIn);

									// server.pushIntoExchangeServer(exchange,dataIn); //commented on 2023-08-04 ,
									// to seperate BC and BF.
									server.pushIntoExchangeServer(dataIn);

									/*
									 * if(dataIn.TranCode==5346){
									 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.TranCode+
									 * dataIn.Data, LOGGER); }
									 */
									// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.Exchange+dataIn.ScripCode);
									/*
									 * if(dataIn.Exchange.equalsIgnoreCase("BC")){
									 * DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.
									 * INFO,"BC Exchnage Queue In-"+dataIn.ScripCode
									 * +" Transcode "+dataIn.TranCode); }
									 */

									// if (dataIn.TranCode == 7256) {
									// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,dataIn.TranCode
									// + " " + allData[i]
									// + " Parsed...");
									// }
								}
							}
						}

						if (isPartial == false)
							Partial = null;
						else {
							Partial = allData[allData.length - 1];
							// DFTLogContainer.getInstance().setAppLogByType(Log4jEnum.INFO,"Partial Data
							// --> " +
							// allData[allData.length - 1]);
						}
					}
				}
			} catch (Exception e) {

				// e.printStackTrace();
			}
		}
	};

	public SocketClient() {

	}

	public void setSocketClient(String exchange) {
		Exchange = exchange;
	}

	public boolean Connect() {
		boolean isConnected = false;
		InetSocketAddress endpoint = new InetSocketAddress(serverAddress, port);

		try {
			socket = new Socket();
			socket.setReceiveBufferSize(inBufferSize);
			socket.setSoTimeout(1000 * 30);
			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			socket.connect(endpoint);
			if (socket.isConnected()) {
				isConnected = true;
			}

			// StartProcess();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			isConnected = false;
		} catch (IllegalBlockingModeException e) {
			LOGGER.error(e.getMessage());
			isConnected = false;
		} catch (IllegalArgumentException e) {
			LOGGER.error(e.getMessage());
			isConnected = false;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			isConnected = false;
		}
		IsConnected = isConnected;

		if (isConnected) {
			StartProcess();
			LOGGER.info(Exchange + " Connected " + IsConnected);
		} else {
			LOGGER.info(Exchange + " Not Connected");
		}

		return isConnected;
	}

	public void DisConnect() {
		try {
			LOGGER.info("socket : " + socket + " | socket.isConnected() " + socket.isConnected());
			if (socket != null && socket.isConnected()) {
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
				IsConnected = false;
				LOGGER.info("DIsConnected " + IsConnected);
				// StopProcess();
				StopReceiver();
				StopProcesser();
				if (socket != null) {
					LOGGER.info(Exchange + " Disconnected" + socket.isConnected());
				}
			}
		} catch (IOException e) {
			LOGGER.error("IOException " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getMessage());
		}
	}

	void StartProcess() {

		String name = Exchange + " Receive Thread";
		Thread old = Utility.getThreadByName(name);
		if (old != null) {
			old.interrupt();
			LOGGER.info(Exchange + " Receive Thread Stoped...");
		}
		threadReceive = new Thread(receive);
		threadReceive.setDaemon(true);
		threadReceive.setName(Exchange + " Receive Thread");
		threadReceive.start();

		String name1 = Exchange + " Process Thread";
		Thread old1 = Utility.getThreadByName(name1);
		if (old1 != null) {
			old1.interrupt();
			LOGGER.info(Exchange + " Receive Thread Stoped...");
		}
		threadProcess = new Thread(process);
		threadProcess.setDaemon(true);
		threadProcess.setName(Exchange + " Process Thread");
		threadProcess.start();
	}

//	@SuppressWarnings("deprecation")
//	void StopProcess() {
//
//		threadReceive.stop();
//		threadProcess.stop();
//	}

	void StopReceiver() {
		try {
			LOGGER.info("StopReceiver : " + threadReceive);
			data.clear();
			// threadReceive.stop();
			if (!threadReceive.isInterrupted()) {
				threadReceive.interrupt();
			}
			LOGGER.info("StopReceiver : " + threadReceive + " | " + threadReceive.isInterrupted());
		} catch (Exception e) {
			LOGGER.error("StopReceiver Exception " + e.getMessage());
			e.printStackTrace();
		}
	}

	void StopProcesser() {
		try {
			LOGGER.info("StopProcesser : " + threadProcess);
			data.EnQueue("stop thread");
			// threadProcess.stop();
			if (!threadProcess.isInterrupted()) {
				threadProcess.interrupt();
			}
			LOGGER.info("StopProcesser : " + threadProcess + "| " + threadProcess.isInterrupted());
		} catch (Exception e) {
			LOGGER.error("StopProcesser Exception " + e.getMessage());
			e.printStackTrace();
		}
	}

}
