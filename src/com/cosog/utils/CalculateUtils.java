package com.cosog.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cosog.main.AgileCalculate;
import com.cosog.model.AppRunStatusProbeResonanceData;
import com.cosog.model.CommResponseData;
import com.cosog.model.DiskProbeResponseData;
import com.cosog.model.EnergyCalculateResponseData;
import com.cosog.model.MemoryProbeResponseData;
import com.cosog.model.PCPCalculateResponseData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.model.TimeEffResponseData;
import com.cosog.model.TimeEffTotalResponseData;
import com.cosog.model.TotalAnalysisResponseData;
import com.cosog.model.TotalCalculateResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CalculateUtils {
	private static final Logger logger = Logger.getLogger(CalculateUtils.class.getName());
	
	public static CommResponseData commCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getCommunication();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<CommResponseData>() {}.getType();
		CommResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static TimeEffResponseData runCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getRun();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<TimeEffResponseData>() {}.getType();
		TimeEffResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static EnergyCalculateResponseData energyCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getEnergy();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<EnergyCalculateResponseData>() {}.getType();
		EnergyCalculateResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static RPCCalculateResponseData fesDiagramCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getFESDiagram();
//		String url="http://8.142.92.84:18100/api/calc/rpc/fesdiagram/pro";
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<RPCCalculateResponseData>() {}.getType();
		RPCCalculateResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static PCPCalculateResponseData rpmCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getRPM();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<PCPCalculateResponseData>() {}.getType();
		PCPCalculateResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static TotalAnalysisResponseData totalCalculate(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getTotalCalculation();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<TotalAnalysisResponseData>() {}.getType();
		TotalAnalysisResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static AppRunStatusProbeResonanceData appProbe(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getProbe().getApp();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<AppRunStatusProbeResonanceData>() {}.getType();
		AppRunStatusProbeResonanceData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static MemoryProbeResponseData memProbe(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getProbe().getMem();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<MemoryProbeResponseData>() {}.getType();
		MemoryProbeResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static DiskProbeResponseData diskProbe(String requestDataStr){
		Gson gson=new Gson();
		java.lang.reflect.Type type=null;
		String url=Config.getInstance().configFile.getAc().getProbe().getDisk();
		String responseDataStr=StringManagerUtils.sendPostMethod(url, requestDataStr,"utf-8",0,0);
		type = new TypeToken<DiskProbeResponseData>() {}.getType();
		DiskProbeResponseData responseData=gson.fromJson(responseDataStr, type);
		return responseData;
	}
	
	public static String getRangeJson(String rangeStr) {
        String result = "";
        StringBuffer dynSbf = new StringBuffer();
        if (StringManagerUtils.isNotNull(rangeStr)) {
            dynSbf.append("[");
            String[] wellRunRimeArr = rangeStr.split(";");
            for (int i = 0; i < wellRunRimeArr.length; i++) {
                if ("00:00-24:00".equals(wellRunRimeArr[i]) || "00:00-00:00".equals(wellRunRimeArr[i])) {
                    dynSbf.append("{\"startTime\":\"00:00\",\"endTime\":\"00:00\"}");
                    break;
                } else {
                    String[] tempArr = wellRunRimeArr[i].split("-");
                    dynSbf.append("{\"startTime\":\"" + tempArr + "\",\"endTime\":\"" + tempArr[1] + "\"}");
                }

                if (i < wellRunRimeArr.length - 1) {
                    dynSbf.append(",");
                }
            }
            dynSbf.append("]");
            result = dynSbf.toString();
        } else {
            result = "[{\"startTime\":\"\",\"endTime\":\"\"}]";
        }
        return result;
    }
	
	public static float volumeWaterCutToWeightWaterCut(float volumeWaterCut,float crudeOilDensity,float waterDensity){
		float weightWaterCut=0;
		if(crudeOilDensity!=0 || waterDensity!=0){
			weightWaterCut=100*waterDensity*volumeWaterCut/( waterDensity*volumeWaterCut+(100-volumeWaterCut)*crudeOilDensity );
			weightWaterCut = Math.round(weightWaterCut * 100) / 100f;
		}
		return weightWaterCut;
	}
	
	public static float weightWaterCutToVolumeWaterCut(float weightWaterCut,float crudeOilDensity,float waterDensity){
		float volumeWaterCut=0;
		if(crudeOilDensity!=0 || waterDensity!=0){
			volumeWaterCut=100*crudeOilDensity*weightWaterCut/(crudeOilDensity*weightWaterCut+(100-weightWaterCut)*waterDensity );
			volumeWaterCut = Math.round(volumeWaterCut * 100) / 100f;
		}
		return volumeWaterCut;
	}
}
