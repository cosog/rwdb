package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.DiagramExceptionData;
import com.cosog.model.DiagramExceptionData.ExceptionInfo;
import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.CounterUtils;
import com.cosog.utils.DataModelMap;
import com.cosog.utils.DataProcessingUtils;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class RPCWellDataSyncThread  extends Thread{
	private RPCCalculateRequestData calculateRequestData;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public RPCWellDataSyncThread(RPCCalculateRequestData calculateRequestData) {
		super();
		this.calculateRequestData = calculateRequestData;
	}
	
	public void run(){
		long time1=System.nanoTime();
		int recordCount=0;
		int writeBackCount=0;
		
		long totalCalTime=0,totalWriteBackTime=0;
		long queryDiagramTime=0;
		
		Map<String,String> dataReadTimeInfoMap=MemoryDataUtils.getDataReadTimeInfo();
		
		String fesdiagramacqtime="";
		if(dataReadTimeInfoMap!=null && dataReadTimeInfoMap.containsKey(calculateRequestData.getWellName())){
			fesdiagramacqtime=dataReadTimeInfoMap.get(calculateRequestData.getWellName());
		}
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(calculateRequestData!=null 
				&& dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				
				&& dataResponseConfig!=null 
				&& dataResponseConfig.getDiagramTable()!=null
				&& dataResponseConfig.getDiagramTable().getTableInfo()!=null
				&& dataResponseConfig.getDiagramTable().getTableInfo().getColumns()!=null
				&& DataResponseConfig.ConnectInfoEffective(dataResponseConfig.getDiagramTable().getConnectInfo())
				){
			String sql="";
			Gson gson = new Gson();
			try {
				long endTime=0,startTime=0;
				String logInfo="";
				String writeBackSql="";
				
				Map<String, Object> map = DataModelMap.getMapObject();
				DiagramExceptionData diagramExceptionData=null;
				Map<String,DiagramExceptionData> diagramCalculateFailureMap=null;
				if(map.containsKey("diagramCalculateFailureMap")){
					diagramCalculateFailureMap=(Map<String, DiagramExceptionData>) map.get("diagramCalculateFailureMap");
					if(diagramCalculateFailureMap.containsKey(calculateRequestData.getWellName())){
						diagramExceptionData=diagramCalculateFailureMap.get(calculateRequestData.getWellName());
					}
				}
				long t1=System.nanoTime();
				sql=DataProcessingUtils.getDiagramQuerySql(calculateRequestData.getWellName(), fesdiagramacqtime);
				List<List<Object>> diagramList=OracleJdbcUtis.queryFESDiagramData(sql);
				long t2=System.nanoTime();
				queryDiagramTime=t2-t1;
				
				if(diagramList!=null && diagramList.size()>0){
					for(int i=0;i<diagramList.size();i++){
						List<Object> list=diagramList.get(i);
						try{
							recordCount++;
							startTime=System.nanoTime();
							long diagramId=StringManagerUtils.stringToLong(list.get(0)+"");
							String fesdiagramAcqtimeStr=list.get(2)+"";
							float stroke=StringManagerUtils.stringToFloat(list.get(3)+"");;
							float spm=StringManagerUtils.stringToFloat(list.get(4)+"");;
							int pointCount=StringManagerUtils.stringToInteger(list.get(5)+"");;
							String sStr=list.get(6)+"";
							String fStr=list.get(7)+"";
							String iStr=list.get(8)+"";
							String KWattStr=list.get(9)+"";
							
							calculateRequestData.setFESDiagram(new RPCCalculateRequestData.FESDiagram());
							calculateRequestData.getFESDiagram().setAcqTime(fesdiagramAcqtimeStr);
							calculateRequestData.getFESDiagram().setStroke(stroke);
							calculateRequestData.getFESDiagram().setSPM(spm);
							calculateRequestData.getFESDiagram().setS(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setF(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setWatt(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setI(new ArrayList<Float>());
							
							String[] sArr=sStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
							String[] fArr=fStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
							String[] wattArr=KWattStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
							String[] iArr=iStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
							
							for(int j=0;j<sArr.length;j++){
								calculateRequestData.getFESDiagram().getS().add(StringManagerUtils.stringToFloat(sArr[j]));
							}
							for(int j=0;j<fArr.length;j++){
								calculateRequestData.getFESDiagram().getF().add(StringManagerUtils.stringToFloat(fArr[j]));
							}
							for(int j=0;j<wattArr.length;j++){
								calculateRequestData.getFESDiagram().getWatt().add(StringManagerUtils.stringToFloat(wattArr[j]));
							}
							for(int j=0;j<iArr.length;j++){
								calculateRequestData.getFESDiagram().getI().add(StringManagerUtils.stringToFloat(iArr[j]));
							}
							
							//功图计算
							RPCCalculateResponseData calculateResponseData=null;
							t1=System.nanoTime();
							calculateResponseData=CalculateUtils.fesDiagramCalculate(gson.toJson(calculateRequestData));
							t2=System.nanoTime();
							totalCalTime+=(t2-t1);
//							StringManagerUtils.printLog("单张功图计算时间:"+StringManagerUtils.getTimeDiff(t1, t2));
//							StringManagerUtils.printLogFile(logger, "单张功图计算时间:"+StringManagerUtils.getTimeDiff(t1, t2) ,"info");
							
							//结果回写
							if(calculateResponseData!=null){
								if(calculateResponseData.getCalculationStatus().getResultStatus()==1){
									writeBackSql=DataProcessingUtils.getWriteBackSql(calculateResponseData);
									if(StringManagerUtils.isNotNull(writeBackSql)){
										t1=System.nanoTime();
							            int iNum=OracleJdbcUtis.writeBackDiagramCalculateData(writeBackSql);
							            t2=System.nanoTime();
							            totalWriteBackTime+=(t2-t1);
//							            StringManagerUtils.printLog("单张功图回写时间:"+StringManagerUtils.getTimeDiff(t1, t2));
//										StringManagerUtils.printLogFile(logger, "单张功图回写时间:"+StringManagerUtils.getTimeDiff(t1, t2) ,"info");
							            if(iNum>0){
							            	writeBackCount++;
							            }
									}
								}else{
									//记录计算失败数据
									if(diagramCalculateFailureMap!=null){
										if(diagramExceptionData==null){
											diagramExceptionData=new DiagramExceptionData();
											diagramExceptionData.setWellName(calculateRequestData.getWellName());
											diagramExceptionData.setReCalculateTimes(0);
											diagramExceptionData.setLastCalculateTime(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
											diagramExceptionData.setExceptionDataList(new ArrayList<ExceptionInfo>());
											
											diagramCalculateFailureMap.put(calculateRequestData.getWellName(), diagramExceptionData);
											diagramExceptionData=diagramCalculateFailureMap.get(calculateRequestData.getWellName());
										}
										diagramExceptionData.addDiagramId(calculateResponseData.getCalculationStatus().getResultStatus(), diagramId);
									}
									StringManagerUtils.printLog("Calculation failure,resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime()+",error:"+(calculateResponseData.getVerification()!=null?calculateResponseData.getVerification().getErrorString():""));
									StringManagerUtils.printLogFile(logger, "Calculation failure,resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime()+",error:"+(calculateResponseData.getVerification()!=null?calculateResponseData.getVerification().getErrorString():""),"info");
								}
								fesdiagramacqtime=fesdiagramAcqtimeStr;
								CounterUtils.incr();//计数器加一
							}else{
								StringManagerUtils.printLog("Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
								StringManagerUtils.printLogFile(logger, "Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime(),"info");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							StringManagerUtils.printLogFile(logger, "error", e1, "error");
						}
					}
				}
			}catch (Exception e1) {
				e1.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e1, "error");
			} finally{
				dataReadTimeInfoMap.put(calculateRequestData.getWellName(), fesdiagramacqtime);
			}
		}else{
			if(calculateRequestData==null){
				StringManagerUtils.printLog("calculateRequestData is null");
				StringManagerUtils.printLogFile(logger, "calculateRequestData is null","info");
			}else if(!(dataRequestConfig!=null 
					&& dataRequestConfig.getDiagramTable()!=null 
					&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
					&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
					&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo()))){
				StringManagerUtils.printLog("The configuration information of the diagram data table is incorrect");
				StringManagerUtils.printLogFile(logger, "The configuration information of the diagram data table is incorrect","info");
			}else{
				StringManagerUtils.printLog("The configuration information of the write back diagram table is incorrect");
				StringManagerUtils.printLogFile(logger, "The configuration information of the write back diagram table is incorrect","info");
			}
		}
		long time2=System.nanoTime();
		String timeDiffStr=StringManagerUtils.getTimeDiff(time1, time2);
		if(recordCount>0){
			String info="Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount+",durationTime:"+timeDiffStr+",平均计算时间:"+StringManagerUtils.getTimeDiff( ((double)totalCalTime)/((double)recordCount) )+",平均回写时间:"+StringManagerUtils.getTimeDiff( ((double)totalWriteBackTime)/((double)recordCount) )+",功图查询耗时:"+StringManagerUtils.getTimeDiff(queryDiagramTime);
			StringManagerUtils.printLog(info);
			StringManagerUtils.printLogFile(logger, info,"info");
		}
		CounterUtils.countDown();
	}

	public RPCCalculateRequestData getCalculateRequestData() {
		return calculateRequestData;
	}

	public void setCalculateRequestData(RPCCalculateRequestData calculateRequestData) {
		this.calculateRequestData = calculateRequestData;
	}
}
