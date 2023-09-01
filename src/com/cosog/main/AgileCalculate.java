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
import com.cosog.model.AppRunStatusProbeResonanceData;
import com.cosog.thread.DIagramSimulateDataThread;
import com.cosog.thread.ExceptionalDataProcessingThread;
import com.cosog.thread.RPCWellDataSyncThread;
import com.cosog.thread.ThreadPool;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.Config;
import com.cosog.utils.DataModelMap;
import com.cosog.utils.DataProcessingUtils;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class AgileCalculate {
	private static Logger logger=null;
	static{
		logPathConfig();
		logger = Logger.getLogger(AgileCalculate.class.getName());
	}
	@SuppressWarnings({ "static-access", "unused" })
	public static void main(String[] args) {
		DIagramSimulateDataThread dIagramSimulateDataThread=new DIagramSimulateDataThread();
		dIagramSimulateDataThread.start();
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
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
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
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
				wellCount=0;
				rpcCalculateRequestDataList=new ArrayList<RPCCalculateRequestData>();
				if(acStatusProbeResonanceData!=null){
					String sql=DataProcessingUtils.getProductionDataSql(null);
					if(StringManagerUtils.isNotNull(sql)){
						try {
							//判断生产数据表连接配置是否有效，是否和功图数据表连接配置相同
							if( (!DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getProductionTable().getConnectInfo())) || DataRequestConfig.ConnectInfoEquals(dataRequestConfig.getProductionTable().getConnectInfo(), dataRequestConfig.getDiagramTable().getConnectInfo())  ){
								conn=OracleJdbcUtis.getDiagramConnection();//配置无效或者和功图数据表连接配置相同，获取功图数据表连接
							}else{
								conn=OracleJdbcUtis.getProductionDataConnection();
							}
							if(conn!=null){
								pstmt = conn.prepareStatement(sql);
								rs=pstmt.executeQuery();
								while(rs.next()){
									wellCount++;
									try{
										RPCCalculateRequestData rpcCalculateRequestData=new RPCCalculateRequestData();
										rpcCalculateRequestData.init();
										rpcCalculateRequestData.setWellName(rs.getString(2));
										rpcCalculateRequestData.getFluidPVT().setCrudeOilDensity(rs.getFloat(3));
										rpcCalculateRequestData.getFluidPVT().setWaterDensity(rs.getFloat(4));
										rpcCalculateRequestData.getFluidPVT().setNaturalGasRelativeDensity(rs.getFloat(5));
										rpcCalculateRequestData.getFluidPVT().setSaturationPressure(rs.getFloat(6));
										
										rpcCalculateRequestData.getReservoir().setDepth(rs.getFloat(7));
										rpcCalculateRequestData.getReservoir().setTemperature(rs.getFloat(8));
										
										rpcCalculateRequestData.getProduction().setTubingPressure(rs.getFloat(9));
										rpcCalculateRequestData.getProduction().setCasingPressure(rs.getFloat(10));
										rpcCalculateRequestData.getProduction().setWellHeadTemperature(rs.getFloat(11));
										rpcCalculateRequestData.getProduction().setWaterCut(rs.getFloat(12));
										rpcCalculateRequestData.getProduction().setProductionGasOilRatio(rs.getFloat(13));
										rpcCalculateRequestData.getProduction().setProducingfluidLevel(rs.getFloat(14));
										rpcCalculateRequestData.getProduction().setPumpSettingDepth(rs.getFloat(15));
										
										rpcCalculateRequestData.getPump().setBarrelType("组合泵".equals(rs.getString(16))?"L":"H");
										rpcCalculateRequestData.getPump().setPumpGrade(rs.getInt(17));
										rpcCalculateRequestData.getPump().setPumpBoreDiameter( (float)(rs.getInt(18) *0.001) );
										rpcCalculateRequestData.getPump().setPlungerLength(rs.getFloat(19));
										
										RPCCalculateRequestData.EveryTubing everyTubing=new RPCCalculateRequestData.EveryTubing();
										everyTubing.setInsideDiameter( (float)(rs.getInt(20) *0.001) );
										rpcCalculateRequestData.getTubingString().getEveryTubing().add(everyTubing);
										
										RPCCalculateRequestData.EveryCasing everyCasing=new RPCCalculateRequestData.EveryCasing();
										everyCasing.setInsideDiameter( (float)(rs.getInt(21) *0.001) );
										rpcCalculateRequestData.getCasingString().getEveryCasing().add(everyCasing);
										
										float rodStringLength1=0,rodStringLength2=0,rodStringLength3=0,rodStringLength4=0;
										rodStringLength1=rs.getFloat(25);
										rodStringLength2=rs.getFloat(29);
										rodStringLength3=rs.getFloat(33);
										rodStringLength4=rs.getFloat(37);
										if(rodStringLength1>0){
											RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
											everyRod.setGrade(rs.getString(22));
											everyRod.setOutsideDiameter( (float)(rs.getInt(23) *0.001) );
											everyRod.setInsideDiameter( (float)(rs.getInt(24) *0.001) );
											everyRod.setLength(rodStringLength1);
											rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
										}
										if(rodStringLength2>0){
											RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
											everyRod.setGrade(rs.getString(26));
											everyRod.setOutsideDiameter( (float)(rs.getInt(27) *0.001) );
											everyRod.setInsideDiameter( (float)(rs.getInt(28) *0.001) );
											everyRod.setLength(rodStringLength2);
											rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
										}
										if(rodStringLength3>0){
											RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
											everyRod.setGrade(rs.getString(30));
											everyRod.setOutsideDiameter( (float)(rs.getInt(31) *0.001) );
											everyRod.setInsideDiameter( (float)(rs.getInt(32) *0.001) );
											everyRod.setLength(rodStringLength3);
											rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
										}
										if(rodStringLength4>0){
											RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
											everyRod.setGrade(rs.getString(34));
											everyRod.setOutsideDiameter( (float)(rs.getInt(35) *0.001) );
											everyRod.setInsideDiameter( (float)(rs.getInt(36) *0.001) );
											everyRod.setLength(rodStringLength4);
											rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
										}
										
										String crankRotationDirection=rs.getString(38);
										float offsetAngleOfCrank=rs.getFloat(39);
										float balanceWeight1=rs.getInt(40);
										float balanceWeight2=rs.getInt(41);
										float balanceWeight3=rs.getInt(42);
										float balanceWeight4=rs.getInt(43);
										float balanceWeight5=rs.getInt(44);
										float balanceWeight6=rs.getInt(45);
										float balanceWeight7=rs.getInt(46);
										float balanceWeight8=rs.getInt(47);
										
										if(balanceWeight1>0 || balanceWeight2>0 || balanceWeight3>0 || balanceWeight4>0 || balanceWeight1>5 || balanceWeight6>0 || balanceWeight7>0 || balanceWeight8>0){
											rpcCalculateRequestData.getPumpingUnit().setCrankRotationDirection("顺时针".equals(crankRotationDirection)?"Clockwise":"Anticlockwise");
											rpcCalculateRequestData.getPumpingUnit().setOffsetAngleOfCrank(offsetAngleOfCrank);
											if(balanceWeight1>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight1);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight2>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight2);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight3>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight3);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight4>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight4);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight5>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight5);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight6>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight6);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight7>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight7);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											if(balanceWeight8>0){
												RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
												everyBalance.setWeight(balanceWeight8);
												rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
											}
											
											rpcCalculateRequestData.getManualIntervention().setCode(rs.getInt(48));
											rpcCalculateRequestData.getManualIntervention().setNetGrossRatio(rs.getFloat(49));
											rpcCalculateRequestData.getManualIntervention().setNetGrossValue(rs.getFloat(50));
											rpcCalculateRequestData.getManualIntervention().setLevelCorrectValue(rs.getFloat(51));
										}
										rpcCalculateRequestDataList.add(rpcCalculateRequestData);
									}catch (Exception e) {
										e.printStackTrace();
										logger.error("error", e);
									}
								}
								StringManagerUtils.printLog("Traverse the well data,well count:"+rpcCalculateRequestDataList.size());
								logger.info("Traverse the well data,well count:"+rpcCalculateRequestDataList.size());
								if(rpcCalculateRequestDataList.size()>0){
									for(RPCCalculateRequestData rpcCalculateRequestData:rpcCalculateRequestDataList){
										executor.execute(new RPCWellDataSyncThread(rpcCalculateRequestData));
									}
								}
								while (!executor.isCompletedByTaskCount()) {
									try {
										Thread.sleep(1000*1);
									}catch (Exception e) {
										e.printStackTrace();
										logger.error("error", e);
									}
							    }
								//将读取数据时间保存到本地
								Map<String, Object> map = DataModelMap.getMapObject();
								if(map.containsKey("dataReadTimeInfoMap") && map.get("dataReadTimeInfoMap")!=null){
									Map<String,String> dataReadTimeInfoMap=(Map<String, String>) map.get("dataReadTimeInfoMap");
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
								StringManagerUtils.printLog("Production data database connection failure");
								logger.info("Production data database connection failure");
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
							logger.error("error", e1);
							StringManagerUtils.printLog("Failed to query production data,sql:"+sql);
							logger.error("Failed to query production data,sql:"+sql);
						} catch (Exception e1) {
							e1.printStackTrace();
							logger.error("error", e1);
						}finally{
							OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
						}
					}
				}else{
					StringManagerUtils.printLog("No ac program detected.");
					logger.error("No ac program detected.");
				}
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error("error", e);
				}
			
			}while(true);
		}else{
			StringManagerUtils.printLog("The configuration information of the database is incorrect,program exit!");
			logger.info("The configuration information of the database is incorrect,program exit!");
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
