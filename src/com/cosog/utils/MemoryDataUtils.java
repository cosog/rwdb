package com.cosog.utils;

import java.util.HashMap;
import java.util.Map;

import com.cosog.model.DataReadTimeInfo;
import com.cosog.model.DataSourceConfig;
import com.cosog.model.DataWriteBackConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MemoryDataUtils {

	public static void loadDataSourceConfig(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("dataSource.json","dataSource/");
		String data=StringManagerUtils.readFile(path,"utf-8").replaceAll(" ", "");
		
		type = new TypeToken<DataSourceConfig>() {}.getType();
		DataSourceConfig dataSourceConfig=gson.fromJson(data, type);
		
		Map<String, Object> map = DataModelMap.getMapObject();
		map.put("dataSourceConfig", dataSourceConfig);
	}
	
	public static DataSourceConfig getDataSourceConfig(){
		Map<String, Object> map = DataModelMap.getMapObject();
		DataSourceConfig dataSourceConfig=(DataSourceConfig) map.get("dataSourceConfig");
		if(dataSourceConfig==null){
			loadDataSourceConfig();
			dataSourceConfig=(DataSourceConfig) map.get("dataSourceConfig");
		}
		return dataSourceConfig;
	}
	
	public static void loadDataWriteBackConfig(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("writeBackConfig.json","dataSource/");
		String data=stringManagerUtils.readFile(path,"utf-8");
		
		type = new TypeToken<DataWriteBackConfig>() {}.getType();
		DataWriteBackConfig dataWriteBackConfig=gson.fromJson(data, type);
		
		Map<String, Object> map = DataModelMap.getMapObject();
		map.put("dataWriteBackConfig", dataWriteBackConfig);
	}
	
	public static DataWriteBackConfig getDataWriteBackConfig(){
		Map<String, Object> map = DataModelMap.getMapObject();
		DataWriteBackConfig dataWriteBackConfig=(DataWriteBackConfig) map.get("dataWriteBackConfig");
		if(dataWriteBackConfig==null){
			loadDataWriteBackConfig();
			dataWriteBackConfig=(DataWriteBackConfig) map.get("dataWriteBackConfig");
		}
		return dataWriteBackConfig;
	}
	
	public static void loadDataReadTimeInfo(){
		Gson gson = new Gson();
		java.lang.reflect.Type type=null;
		StringManagerUtils stringManagerUtils=new StringManagerUtils();
		
		String path=stringManagerUtils.getFilePath("dataReadTimeInfo.json","dataSource/");
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
}
