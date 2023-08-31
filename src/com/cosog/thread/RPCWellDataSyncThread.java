package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import com.cosog.model.WorkType;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.Config;
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
		Map<String,String> dataReadTimeInfoMap=MemoryDataUtils.getDataReadTimeInfo();
		
		String fesdiagramacqtime="";
		String currentDate=StringManagerUtils.getCurrentTime("yyyy-MM-dd");
		int defaultTimeSpan=Config.getInstance().configFile.getOther().getDefaultTimeSpan();
		if(dataReadTimeInfoMap!=null && dataReadTimeInfoMap.containsKey(calculateRequestData.getWellName())){
			fesdiagramacqtime=dataReadTimeInfoMap.get(calculateRequestData.getWellName());
		}
		
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(calculateRequestData!=null 
				&& dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getEnable() 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				
				&& dataResponseConfig!=null 
				&& dataResponseConfig.isEnable() 
				&& dataResponseConfig.getDiagramResult().getEnable()
				&& dataResponseConfig.getDiagramResult().getColumns()!=null
				&& DataResponseConfig.ConnectInfoEffective(dataResponseConfig.getConnectInfo())
				){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			Connection writeBackConn = null;
			PreparedStatement writeBackPstmt = null;
			ResultSet writeBackRs = null;
			
			String sql="";
			String finalSql="";
			int recordCount=0;
			int writeBackCount=0;
			
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
					
					String diagramIdColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getDiagramId().getColumn();
					String wellNameColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn();
					String acqTimeColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getColumn();
					String strokeColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getStroke().getColumn();
					String spmColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getSPM().getColumn();
					String pointCountColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getPointCount().getColumn();
					String sColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getS().getColumn();
					String fColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getF().getColumn();
					String iColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getI().getColumn();
					String KWattColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getKWatt().getColumn();
					sql="";
					finalSql="";
					sql="select "+diagramIdColumn+","
							+ wellNameColumn+","
							+ "to_char("+acqTimeColumn+",'yyyy-mm-dd hh24:mi:ss') as "+acqTimeColumn+", "
							+ strokeColumn+", "
							+ spmColumn+", "
							+ pointCountColumn+", "
							+ sColumn+", "
							+ fColumn+", "
							+ iColumn+", "
							+ KWattColumn+" "
							+ " from "+dataRequestConfig.getDiagramTable().getTableInfo().getName()+" t "
							+ " where t."+wellNameColumn+"='"+calculateRequestData.getWellName()+"'";
					if(StringManagerUtils.isNotNull(fesdiagramacqtime)){
						sql+=" and "+acqTimeColumn+" > to_date('"+fesdiagramacqtime+"','yyyy-mm-dd hh24:mi:ss') order by "+acqTimeColumn;
					}else{
//						sql+="order by "+acqTimeColumn+" desc";
						sql+=" and "+acqTimeColumn+" > to_date('"+currentDate+"','yyyy-mm-dd')-"+defaultTimeSpan+" order by "+acqTimeColumn;
					}
					
					finalSql="select "+diagramIdColumn+","
							+ wellNameColumn+","
							+ acqTimeColumn+", "
							+ strokeColumn+", "
							+ spmColumn+", "
							+ pointCountColumn+", "
							+ sColumn+", "
							+ fColumn+", "
							+ iColumn+", "
							+ KWattColumn+" "
							+ " from ("+sql+") v";
					if(StringManagerUtils.isNotNull(fesdiagramacqtime)){
						finalSql+=" where rownum<=100";
					}else{
						finalSql+=" where rownum<=100";
//						finalSql+=" where rownum<=1";//取最新数据
					}
					
					pstmt = conn.prepareStatement(finalSql);
					rs=pstmt.executeQuery();
					
					long endTime=0,startTime=0;
					long diffTime=0;
	            	double durationTime=diffTime;
					String timeUnit="ns";
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
										writeBackPstmt=writeBackConn.prepareStatement(writeBackSql);
							            int iNum=writeBackPstmt.executeUpdate();
							            if(iNum>0){
							            	writeBackCount++;
							            	endTime=System.nanoTime();
							            	diffTime=endTime-startTime;
							            	durationTime=diffTime;
							            	if(diffTime<1000){
							            		timeUnit="ns";
							            	}else if(diffTime>=1000 && diffTime<1000*1000){
							            		timeUnit="us";
							            		durationTime=(double)(Math.round(diffTime*100/1000)/100.0);
							            	}else if(diffTime>=1000*1000 && diffTime<1000*1000*1000){
							            		timeUnit="ms";
							            		durationTime=(double)(Math.round(diffTime*100/(1000*1000))/100.0);
							            	}else if(diffTime>=1000*1000*1000){
							            		timeUnit="s";
							            		durationTime=(double)(Math.round(diffTime*100/(1000*1000*1000))/100.0);
							            	}
							            	logInfo="Write back the calculated data,wellname:"+calculateRequestData.getWellName()+",acqtime:"+fesdiagramAcqtimeStr+",resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",resultCode:"+calculateResponseData.getCalculationStatus().getResultCode()+",durationTime:"+durationTime+timeUnit;
							            	
							            	if(calculateResponseData.getCalculationStatus().getResultStatus()!=1){
							            		logInfo+=",requestData:"+gson.toJson(calculateRequestData);
							            	}
							            	StringManagerUtils.printLog(logInfo);
											logger.info(logInfo);
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
									
									StringManagerUtils.printLog("Calculation resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
									logger.info("Calculation resultStatus:"+calculateResponseData.getCalculationStatus().getResultStatus()+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
								}
								dataReadTimeInfoMap.put(calculateRequestData.getWellName(), fesdiagramAcqtimeStr);
							}else{
								StringManagerUtils.printLog("Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
								logger.info("Calculation failed, no response data"+",wellName:"+calculateRequestData.getWellName()+",acqTime:"+calculateRequestData.getFESDiagram().getAcqTime());
							}
						} catch (SQLException e) {
							e.printStackTrace();
							logger.error("error", e);
							StringManagerUtils.printLog("sql:"+writeBackSql);
							logger.error("sql:"+writeBackSql);
						} catch (Exception e1) {
							e1.printStackTrace();
							logger.error("error", e1);
						} finally{
							
						}
					}
				}else{
					if(conn==null){
						StringManagerUtils.printLog("Diagram data database connection failure");
						logger.info("Diagram data database connection failure");
					}else if(writeBackConn==null){
						StringManagerUtils.printLog("Write back database connection failure");
						logger.info("Write back database connection failure");
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("error", e);
				StringManagerUtils.printLog("sql:"+finalSql);
				logger.error("sql:"+finalSql);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("error", e1);
			} finally{
				OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
				OracleJdbcUtis.closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
				if(recordCount>0 || writeBackCount>0){
					StringManagerUtils.printLog("Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount);
					logger.info("Get fesdiagram data,wellname:"+calculateRequestData.getWellName()+",current acqtime:"+fesdiagramacqtime+",recordCount:"+recordCount+",writeBackCount:"+writeBackCount);
				}
			}
		}else{
			if(calculateRequestData==null){
				StringManagerUtils.printLog("calculateRequestData is null");
				logger.info("calculateRequestData is null");
			}else if(!(dataRequestConfig!=null 
					&& dataRequestConfig.getDiagramTable()!=null 
					&& dataRequestConfig.getDiagramTable().getEnable() 
					&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
					&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
					&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo()))){
				StringManagerUtils.printLog("The configuration information of the diagram data table is incorrect");
				logger.info("The configuration information of the diagram data table is incorrect");
			}else{
				StringManagerUtils.printLog("The configuration information of the diagram data table is incorrect");
				logger.info("The configuration information of the diagram data table is incorrect");
			}
		}
	}

	public RPCCalculateRequestData getCalculateRequestData() {
		return calculateRequestData;
	}

	public void setCalculateRequestData(RPCCalculateRequestData calculateRequestData) {
		this.calculateRequestData = calculateRequestData;
	}
}
