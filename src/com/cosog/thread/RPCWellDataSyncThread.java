package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		System.out.println("单井线程:"+calculateRequestData.getWellName());
		
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
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			Connection writeBackConn = null;
			PreparedStatement writeBackPstmt = null;
			ResultSet writeBackRs = null;
			
			String sql="";
			Gson gson = new Gson();
			try {
				conn=OracleJdbcUtis.getDiagramConnection();
				writeBackConn=OracleJdbcUtis.getDataWriteBackConnection();
				if(conn!=null && writeBackConn!=null){
					Map<String, Object> map = DataModelMap.getMapObject();
					DiagramExceptionData diagramExceptionData=null;
					Map<String,DiagramExceptionData> diagramCalculateFailureMap=null;
					if(map.containsKey("diagramCalculateFailureMap")){
						diagramCalculateFailureMap=(Map<String, DiagramExceptionData>) map.get("diagramCalculateFailureMap");
						if(diagramCalculateFailureMap.containsKey(calculateRequestData.getWellName())){
							diagramExceptionData=diagramCalculateFailureMap.get(calculateRequestData.getWellName());
						}
					}
					sql=DataProcessingUtils.getDiagramQuerySql(calculateRequestData.getWellName(), fesdiagramacqtime);
					
					pstmt = conn.prepareStatement(sql);
					rs=pstmt.executeQuery();
					System.out.println("线程中查询功图数据:"+calculateRequestData.getWellName());
					long endTime=0,startTime=0;
					String logInfo="";
					String writeBackSql="";
					while(rs.next()){
						try{
							recordCount++;
							startTime=System.nanoTime();
							long diagramId=rs.getLong(1);
							String fesdiagramAcqtimeStr=rs.getString(3);
							float stroke=rs.getFloat(4);
							float spm=rs.getFloat(5);
							int pointCount=rs.getInt(6);
							String sStr=rs.getString(7);
							String fStr=rs.getString(8);
							String iStr=rs.getString(9);
							String KWattStr=rs.getString(10);
							
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
							
							for(int i=0;i<sArr.length;i++){
								calculateRequestData.getFESDiagram().getS().add(StringManagerUtils.stringToFloat(sArr[i]));
							}
							for(int i=0;i<fArr.length;i++){
								calculateRequestData.getFESDiagram().getF().add(StringManagerUtils.stringToFloat(fArr[i]));
							}
							for(int i=0;i<wattArr.length;i++){
								calculateRequestData.getFESDiagram().getWatt().add(StringManagerUtils.stringToFloat(wattArr[i]));
							}
							for(int i=0;i<iArr.length;i++){
								calculateRequestData.getFESDiagram().getI().add(StringManagerUtils.stringToFloat(iArr[i]));
							}
							
							//功图计算
							RPCCalculateResponseData calculateResponseData=CalculateUtils.fesDiagramCalculate(gson.toJson(calculateRequestData));
							
							//结果回写
							if(calculateResponseData!=null){
								if(calculateResponseData.getCalculationStatus().getResultStatus()==1){
									writeBackSql=DataProcessingUtils.getWriteBackSql(calculateResponseData);
									if(StringManagerUtils.isNotNull(writeBackSql)){
										long t1=System.nanoTime();
										writeBackPstmt=writeBackConn.prepareStatement(writeBackSql);
							            int iNum=writeBackPstmt.executeUpdate();
							            long t2=System.nanoTime();
							            System.out.println(calculateRequestData.getWellName()+"单张功图回写时间："+StringManagerUtils.getTimeDiff(t1, t2));
							            if(iNum>0){
							            	writeBackCount++;
							            	endTime=System.nanoTime();
							            	String timeDiffStr=StringManagerUtils.getTimeDiff(startTime, endTime);
							            	logInfo="Write back the calculated data,wellname:"+calculateRequestData.getWellName()+",acqtime:"+fesdiagramAcqtimeStr+",resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",resultCode:"+calculateResponseData.getCalculationStatus().getResultCode()+",durationTime:"+timeDiffStr;
							            	
							            	if(calculateResponseData.getCalculationStatus().getResultStatus()!=1){
							            		logInfo+=",requestData:"+gson.toJson(calculateRequestData);
							            	}
//							            	System.out.println(logInfo);
							            	StringManagerUtils.printLog(logInfo);
							            	StringManagerUtils.printLogFile(logger, logInfo,"info");
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
									
									StringManagerUtils.printLog("Calculation failure,resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
									StringManagerUtils.printLogFile(logger, "Calculation failure,resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime(),"info");
								}
								
								fesdiagramacqtime=fesdiagramAcqtimeStr;
							}else{
								StringManagerUtils.printLog("Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
								StringManagerUtils.printLogFile(logger, "Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime(),"info");
							}
						} catch (SQLException e) {
							e.printStackTrace();
							StringManagerUtils.printLogFile(logger, "error", e, "error");
							StringManagerUtils.printLog("sql:"+writeBackSql);
							StringManagerUtils.printLogFile(logger, "sql:"+writeBackSql, "error");
						} catch (Exception e1) {
							e1.printStackTrace();
							StringManagerUtils.printLogFile(logger, "error", e1, "error");
						} finally{
							
						}
					}
					
				}else{
					if(conn==null){
						StringManagerUtils.printLog("Diagram data database connection failure");
						StringManagerUtils.printLogFile(logger, "Diagram data database connection failure","info");
					}else if(writeBackConn==null){
						StringManagerUtils.printLog("Write back database connection failure");
						StringManagerUtils.printLogFile(logger, "Write back database connection failure","info");
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e, "error");
				StringManagerUtils.printLog("sql:"+sql);
				StringManagerUtils.printLogFile(logger, "sql:"+sql, "error");
			} catch (Exception e1) {
				e1.printStackTrace();
				StringManagerUtils.printLogFile(logger, "error", e1, "error");
			} finally{
				OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
				OracleJdbcUtis.closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
				if(recordCount>0 || writeBackCount>0){
//					System.out.println("Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount);
					StringManagerUtils.printLog("Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount);
					StringManagerUtils.printLogFile(logger, "Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount,"info");
				}
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
		System.out.println("Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount+",durationTime:"+timeDiffStr);
	}

	public RPCCalculateRequestData getCalculateRequestData() {
		return calculateRequestData;
	}

	public void setCalculateRequestData(RPCCalculateRequestData calculateRequestData) {
		this.calculateRequestData = calculateRequestData;
	}
}
