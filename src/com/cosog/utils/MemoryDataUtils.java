package com.cosog.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cosog.main.AgileCalculate;
import com.cosog.model.DataReadTimeInfo;
import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.DiagramExceptionData;
import com.cosog.model.WorkType;
import com.cosog.utils.ConfigFile.Ac;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MemoryDataUtils {
	private static final Logger logger = Logger.getLogger(AgileCalculate.class.getName());
	
	public static void loadSystemConfig(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		Map<String, Object> map = DataModelMap.getMapObject();
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		String path=stringManagerUtils.getFilePath("conf.json","conf/");
		String data=StringManagerUtils.readFile(path,"utf-8").replaceAll(" ", "");
		type = new TypeToken<ConfigFile>() {}.getType();
		ConfigFile configFile=gson.fromJson(data, type);
		if(configFile==null){
			configFile=new ConfigFile();
		}
		configFile.init();
		map.put("systemConfig", configFile);
	}
	
	public static ConfigFile getSystemConfig(){
		Map<String, Object> map = DataModelMap.getMapObject();
		ConfigFile configFile=(ConfigFile) map.get("systemConfig");
		if(configFile==null){
			loadSystemConfig();
			configFile=(ConfigFile) map.get("systemConfig");
		}
		return configFile;
	}
	
	public static void loadDataReqConfig(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("req.json","conf/");
		String data=StringManagerUtils.readFile(path,"utf-8").replaceAll(" ", "");
		
		type = new TypeToken<DataRequestConfig>() {}.getType();
		DataRequestConfig dataRequestConfig=gson.fromJson(data, type);
		
		Map<String, Object> map = DataModelMap.getMapObject();
		map.put("dataRequestConfig", dataRequestConfig);
		if(dataRequestConfig==null){
			StringManagerUtils.printLog("Parsing the req.json file failed");
			logger.info("Parsing the req.json file failed");
		}
	}
	
	public static DataRequestConfig getDataReqConfig(){
		Map<String, Object> map = DataModelMap.getMapObject();
		DataRequestConfig dataRequestConfig=(DataRequestConfig) map.get("dataRequestConfig");
		if(dataRequestConfig==null){
			loadDataReqConfig();
			dataRequestConfig=(DataRequestConfig) map.get("dataRequestConfig");
		}
		return dataRequestConfig;
	}
	
	public static void loadDataResponseConfig(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("res.json","conf/");
		String data=stringManagerUtils.readFile(path,"utf-8");
		
		type = new TypeToken<DataResponseConfig>() {}.getType();
		DataResponseConfig dataResponseConfig=gson.fromJson(data, type);
		
		Map<String, Object> map = DataModelMap.getMapObject();
		map.put("dataResponseConfig", dataResponseConfig);
		
		if(dataResponseConfig==null){
			StringManagerUtils.printLog("Parsing the res.json file failed");
			logger.info("Parsing the res.json file failed");
		}
	}
	
	public static DataResponseConfig getDataResponseConfig(){
		Map<String, Object> map = DataModelMap.getMapObject();
		DataResponseConfig dataResponseConfig=(DataResponseConfig) map.get("dataResponseConfig");
		if(dataResponseConfig==null){
			loadDataResponseConfig();
			dataResponseConfig=(DataResponseConfig) map.get("dataResponseConfig");
		}
		return dataResponseConfig;
	}
	
	public static void loadDataReadTimeInfo(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("timestamp.json","conf/");
		String data=stringManagerUtils.readFile(path,"utf-8");
		
		type = new TypeToken<DataReadTimeInfo>() {}.getType();
		DataReadTimeInfo dataReadTimeInfo=gson.fromJson(data, type);
		
		Map<String, Object> map = DataModelMap.getMapObject();
		
		Map<String,String> dataReadTimeInfoMap=new HashMap<>();
		
		if(dataReadTimeInfo!=null && dataReadTimeInfo.getWellList()!=null && dataReadTimeInfo.getWellList().size()>0){
			for(int i=0;i<dataReadTimeInfo.getWellList().size();i++){
				if(dataReadTimeInfoMap.containsKey(dataReadTimeInfo.getWellList().get(i).getWellName())){
					if(StringManagerUtils.getTimeDifference(dataReadTimeInfoMap.get(dataReadTimeInfo.getWellList().get(i).getWellName()), dataReadTimeInfo.getWellList().get(i).getReadTime(), "yyyy-MM-dd HH:mm:ss")>0){
						dataReadTimeInfoMap.put(dataReadTimeInfo.getWellList().get(i).getWellName(), dataReadTimeInfo.getWellList().get(i).getReadTime());
					}
				}else{
					dataReadTimeInfoMap.put(dataReadTimeInfo.getWellList().get(i).getWellName(), dataReadTimeInfo.getWellList().get(i).getReadTime());
				}
			}
		}
		
		
		map.put("dataReadTimeInfoMap", dataReadTimeInfoMap);
	}
	
	public static Map<String,String> getDataReadTimeInfo(){
		Map<String, Object> map = DataModelMap.getMapObject();
		Map<String,String> dataReadTimeInfoMap=(Map<String, String>) map.get("dataReadTimeInfoMap");
		if(dataReadTimeInfoMap==null){
			loadDataReadTimeInfo();
			dataReadTimeInfoMap=(Map<String, String>) map.get("dataReadTimeInfoMap");
		}
		return dataReadTimeInfoMap;
	}
	
	public static void loadRPCWorkType(){
		Map<String, Object> map = DataModelMap.getMapObject();
		Map<Integer,WorkType> workTypeMap=new HashMap<>();
		workTypeMap.put(1201, new WorkType(1201,"抽喷",""));
		workTypeMap.put(1202, new WorkType(1202,"正常",""));
		workTypeMap.put(1203, new WorkType(1203,"充满不足",""));
		workTypeMap.put(1204, new WorkType(1204,"供液不足",""));
		workTypeMap.put(1205, new WorkType(1205,"供液极差",""));
		workTypeMap.put(1206, new WorkType(1206,"抽空",""));
		workTypeMap.put(1207, new WorkType(1207,"泵下堵",""));
		workTypeMap.put(1208, new WorkType(1208,"气锁",""));
		workTypeMap.put(1209, new WorkType(1209,"气影响",""));
		workTypeMap.put(1210, new WorkType(1210,"间隙漏",""));
		workTypeMap.put(1211, new WorkType(1211,"油管漏",""));
		workTypeMap.put(1212, new WorkType(1212,"游动凡尔漏失","洗井、碰泵或检泵"));
		workTypeMap.put(1213, new WorkType(1213,"固定凡尔漏失","洗井、碰泵或检泵"));
		workTypeMap.put(1214, new WorkType(1214,"双凡尔漏失","洗井、碰泵或检泵"));
		workTypeMap.put(1215, new WorkType(1215,"游动凡尔失灵/油管漏","洗井、碰泵或检泵/打压、更换油管"));
		workTypeMap.put(1216, new WorkType(1216,"固定凡尔失灵","洗井、碰泵或检泵"));
		workTypeMap.put(1217, new WorkType(1217,"双凡尔失灵","洗井、碰泵或检泵"));
		workTypeMap.put(1218, new WorkType(1218,"上死点别、碰","校正井口设备"));
		workTypeMap.put(1219, new WorkType(1219,"碰泵","上提（增大）防冲距"));
		workTypeMap.put(1220, new WorkType(1220,"活塞/底部断脱/未入工作筒","检泵/下放（缩小）防冲距"));
		workTypeMap.put(1221, new WorkType(1221,"柱塞脱出工作筒","放（缩小）防冲距"));
		workTypeMap.put(1222, new WorkType(1222,"杆断脱","替换抽油杆"));
		workTypeMap.put(1223, new WorkType(1223,"杆（泵）卡","洗井或检泵"));
		workTypeMap.put(1224, new WorkType(1224,"结蜡","洗井或加药"));
		workTypeMap.put(1225, new WorkType(1225,"严重结蜡","洗井或加药"));
		workTypeMap.put(1226, new WorkType(1226,"出砂","防砂"));
		workTypeMap.put(1227, new WorkType(1227,"严重出砂","防砂"));
		workTypeMap.put(1230, new WorkType(1230,"惯性载荷大","降低冲次"));
		workTypeMap.put(1231, new WorkType(1231,"应力超标","优化抽油杆柱组合"));
		workTypeMap.put(1232, new WorkType(1232,"采集异常","检查采集仪表"));
		workTypeMap.put(1302, new WorkType(1302,"停抽",""));
		
		map.put("workTypeMap", workTypeMap);
	}
	
	public static Map<Integer,WorkType> getRPCWorkTypeInfo(){
		Map<String, Object> map = DataModelMap.getMapObject();
		Map<Integer,WorkType> workTypeMap=(Map<Integer,WorkType>) map.get("workTypeMap");
		if(workTypeMap==null){
			loadRPCWorkType();
			workTypeMap=(Map<Integer,WorkType>) map.get("workTypeMap");
		}
		return workTypeMap;
	}
	
	public static void initDiagramCalculateFailureData(){
		Map<String, Object> map = DataModelMap.getMapObject();
		Map<String,DiagramExceptionData> calculateFailureMap=new HashMap<String,DiagramExceptionData>();
		map.put("diagramCalculateFailureMap", calculateFailureMap);
	}
}
