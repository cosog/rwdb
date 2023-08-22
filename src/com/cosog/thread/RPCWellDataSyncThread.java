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
import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.model.WorkType;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.Config;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class RPCWellDataSyncThread  extends Thread{
	private RPCCalculateRequestData calculateRequestData;
//	private Logger logger = Logger.getLogger(this.getClass());
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public RPCWellDataSyncThread(RPCCalculateRequestData calculateRequestData) {
		super();
		this.calculateRequestData = calculateRequestData;
	}
	
	public void run(){
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(calculateRequestData!=null 
				&& dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getEnable() 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				&&dataResponseConfig!=null && dataResponseConfig.isEnable()){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn=OracleJdbcUtis.getDiagramConnection();
				if(conn!=null){
					if(dataRequestConfig.getDiagramTable().getEnable()){
						Map<String,String> dataReadTimeInfoMap=MemoryDataUtils.getDataReadTimeInfo();
						
						String fesdiagramacqtime="";
						String currentDate=StringManagerUtils.getCurrentTime("yyyy-MM-dd");
						int defaultTimeSpan=Config.getInstance().configFile.getOther().getDefaultTimeSpan();
						if(dataReadTimeInfoMap!=null && dataReadTimeInfoMap.containsKey(calculateRequestData.getWellName())){
							fesdiagramacqtime=dataReadTimeInfoMap.get(calculateRequestData.getWellName());
						}
						
						String wellNameColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn();
						String acqTimeColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getColumn();
						String strokeColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getStroke().getColumn();
						String spmColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getSPM().getColumn();
						String pointCountColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getPointCount().getColumn();
						String sColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getS().getColumn();
						String fColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getF().getColumn();
						String iColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getI().getColumn();
						String KWattColumn=dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getKWatt().getColumn();
						String sql="";
						String finalSql="";
						Gson gson = new Gson();

						sql="select "+wellNameColumn+","
								+ "to_char("+acqTimeColumn+",'yyyy-mm-dd hh24:mi:ss') as "+acqTimeColumn+", "
								+ strokeColumn+", "
								+ spmColumn+", "
								+ pointCountColumn+", "
								+ sColumn+", "
								+ fColumn+", "
								+ iColumn+", "
								+ KWattColumn+" "
								+ " from "+dataRequestConfig.getDiagramTable().getTableInfo().getName()+" t "
								+ " where t."+dataRequestConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn()+"='"+calculateRequestData.getWellName()+"'";
						if(StringManagerUtils.isNotNull(fesdiagramacqtime)){
							sql+=" and "+acqTimeColumn+" > to_date('"+fesdiagramacqtime+"','yyyy-mm-dd hh24:mi:ss') order by "+acqTimeColumn;
						}else{
//							sql+="order by "+acqTimeColumn+" desc";
							sql+=" and "+acqTimeColumn+" > to_date('"+currentDate+"','yyyy-mm-dd')-"+defaultTimeSpan+" order by "+acqTimeColumn;
						}
						
						finalSql="select "+wellNameColumn+","
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
//							finalSql+=" where rownum<=1";//取最新数据
						}
						
						pstmt = conn.prepareStatement(finalSql);
						rs=pstmt.executeQuery();
						
						while(rs.next()){
							String fesdiagramAcqtimeStr=rs.getString(2);
							float stroke=rs.getFloat(3);
							float spm=rs.getFloat(4);
							int pointCount=rs.getInt(5);
							String sStr=rs.getString(6);
							String fStr=rs.getString(7);
							String iStr=rs.getString(8);
							String KWattStr=rs.getString(9);
							
							StringManagerUtils.printLog("获取"+calculateRequestData.getWellName()+"井功图数据，功图采集时间："+fesdiagramAcqtimeStr);
							logger.info("获取"+calculateRequestData.getWellName()+"井功图数据，功图采集时间："+fesdiagramAcqtimeStr);
							
							calculateRequestData.setFESDiagram(new RPCCalculateRequestData.FESDiagram());
							calculateRequestData.getFESDiagram().setAcqTime(fesdiagramAcqtimeStr);
							calculateRequestData.getFESDiagram().setStroke(stroke);
							calculateRequestData.getFESDiagram().setSPM(spm);
							calculateRequestData.getFESDiagram().setS(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setF(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setWatt(new ArrayList<Float>());
							calculateRequestData.getFESDiagram().setI(new ArrayList<Float>());
							
							String[] sArr=sStr.replaceAll(";", ",").replaceAll("，", ",").split(",");
							String[] fArr=fStr.replaceAll(";", ",").replaceAll("，", ",").split(",");
							String[] wattArr=KWattStr.replaceAll(";", ",").replaceAll("，", ",").split(",");
							String[] iArr=iStr.replaceAll(";", ",").replaceAll("，", ",").split(",");
							
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

							if(dataResponseConfig!=null && dataResponseConfig.isEnable() && calculateResponseData!=null){
								Connection writeBackConn = null;
								PreparedStatement writeBackPstmt = null;
								ResultSet writeBackRs = null;
								try {
									writeBackConn=OracleJdbcUtis.getDataWriteBackConnection();
									if(writeBackConn!=null){
										if(dataResponseConfig.getDiagramResult().getEnable()){
											String updateSql="";
											String insertSql="";
											String insertColumns="";
											String insertValues="";
											int itemCount=0;
											
											String wellNameValue="";
											String acqTimeValue="";
											
											updateSql="update "+dataResponseConfig.getDiagramResult().getTableName()+" t set ";
											insertSql="insert into "+dataResponseConfig.getDiagramResult().getTableName()+" (";
											
											if(dataResponseConfig.getDiagramResult().getColumns().getWellName().getEnable()){
												String value=getOperaValue(calculateResponseData.getWellName(),dataResponseConfig.getDiagramResult().getColumns().getWellName().getType(),dataResponseConfig.getDiagramResult().getColumns().getWellName().getRatio());
												
//												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+",";
												insertValues+=value+",";
												
												wellNameValue=value;
//												itemCount++;
											}
											if(dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getEnable()){
												String value="null";
												if(calculateResponseData.getFESDiagram()!=null){
													value=getOperaValue(calculateResponseData.getFESDiagram().getAcqTime(),dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getType(),dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getRatio());
												}	
//												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+",";
												insertValues+=value+",";
												
												acqTimeValue=value;
//												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getFMax().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram().getFMax()!=null&&calculateResponseData.getFESDiagram().getFMax().size()>0){
														value=getOperaValue(calculateResponseData.getFESDiagram().getFMax().get(0)+"",dataResponseConfig.getDiagramResult().getColumns().getFMax().getType(),dataResponseConfig.getDiagramResult().getColumns().getFMax().getRatio());
													}
												}	
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getFMax().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getFMax().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getFMin().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram().getFMin()!=null&&calculateResponseData.getFESDiagram().getFMin().size()>0){
														value=getOperaValue(calculateResponseData.getFESDiagram().getFMin().get(0)+"",dataResponseConfig.getDiagramResult().getColumns().getFMin().getType(),dataResponseConfig.getDiagramResult().getColumns().getFMin().getRatio());
													}
												}	
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getFMin().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getFMin().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getUpperLoadLine().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getUpperLoadLine()+"",dataResponseConfig.getDiagramResult().getColumns().getUpperLoadLine().getType(),dataResponseConfig.getDiagramResult().getColumns().getUpperLoadLine().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getUpperLoadLine().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getUpperLoadLine().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getLowerLoadLine().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getLowerLoadLine()+"",dataResponseConfig.getDiagramResult().getColumns().getLowerLoadLine().getType(),dataResponseConfig.getDiagramResult().getColumns().getLowerLoadLine().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getLowerLoadLine().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getLowerLoadLine().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getFullnessCoefficient().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getFullnessCoefficient()+"",dataResponseConfig.getDiagramResult().getColumns().getFullnessCoefficient().getType(),dataResponseConfig.getDiagramResult().getColumns().getFullnessCoefficient().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getFullnessCoefficient().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getFullnessCoefficient().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getNoLiquidFullnessCoefficient()+"",dataResponseConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getType(),dataResponseConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getResultCode().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													value=getOperaValue(calculateResponseData.getCalculationStatus().getResultCode()+"",dataResponseConfig.getDiagramResult().getColumns().getResultCode().getType(),dataResponseConfig.getDiagramResult().getColumns().getResultCode().getRatio());
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getResultCode().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getResultCode().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											//工况名称和优化建议
											String resultName="";
											String optimizationSuggestion="";
											if(calculateResponseData!=null && calculateResponseData.getCalculationStatus().getResultStatus()==1){
												Map<Integer,WorkType> workTypeMap=MemoryDataUtils.getRPCWorkTypeInfo();
												WorkType workType=workTypeMap.get(calculateResponseData.getCalculationStatus().getResultCode());
												if(workType!=null){
													resultName=workType.getResultName();
													optimizationSuggestion=workType.getOptimizationSuggestion();
												}
											}
											
											resultName=getOperaValue(resultName,dataResponseConfig.getDiagramResult().getColumns().getResultName().getType(),dataResponseConfig.getDiagramResult().getColumns().getResultName().getRatio());
											optimizationSuggestion=getOperaValue(optimizationSuggestion,dataResponseConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getType(),dataResponseConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getRatio());
											
											if(dataResponseConfig.getDiagramResult().getColumns().getResultName().getEnable()){
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getResultName().getColumn()+ "="+resultName+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getResultName().getColumn()+",";
												insertValues+=resultName+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getEnable()){
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getColumn()+ "="+optimizationSuggestion+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getColumn()+",";
												insertValues+=optimizationSuggestion+",";
												itemCount++;
											}
											
											//产量
											if(dataResponseConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getLiquidVolumetricProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											if(dataResponseConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getOilVolumetricProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											if(dataResponseConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getWaterVolumetricProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											
											if(dataResponseConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getLiquidWeightProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											if(dataResponseConfig.getDiagramResult().getColumns().getOilWeightProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getOilWeightProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getOilWeightProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getOilWeightProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getOilWeightProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getOilWeightProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											if(dataResponseConfig.getDiagramResult().getColumns().getWaterWeightProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getWaterWeightProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getWaterWeightProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getWaterWeightProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWaterWeightProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWaterWeightProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getTheoreticalProduction().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getTheoreticalProduction()+"",dataResponseConfig.getDiagramResult().getColumns().getTheoreticalProduction().getType(),dataResponseConfig.getDiagramResult().getColumns().getTheoreticalProduction().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getTheoreticalProduction().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getTheoreticalProduction().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getPumpEff().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getPumpEfficiency()!=null){
														value=getOperaValue(calculateResponseData.getPumpEfficiency().getPumpEff()+"",dataResponseConfig.getDiagramResult().getColumns().getPumpEff().getType(),dataResponseConfig.getDiagramResult().getColumns().getPumpEff().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getPumpEff().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getPumpEff().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getCalcProducingfluidLevel()+"",dataResponseConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getType(),dataResponseConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getLevelDifferenceValue()+"",dataResponseConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getType(),dataResponseConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getSubmergence().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getProduction()!=null){
														value=getOperaValue(calculateResponseData.getProduction().getSubmergence()+"",dataResponseConfig.getDiagramResult().getColumns().getSubmergence().getType(),dataResponseConfig.getDiagramResult().getColumns().getSubmergence().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getSubmergence().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getSubmergence().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getUpStrokeIMax().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getUpStrokeIMax()+"",dataResponseConfig.getDiagramResult().getColumns().getUpStrokeIMax().getType(),dataResponseConfig.getDiagramResult().getColumns().getUpStrokeIMax().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getUpStrokeIMax().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getUpStrokeIMax().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getDownStrokeIMax().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getDownStrokeIMax()+"",dataResponseConfig.getDiagramResult().getColumns().getDownStrokeIMax().getType(),dataResponseConfig.getDiagramResult().getColumns().getDownStrokeIMax().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getDownStrokeIMax().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getDownStrokeIMax().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getIDegreeBalance().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getIDegreeBalance()+"",dataResponseConfig.getDiagramResult().getColumns().getIDegreeBalance().getType(),dataResponseConfig.getDiagramResult().getColumns().getIDegreeBalance().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getIDegreeBalance().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getIDegreeBalance().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getUpStrokeWattMax()+"",dataResponseConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getType(),dataResponseConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getDownStrokeWattMax()+"",dataResponseConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getType(),dataResponseConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getWattDegreeBalance().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getWattDegreeBalance()+"",dataResponseConfig.getDiagramResult().getColumns().getWattDegreeBalance().getType(),dataResponseConfig.getDiagramResult().getColumns().getWattDegreeBalance().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWattDegreeBalance().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWattDegreeBalance().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getDeltaRadius().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getFESDiagram()!=null){
														value=getOperaValue(calculateResponseData.getFESDiagram().getDeltaRadius()+"",dataResponseConfig.getDiagramResult().getColumns().getDeltaRadius().getType(),dataResponseConfig.getDiagramResult().getColumns().getDeltaRadius().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getDeltaRadius().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getDeltaRadius().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}

											if(dataResponseConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getSystemEfficiency()!=null){
														value=getOperaValue(calculateResponseData.getSystemEfficiency().getWellDownSystemEfficiency()+"",dataResponseConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getType(),dataResponseConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getSystemEfficiency()!=null){
														value=getOperaValue(calculateResponseData.getSystemEfficiency().getSurfaceSystemEfficiency()+"",dataResponseConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getType(),dataResponseConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getSystemEfficiency().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getSystemEfficiency()!=null){
														value=getOperaValue(calculateResponseData.getSystemEfficiency().getSystemEfficiency()+"",dataResponseConfig.getDiagramResult().getColumns().getSystemEfficiency().getType(),dataResponseConfig.getDiagramResult().getColumns().getSystemEfficiency().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getSystemEfficiency().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getSystemEfficiency().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(dataResponseConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getEnable()){
												String value="null";
												if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
													if(calculateResponseData.getSystemEfficiency()!=null){
														value=getOperaValue(calculateResponseData.getSystemEfficiency().getEnergyPer100mLift()+"",dataResponseConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getType(),dataResponseConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getRatio());
													}	
												}
												updateSql+= dataResponseConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getColumn()+ "="+value+",";
												insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getColumn()+",";
												insertValues+=value+",";
												itemCount++;
											}
											
											if(itemCount>0){
												if(updateSql.endsWith(",")){
													updateSql=updateSql.substring(0,updateSql.length()-1);
												}
												if(insertColumns.endsWith(",")){
													insertColumns=insertColumns.substring(0,insertColumns.length()-1);
												}
												if(insertValues.endsWith(",")){
													insertValues=insertValues.substring(0,insertValues.length()-1);
												}
												
												updateSql+=" where "+dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+"="+wellNameValue+" "
														+ " and "+dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+"="+acqTimeValue;
												insertSql+=insertColumns+") values ("+insertValues+")";
												
												String writeBackSql=updateSql;
												
												if("insert".equalsIgnoreCase(dataResponseConfig.getWriteType())){
													writeBackSql=insertSql;
												}
												
												try{  
													writeBackPstmt=writeBackConn.prepareStatement(writeBackSql);
										            int iNum=writeBackPstmt.executeUpdate();
										        }catch(RuntimeException re){  
										        	re.printStackTrace();
										        	logger.error("error", re);
										        }
											}
										}
										
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									logger.error("error", e);
								} finally{
									OracleJdbcUtis.closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
								}
							}
							dataReadTimeInfoMap.put(calculateRequestData.getWellName(), fesdiagramAcqtimeStr);
						}
					}
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("error", e);
			} finally{
				OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
			}
		}
	}
	
	public String getOperaValue(String value,String type,float ratio){
		if("number".equalsIgnoreCase(type)){
			value=(StringManagerUtils.stringToFloat(value)*ratio)+"";
		}else if("date".equalsIgnoreCase(type)){
			value="to_date('"+value+"','yyyy-mm-dd hh24:mi:ss')";
		}else if("varchar2".equalsIgnoreCase(type) || "varchar".equalsIgnoreCase(type) || "nvarchar".equalsIgnoreCase(type) || "nvarchar2".equalsIgnoreCase(type)  || "string".equalsIgnoreCase(type) ){
			value="'"+value+"'";
		}else {
			value="'"+value+"'";
		}
		return value;
	}

	public RPCCalculateRequestData getCalculateRequestData() {
		return calculateRequestData;
	}

	public void setCalculateRequestData(RPCCalculateRequestData calculateRequestData) {
		this.calculateRequestData = calculateRequestData;
	}
}
