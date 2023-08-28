package com.cosog.utils;

import java.util.Map;

import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.model.WorkType;

public class DataProcessingUtils {
	public static String getWriteBackSql(RPCCalculateResponseData calculateResponseData){
		String sql="";
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(calculateResponseData!=null 
				&& dataResponseConfig!=null 
				&& dataResponseConfig.isEnable() 
				&& dataResponseConfig.getDiagramResult().getEnable()
				&& dataResponseConfig.getDiagramResult().getColumns()!=null
				&& DataResponseConfig.ConnectInfoEffective(dataResponseConfig.getConnectInfo())){
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
				
//				updateSql+= dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getWellName().getColumn()+",";
				insertValues+=value+",";
				
				wellNameValue=value;
//				itemCount++;
			}
			if(dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getEnable()){
				String value="null";
				if(calculateResponseData.getFESDiagram()!=null){
					value=getOperaValue(calculateResponseData.getFESDiagram().getAcqTime(),dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getType(),dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getRatio());
				}	
//				updateSql+= dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramResult().getColumns().getAcqTime().getColumn()+",";
				insertValues+=value+",";
				
				acqTimeValue=value;
//				itemCount++;
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
				
				sql=updateSql;
				if("insert".equalsIgnoreCase(dataResponseConfig.getWriteType())){
					sql=insertSql;
				}
			}
		}
		return sql;
	}
	
	public static String getProductionDataSql(){
		StringBuffer sqlBuff=new StringBuffer();
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getEnable() 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())){
			sqlBuff.append("select ");
			//井id
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWellId().getColumn()+",");
			//井名
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWellName().getColumn()+",");
			//原油密度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getCrudeOilDensity().getColumn()+",");
			//水密度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWaterDensity().getColumn()+",");
			//天然气相对密度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getNaturalGasRelativeDensity().getColumn()+",");
			//饱和压力
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getSaturationPressure().getColumn()+",");
			//油层中部深度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getReservoirDepth().getColumn()+",");
			//油层中部温度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getReservoirTemperature().getColumn()+",");
			//油压
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getTubingPressure().getColumn()+",");
			//套压
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getCasingPressure().getColumn()+",");
			//井口温度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWellHeadTemperature().getColumn()+",");
			//含水率
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWaterCut().getColumn()+",");
			//生产气油比
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getProductionGasOilRatio().getColumn()+",");
			//动液面
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getProducingfluidLevel().getColumn()+",");
			//泵挂
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getPumpSettingDepth().getColumn()+",");
			//泵筒类型
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBarrelType().getColumn()+",");
			//泵级别
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getPumpGrade().getColumn()+",");
			//泵径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getPumpBoreDiameter().getColumn()+",");
			//柱塞长
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getPlungerLength().getColumn()+",");
			//油管内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getTubingStringInsideDiameter().getColumn()+",");
			//套管内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getCasingStringInsideDiameter().getColumn()+",");
			//一级杆级别
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringGrade1().getColumn()+",");
			//一级杆外径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringOutsideDiameter1().getColumn()+",");
			//一级杆内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringInsideDiameter1().getColumn()+",");
			//一级杆长度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringLength1().getColumn()+",");
			//二级杆级别
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringGrade2().getColumn()+",");
			//二级杆外径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringOutsideDiameter2().getColumn()+",");
			//二级杆内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringInsideDiameter2().getColumn()+",");
			//二级杆长度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringLength2().getColumn()+",");
			//三级杆级别
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringGrade3().getColumn()+",");
			//三级杆外径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringOutsideDiameter3().getColumn()+",");
			//三级杆内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringInsideDiameter3().getColumn()+",");
			//三级杆长度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringLength3().getColumn()+",");
			//四级杆级别
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringGrade4().getColumn()+",");
			//四级杆外径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringOutsideDiameter4().getColumn()+",");
			//四级杆内径
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringInsideDiameter4().getColumn()+",");
			//四级杆长度
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getRodStringLength4().getColumn()+",");
			//曲柄旋转方向
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getCrankRotationDirection().getColumn()+",");
			//曲柄偏置角
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getOffsetAngleOfCrank().getColumn()+",");
			//平衡块1重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight1().getColumn()+",");
			//平衡块2重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight2().getColumn()+",");
			//平衡块3重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight3().getColumn()+",");
			//平衡块4重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight4().getColumn()+",");
			//平衡块5重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight5().getColumn()+",");
			//平衡块6重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight6().getColumn()+",");
			//平衡块7重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight7().getColumn()+",");
			//平衡块8重量
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getBalanceWeight8().getColumn()+",");
			//工况干预代码
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getManualInterventionCode().getColumn()+",");
			//净毛比
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getNetGrossRatio().getColumn()+",");
			//净毛值
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getNetGrossValue().getColumn()+",");
			//反演液面校正值
			sqlBuff.append(" "+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getLevelCorrectValue().getColumn());
			
			sqlBuff.append(" from "+dataRequestConfig.getProductionDataTable().getTableInfo().getName()+" t");
			
			if(StringManagerUtils.isNotNull(dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getSaveTime().getColumn())){
				sqlBuff.append(" where t."+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getSaveTime().getColumn());
				sqlBuff.append("= (select max(t2."+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getSaveTime().getColumn()+") from "+dataRequestConfig.getProductionDataTable().getTableInfo().getName()+" t2 where t2."+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWellName().getColumn()+"=t."+dataRequestConfig.getProductionDataTable().getTableInfo().getColumns().getWellName().getColumn()+" )");
			}
		}
		return sqlBuff.toString();
	}
	
	public static String getOperaValue(String value,String type,float ratio){
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
}
