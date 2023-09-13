package com.cosog.thread;

import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TestThread   extends Thread{
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
			System.out.println(calculateRequestData.getWellName()+"单张功图计算时间："+StringManagerUtils.getTimeDiff(time1, time2));
		}while(true);
	}
	
}
