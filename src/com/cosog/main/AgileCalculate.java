package com.cosog.main;

import com.cosog.model.DataSourceConfig;
import com.cosog.model.DataWriteBackConfig;
import com.cosog.utils.Config;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.StringManagerUtils;

public class AgileCalculate {

	@SuppressWarnings({ "static-access", "unused" })
	public static void main(String[] args) {
		String commUrl=Config.getInstance().configFile.getAc().getCommunication();
		
		DataSourceConfig dataSourceConfig=MemoryDataUtils.getDataSourceConfig();
		DataWriteBackConfig dataWriteBackConfig=MemoryDataUtils.getDataWriteBackConfig();
		if(dataSourceConfig!=null && dataSourceConfig.getDiagramTable()!=null && dataSourceConfig.getDiagramTable().getEnable()
				&& dataSourceConfig.getProductionDataTable()!=null && dataSourceConfig.getProductionDataTable().getEnable()){
			do{
				System.out.println(commUrl);
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}while(true);
		}
		
//		String path=stringManagerUtils.getFilePath(fileName,"protocolConfig/");
//		StringManagerUtils.writeFile(path,StringManagerUtils.jsonStringFormat(gson.toJson(modbusProtocolConfig)));
	}

}
