package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cosog.model.DataWriteBackConfig;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;


public class DiagramDataWriteBackThread  extends Thread{
	private RPCCalculateResponseData rpcCalculateResponseData;
	public DiagramDataWriteBackThread(RPCCalculateResponseData rpcCalculateResponseData) {
		super();
		this.rpcCalculateResponseData = rpcCalculateResponseData;
	}
	
	public void run(){
		DataWriteBackConfig dataWriteBackConfig=MemoryDataUtils.getDataWriteBackConfig();
		if(dataWriteBackConfig!=null && dataWriteBackConfig.isEnable() && this.rpcCalculateResponseData!=null){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn=OracleJdbcUtis.getDataWriteBackConnection();
				if(conn!=null){
					if(dataWriteBackConfig.getDiagramResult().getEnable()){
						String updateSql="";
						String insertSql="";
						String insertColumns="";
						String insertValues="";
						int itemCount=0;
						
						String wellNameValue="";
						String acqTimeValue="";
						
						updateSql="update "+dataWriteBackConfig.getDiagramResult().getTableName()+" t set ";
						insertSql="insert into "+dataWriteBackConfig.getDiagramResult().getTableName()+" (";
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getEnable()){
							String value=getOperaValue(this.rpcCalculateResponseData.getWellName(),dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getRatio());
							
//							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getColumn()+",";
							insertValues+=value+",";
							
							wellNameValue=value;
//							itemCount++;
						}
						if(dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData.getFESDiagram()!=null){
								value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getAcqTime(),dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getRatio());
							}	
//							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+",";
							insertValues+=value+",";
							
							acqTimeValue=value;
//							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getFMax().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram().getFMax()!=null&&this.rpcCalculateResponseData.getFESDiagram().getFMax().size()>0){
									value=getOperaValue(rpcCalculateResponseData.getFESDiagram().getFMax().get(0)+"",dataWriteBackConfig.getDiagramResult().getColumns().getFMax().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getFMax().getRatio());
								}
							}	
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getFMax().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getFMax().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getFMin().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram().getFMin()!=null&&this.rpcCalculateResponseData.getFESDiagram().getFMin().size()>0){
									value=getOperaValue(rpcCalculateResponseData.getFESDiagram().getFMin().get(0)+"",dataWriteBackConfig.getDiagramResult().getColumns().getFMin().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getFMin().getRatio());
								}
							}	
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getFMin().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getFMin().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getUpperLoadLine().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getUpperLoadLine()+"",dataWriteBackConfig.getDiagramResult().getColumns().getUpperLoadLine().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getUpperLoadLine().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getUpperLoadLine().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getUpperLoadLine().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getLowerLoadLine().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getLowerLoadLine()+"",dataWriteBackConfig.getDiagramResult().getColumns().getLowerLoadLine().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getLowerLoadLine().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getLowerLoadLine().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getLowerLoadLine().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getFullnessCoefficient().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getFullnessCoefficient()+"",dataWriteBackConfig.getDiagramResult().getColumns().getFullnessCoefficient().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getFullnessCoefficient().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getFullnessCoefficient().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getFullnessCoefficient().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getNoLiquidFullnessCoefficient()+"",dataWriteBackConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getNoLiquidFullnessCoefficient().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getResultCode().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								value=getOperaValue(this.rpcCalculateResponseData.getCalculationStatus().getResultCode()+"",dataWriteBackConfig.getDiagramResult().getColumns().getResultCode().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getResultCode().getRatio());
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getResultCode().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getResultCode().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						//工况名称和优化建议
						String resultName="";
						String optimizationSuggestion="";
						
						resultName=getOperaValue(resultName,dataWriteBackConfig.getDiagramResult().getColumns().getResultName().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getResultName().getRatio());
						optimizationSuggestion=getOperaValue(optimizationSuggestion,dataWriteBackConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getRatio());
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getResultName().getEnable()){
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getResultName().getColumn()+ "="+resultName+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getResultName().getColumn()+",";
							insertValues+=resultName+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getEnable()){
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getColumn()+ "="+optimizationSuggestion+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getOptimizationSuggestion().getColumn()+",";
							insertValues+=optimizationSuggestion+",";
							itemCount++;
						}
						
						//产量
						if(dataWriteBackConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getLiquidVolumetricProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getLiquidVolumetricProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						if(dataWriteBackConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getOilVolumetricProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getOilVolumetricProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						if(dataWriteBackConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getWaterVolumetricProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getWaterVolumetricProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getLiquidWeightProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getLiquidWeightProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						if(dataWriteBackConfig.getDiagramResult().getColumns().getOilWeightProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getOilWeightProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getOilWeightProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getOilWeightProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getOilWeightProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getOilWeightProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						if(dataWriteBackConfig.getDiagramResult().getColumns().getWaterWeightProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getWaterWeightProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getWaterWeightProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getWaterWeightProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getWaterWeightProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getWaterWeightProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getTheoreticalProduction().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getTheoreticalProduction()+"",dataWriteBackConfig.getDiagramResult().getColumns().getTheoreticalProduction().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getTheoreticalProduction().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getTheoreticalProduction().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getTheoreticalProduction().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getPumpEff().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getPumpEfficiency()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getPumpEfficiency().getPumpEff()+"",dataWriteBackConfig.getDiagramResult().getColumns().getPumpEff().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getPumpEff().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getPumpEff().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getPumpEff().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getCalcProducingfluidLevel()+"",dataWriteBackConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getCalcProducingfluidLevel().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getLevelDifferenceValue()+"",dataWriteBackConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getLevelDifferenceValue().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getSubmergence().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getProduction()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getProduction().getSubmergence()+"",dataWriteBackConfig.getDiagramResult().getColumns().getSubmergence().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getSubmergence().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getSubmergence().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getSubmergence().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeIMax().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getUpStrokeIMax()+"",dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeIMax().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeIMax().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeIMax().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeIMax().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeIMax().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getDownStrokeIMax()+"",dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeIMax().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeIMax().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeIMax().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeIMax().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getIDegreeBalance().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getIDegreeBalance()+"",dataWriteBackConfig.getDiagramResult().getColumns().getIDegreeBalance().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getIDegreeBalance().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getIDegreeBalance().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getIDegreeBalance().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getUpStrokeWattMax()+"",dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getUpStrokeWattMax().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getDownStrokeWattMax()+"",dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getDownStrokeWattMax().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getWattDegreeBalance().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getWattDegreeBalance()+"",dataWriteBackConfig.getDiagramResult().getColumns().getWattDegreeBalance().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getWattDegreeBalance().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getWattDegreeBalance().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getWattDegreeBalance().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getDeltaRadius().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getFESDiagram()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getFESDiagram().getDeltaRadius()+"",dataWriteBackConfig.getDiagramResult().getColumns().getDeltaRadius().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getDeltaRadius().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getDeltaRadius().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getDeltaRadius().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}

						if(dataWriteBackConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getSystemEfficiency()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getSystemEfficiency().getWellDownSystemEfficiency()+"",dataWriteBackConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getWellDownSystemEfficiency().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getSystemEfficiency()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getSystemEfficiency().getSurfaceSystemEfficiency()+"",dataWriteBackConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getSurfaceSystemEfficiency().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getSystemEfficiency().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getSystemEfficiency()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getSystemEfficiency().getSystemEfficiency()+"",dataWriteBackConfig.getDiagramResult().getColumns().getSystemEfficiency().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getSystemEfficiency().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getSystemEfficiency().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getSystemEfficiency().getColumn()+",";
							insertValues+=value+",";
							itemCount++;
						}
						
						if(dataWriteBackConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getEnable()){
							String value="null";
							if(this.rpcCalculateResponseData!=null&&(this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==1||this.rpcCalculateResponseData.getCalculationStatus().getResultStatus()==-99)){
								if(this.rpcCalculateResponseData.getSystemEfficiency()!=null){
									value=getOperaValue(this.rpcCalculateResponseData.getSystemEfficiency().getEnergyPer100mLift()+"",dataWriteBackConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getType(),dataWriteBackConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getRatio());
								}	
							}
							updateSql+= dataWriteBackConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getColumn()+ "="+value+",";
							insertColumns+=dataWriteBackConfig.getDiagramResult().getColumns().getEnergyPer100mLift().getColumn()+",";
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
							
							updateSql+=" where "+dataWriteBackConfig.getDiagramResult().getColumns().getWellName().getColumn()+"="+wellNameValue+" "
									+ " and "+dataWriteBackConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+"="+acqTimeValue;
							insertSql+=insertColumns+") values ("+insertValues+")";
							
							String sql=updateSql;
							
							if("insert".equalsIgnoreCase(dataWriteBackConfig.getWriteType())){
								sql=insertSql;
							}
							
							try{  
					            pstmt=conn.prepareStatement(sql);
					            int iNum=pstmt.executeUpdate();
					        }catch(RuntimeException re){  
					        	re.printStackTrace();
					        }
						}
					}
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public RPCCalculateResponseData getRpcCalculateResponseData() {
		return rpcCalculateResponseData;
	}

	public void setRpcCalculateResponseData(RPCCalculateResponseData rpcCalculateResponseData) {
		this.rpcCalculateResponseData = rpcCalculateResponseData;
	}
}
