package com.cosog.main;

import com.cosog.model.DataSourceConfig;
import com.cosog.model.DataWriteBackConfig;
import com.cosog.utils.Config;
import com.cosog.utils.MemoryDataUtils;

public class AgileCalculate {

	@SuppressWarnings({ "static-access", "unused" })
	public static void main(String[] args) {
		String commUrl=Config.getInstance().configFile.getAc().getCommunication();
		
		DataSourceConfig dataSourceConfig=MemoryDataUtils.getDataSourceConfig();
		DataWriteBackConfig dataWriteBackConfig=MemoryDataUtils.getDataWriteBackConfig();
		if(dataSourceConfig!=null && dataSourceConfig.isEnable()){
			do{
				System.out.println(commUrl);
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(true);
		}
	}

}
