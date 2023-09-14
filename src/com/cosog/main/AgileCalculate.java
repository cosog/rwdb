package com.cosog.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cosog.model.DataReadTimeInfo;
import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.model.AppRunStatusProbeResonanceData;
import com.cosog.thread.DIagramSimulateDataThread;
import com.cosog.thread.ExceptionalDataProcessingThread;
import com.cosog.thread.RPCWellDataSyncThread;
import com.cosog.thread.ThreadPool;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.Config;
import com.cosog.utils.ConfigFile;
import com.cosog.utils.CounterUtils;
import com.cosog.utils.DataModelMap;
import com.cosog.utils.DataProcessingUtils;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AgileCalculate {
	private static Logger logger=null;
	private static int count=0;
	static{
		logPathConfig();
		logger = Logger.getLogger(AgileCalculate.class.getName());
	}
	@SuppressWarnings({ "static-access", "unused" })
	public static void main(String[] args) {
//		DIagramSimulateDataThread dIagramSimulateDataThread=new DIagramSimulateDataThread();
//		dIagramSimulateDataThread.start();
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		Map<String,String> dataReadTimeInfoMap=MemoryDataUtils.getDataReadTimeInfo();
		MemoryDataUtils.initDiagramCalculateFailureData();
		
//		ExceptionalDataProcessingThread exceptionalDataProcessingThread=new ExceptionalDataProcessingThread();
//		exceptionalDataProcessingThread.start();
		
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo()) 
				
				&& dataRequestConfig.getProductionTable()!=null 
				&& dataRequestConfig.getProductionTable().getTableInfo()!=null
				&& dataRequestConfig.getProductionTable().getTableInfo().getColumns()!=null){
			Gson gson=new Gson();
			StringManagerUtils stringManagerUtils=new StringManagerUtils();
			List<RPCCalculateRequestData> rpcCalculateRequestDataList=null;
			int wellCount=0;
			ThreadPool executor = new ThreadPool("RPCWellDataSyncAndCalThreadPool",
					Config.getInstance().configFile.getThreadPool().getOuterDatabaseSync().getCorePoolSize(), 
					Config.getInstance().configFile.getThreadPool().getOuterDatabaseSync().getMaximumPoolSize(), 
					Config.getInstance().configFile.getThreadPool().getOuterDatabaseSync().getKeepAliveTime(), 
					TimeUnit.SECONDS, 
					Config.getInstance().configFile.getThreadPool().getOuterDatabaseSync().getWattingCount());
			do{
				AppRunStatusProbeResonanceData acStatusProbeResonanceData=CalculateUtils.appProbe("");
				rpcCalculateRequestDataList=new ArrayList<RPCCalculateRequestData>();
				if(acStatusProbeResonanceData!=null){
					String sql=DataProcessingUtils.getProductionDataSql(null);
					if(StringManagerUtils.isNotNull(sql)){
						List<List<Object>> prodList=OracleJdbcUtis.queryProductionData(sql);
						if(prodList!=null && prodList.size()>0){
							for(int i=0;i<prodList.size();i++){
								List<Object> list=prodList.get(i);
								wellCount++;
								RPCCalculateRequestData rpcCalculateRequestData=DataProcessingUtils.getRPCCalculateRequestData(list);
								rpcCalculateRequestDataList.add(rpcCalculateRequestData);
							}
						}
						System.out.println(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")+":Traverse the well data,well count:"+rpcCalculateRequestDataList.size());
						logger.info(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")+":Traverse the well data,well count:"+rpcCalculateRequestDataList.size());
						StringManagerUtils.printLog("Traverse the well data,well count:"+rpcCalculateRequestDataList.size());
						StringManagerUtils.printLogFile(logger, "Traverse the well data,well count:"+rpcCalculateRequestDataList.size(),"info");
						
						
						if(rpcCalculateRequestDataList.size()>0){
							CounterUtils.initCountDownLatch(rpcCalculateRequestDataList.size());
							CounterUtils.reset();//加法计数器清零
							long calculateStartTime=System.nanoTime();
							for(RPCCalculateRequestData rpcCalculateRequestData:rpcCalculateRequestDataList){
								executor.execute(new RPCWellDataSyncThread(rpcCalculateRequestData));
							}
							try {
								CounterUtils.await();//等待所有线程执行完毕
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								StringManagerUtils.printLogFile(logger, "error", e, "error");
							}
							long calculateEndTime=System.nanoTime();
							long sum=CounterUtils.sum();//获取计算的功图数量
							System.out.println(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")+":计算功图数量:"+sum+",用时:"+StringManagerUtils.getTimeDiff(calculateStartTime, calculateEndTime));
						}
						try {
							Thread.sleep(1000*1);
						}catch (Exception e) {
							e.printStackTrace();
							StringManagerUtils.printLogFile(logger, "error", e, "error");
						}
						//将读取数据时间保存到本地
						if(dataReadTimeInfoMap!=null){
							DataReadTimeInfo dataReadTimeInfo=new DataReadTimeInfo();
							dataReadTimeInfo.setWellList(new ArrayList<DataReadTimeInfo.DataReadTime>());
							
							Iterator<Map.Entry<String, String>> iterator = dataReadTimeInfoMap.entrySet().iterator();
							while (iterator.hasNext()) {
								Map.Entry<String, String> entry = iterator.next();
								DataReadTimeInfo.DataReadTime dataReadTime=new DataReadTimeInfo.DataReadTime();
								dataReadTime.setWellName(entry.getKey());
								dataReadTime.setReadTime(entry.getValue());
								dataReadTimeInfo.getWellList().add(dataReadTime);
							}
							String path=stringManagerUtils.getFilePath("timestamp.json","conf/");
							StringManagerUtils.writeFile(path,StringManagerUtils.jsonStringFormat(gson.toJson(dataReadTimeInfo)));
						}
					}
				}else{
					StringManagerUtils.printLog("No ac program detected.");
					StringManagerUtils.printLogFile(logger, "No ac program detected.", "error");
				}
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					StringManagerUtils.printLogFile(logger, "error", e, "error");
				}
			}while(true);
		}else{
			StringManagerUtils.printLog("The configuration information of the database is incorrect,program exit!");
			StringManagerUtils.printLogFile(logger, "The configuration information of the database is incorrect,program exit!","info");
		}
	}
	
	public static class TestThread   extends Thread{
		private String wellName;
		private String data;

		public TestThread(String data,String wellName) {
			super();
			this.data = data;
			this.wellName = wellName;
		}
		public void run(){
			long time1=0,time2=0;
			RPCCalculateResponseData calculateResponseData=null;
			Gson gson = new Gson();
			java.lang.reflect.Type type=null;
			type = new TypeToken<RPCCalculateRequestData>() {}.getType();
			RPCCalculateRequestData calculateRequestData=gson.fromJson(data, type);
			calculateRequestData.setWellName(wellName);
			do{
				calculateRequestData.getFESDiagram().setAcqTime(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				time1=System.nanoTime();
				calculateResponseData=CalculateUtils.fesDiagramCalculate(gson.toJson(calculateRequestData));
				time2=System.nanoTime();
//				System.out.println(calculateRequestData.getWellName()+"单张功图计算时间："+StringManagerUtils.getTimeDiff(time1, time2));
				count++;
			}while(true);
		}
		
	}

	public static void logPathConfig(){
		String path=getFilePath("rwdb.log","logs/");
		System.setProperty ("LOG4GWORKDIR", path);
	}
	
	public static String getFilePath(String index4Str, String path0) {
        String path=Class.class.getClass().getResource("/").getPath();
        int index = path.indexOf(index4Str);
        if (index == -1) {
            index = path.indexOf("WEB-INF");
        }

        if (index == -1) {
            index = path.indexOf("bin");
        }
        
        if(index == -1){
        	path="";
        }else{
        	path = path.substring(0, index);
        }

        if (path.startsWith("zip")) {
            path = path.substring(4);
        } else if (path.startsWith("file")) {
            path = path.substring(5);
        } else if (path.startsWith("jar")) {
            path = path.substring(9);
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        path = path + path0 + index4Str;
        return path;
    }
}
