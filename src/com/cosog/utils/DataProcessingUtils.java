package com.cosog.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.model.WorkType;

public class DataProcessingUtils {
	private static final Logger logger = Logger.getLogger(DataProcessingUtils.class.getName());
	public static String getDiagramQuerySql(String wellName,String fesdiagramacqtime){
		String sql="";
		String finalSql="";
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				){
			String currentDate=StringManagerUtils.getCurrentTime("yyyy-MM-dd");
			int defaultTimeSpan=Config.getInstance().configFile.getOther().getDefaultTimeSpan();
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
					+ " where t."+wellNameColumn+"='"+wellName+"'";
			if(StringManagerUtils.isNotNull(fesdiagramacqtime)){
				sql+=" and "+acqTimeColumn+" > to_date('"+fesdiagramacqtime+"','yyyy-mm-dd hh24:mi:ss') order by "+acqTimeColumn;
			}else{
//				sql+="order by "+acqTimeColumn+" desc";
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
//				finalSql+=" where rownum<=1";//取最新数据
			}
		}
		
		return finalSql;
	}
	
	
	public static String getWriteBackSql(RPCCalculateResponseData calculateResponseData){
		String sql="";
		DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
		if(calculateResponseData!=null 
				&& dataResponseConfig!=null 
				&& dataResponseConfig.getDiagramTable()!=null
				&& dataResponseConfig.getDiagramTable().getTableInfo()!=null
				&& dataResponseConfig.getDiagramTable().getTableInfo().getColumns()!=null
				&& DataResponseConfig.ConnectInfoEffective(dataResponseConfig.getDiagramTable().getConnectInfo())
				){
			String updateSql="";
			String insertSql="";
			String insertColumns="";
			String insertValues="";
			int itemCount=0;
			
			String wellNameValue="";
			String acqTimeValue="";
			
			updateSql="update "+dataResponseConfig.getDiagramTable().getTableInfo().getName()+" t set ";
			insertSql="insert into "+dataResponseConfig.getDiagramTable().getTableInfo().getName()+" (";
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getEnable()){
				String value=getOperaValue(calculateResponseData.getWellName(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getRatio());
				
//				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn()+",";
				insertValues+=value+",";
				
				wellNameValue=value;
//				itemCount++;
			}
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getEnable()){
				String value="null";
				if(calculateResponseData.getFESDiagram()!=null){
					value=getOperaValue(calculateResponseData.getFESDiagram().getAcqTime(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getRatio());
				}	
//				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getColumn()+",";
				insertValues+=value+",";
				
				acqTimeValue=value;
//				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMax().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram().getFMax()!=null&&calculateResponseData.getFESDiagram().getFMax().size()>0){
						value=getOperaValue(calculateResponseData.getFESDiagram().getFMax().get(0)+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMax().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMax().getRatio());
					}
				}	
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMax().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMax().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMin().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram().getFMin()!=null&&calculateResponseData.getFESDiagram().getFMin().size()>0){
						value=getOperaValue(calculateResponseData.getFESDiagram().getFMin().get(0)+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMin().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMin().getRatio());
					}
				}	
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMin().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFMin().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpperLoadLine().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getUpperLoadLine()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpperLoadLine().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpperLoadLine().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpperLoadLine().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpperLoadLine().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLowerLoadLine().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getLowerLoadLine()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLowerLoadLine().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLowerLoadLine().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLowerLoadLine().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLowerLoadLine().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFullnessCoefficient().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getFullnessCoefficient()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFullnessCoefficient().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFullnessCoefficient().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFullnessCoefficient().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getFullnessCoefficient().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getNoLiquidFullnessCoefficient().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getNoLiquidFullnessCoefficient()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getNoLiquidFullnessCoefficient().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getNoLiquidFullnessCoefficient().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getNoLiquidFullnessCoefficient().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getNoLiquidFullnessCoefficient().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultCode().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					value=getOperaValue(calculateResponseData.getCalculationStatus().getResultCode()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultCode().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultCode().getRatio());
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultCode().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultCode().getColumn()+",";
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
			
			resultName=getOperaValue(resultName,dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultName().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultName().getRatio());
			optimizationSuggestion=getOperaValue(optimizationSuggestion,dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOptimizationSuggestion().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOptimizationSuggestion().getRatio());
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultName().getEnable()){
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultName().getColumn()+ "="+resultName+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getResultName().getColumn()+",";
				insertValues+=resultName+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOptimizationSuggestion().getEnable()){
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOptimizationSuggestion().getColumn()+ "="+optimizationSuggestion+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOptimizationSuggestion().getColumn()+",";
				insertValues+=optimizationSuggestion+",";
				itemCount++;
			}
			
			//产量
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidVolumetricProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getLiquidVolumetricProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidVolumetricProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidVolumetricProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidVolumetricProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidVolumetricProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilVolumetricProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getOilVolumetricProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilVolumetricProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilVolumetricProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilVolumetricProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilVolumetricProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterVolumetricProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getWaterVolumetricProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterVolumetricProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterVolumetricProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterVolumetricProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterVolumetricProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidWeightProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getLiquidWeightProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidWeightProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidWeightProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidWeightProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLiquidWeightProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilWeightProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getOilWeightProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilWeightProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilWeightProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilWeightProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getOilWeightProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterWeightProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getWaterWeightProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterWeightProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterWeightProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterWeightProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWaterWeightProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getTheoreticalProduction().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getTheoreticalProduction()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getTheoreticalProduction().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getTheoreticalProduction().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getTheoreticalProduction().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getTheoreticalProduction().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getPumpEff().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getPumpEfficiency()!=null){
						value=getOperaValue(calculateResponseData.getPumpEfficiency().getPumpEff()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getPumpEff().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getPumpEff().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getPumpEff().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getPumpEff().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getCalcProducingfluidLevel().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getCalcProducingfluidLevel()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getCalcProducingfluidLevel().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getCalcProducingfluidLevel().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getCalcProducingfluidLevel().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getCalcProducingfluidLevel().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLevelDifferenceValue().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getLevelDifferenceValue()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLevelDifferenceValue().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLevelDifferenceValue().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLevelDifferenceValue().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getLevelDifferenceValue().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSubmergence().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getProduction()!=null){
						value=getOperaValue(calculateResponseData.getProduction().getSubmergence()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSubmergence().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSubmergence().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSubmergence().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSubmergence().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeIMax().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getUpStrokeIMax()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeIMax().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeIMax().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeIMax().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeIMax().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeIMax().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getDownStrokeIMax()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeIMax().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeIMax().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeIMax().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeIMax().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getIDegreeBalance().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getIDegreeBalance()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getIDegreeBalance().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getIDegreeBalance().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getIDegreeBalance().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getIDegreeBalance().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeWattMax().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getUpStrokeWattMax()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeWattMax().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeWattMax().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeWattMax().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getUpStrokeWattMax().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeWattMax().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getDownStrokeWattMax()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeWattMax().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeWattMax().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeWattMax().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDownStrokeWattMax().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWattDegreeBalance().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getWattDegreeBalance()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWattDegreeBalance().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWattDegreeBalance().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWattDegreeBalance().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWattDegreeBalance().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDeltaRadius().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getFESDiagram()!=null){
						value=getOperaValue(calculateResponseData.getFESDiagram().getDeltaRadius()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDeltaRadius().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDeltaRadius().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDeltaRadius().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getDeltaRadius().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}

			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellDownSystemEfficiency().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getSystemEfficiency()!=null){
						value=getOperaValue(calculateResponseData.getSystemEfficiency().getWellDownSystemEfficiency()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellDownSystemEfficiency().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellDownSystemEfficiency().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellDownSystemEfficiency().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellDownSystemEfficiency().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSurfaceSystemEfficiency().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getSystemEfficiency()!=null){
						value=getOperaValue(calculateResponseData.getSystemEfficiency().getSurfaceSystemEfficiency()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSurfaceSystemEfficiency().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSurfaceSystemEfficiency().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSurfaceSystemEfficiency().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSurfaceSystemEfficiency().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSystemEfficiency().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getSystemEfficiency()!=null){
						value=getOperaValue(calculateResponseData.getSystemEfficiency().getSystemEfficiency()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSystemEfficiency().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSystemEfficiency().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSystemEfficiency().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getSystemEfficiency().getColumn()+",";
				insertValues+=value+",";
				itemCount++;
			}
			
			if(dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getEnergyPer100mLift().getEnable()){
				String value="null";
				if(calculateResponseData!=null&&(calculateResponseData.getCalculationStatus().getResultStatus()==1||calculateResponseData.getCalculationStatus().getResultStatus()==-99)){
					if(calculateResponseData.getSystemEfficiency()!=null){
						value=getOperaValue(calculateResponseData.getSystemEfficiency().getEnergyPer100mLift()+"",dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getEnergyPer100mLift().getType(),dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getEnergyPer100mLift().getRatio());
					}	
				}
				updateSql+= dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getEnergyPer100mLift().getColumn()+ "="+value+",";
				insertColumns+=dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getEnergyPer100mLift().getColumn()+",";
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
				
				updateSql+=" where "+dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getWellName().getColumn()+"="+wellNameValue+" "
						+ " and "+dataResponseConfig.getDiagramTable().getTableInfo().getColumns().getAcqTime().getColumn()+"="+acqTimeValue;
				insertSql+=insertColumns+") values ("+insertValues+")";
				
				sql=updateSql;
				if("insert".equalsIgnoreCase(dataResponseConfig.getDiagramTable().getWriteType())){
					sql=insertSql;
				}
			}
		}
		return sql;
	}
	
	public static String getProductionDataSql(List<String> wellList){
		StringBuffer sqlBuff=new StringBuffer();
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())){
			sqlBuff.append("select ");
			//井id
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellId().getColumn()+",");
			//井名
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellName().getColumn()+",");
			//原油密度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getCrudeOilDensity().getColumn()+",");
			//水密度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWaterDensity().getColumn()+",");
			//天然气相对密度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getNaturalGasRelativeDensity().getColumn()+",");
			//饱和压力
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getSaturationPressure().getColumn()+",");
			//油层中部深度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getReservoirDepth().getColumn()+",");
			//油层中部温度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getReservoirTemperature().getColumn()+",");
			//油压
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getTubingPressure().getColumn()+",");
			//套压
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getCasingPressure().getColumn()+",");
			//井口温度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellHeadTemperature().getColumn()+",");
			//含水率
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWaterCut().getColumn()+",");
			//生产气油比
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getProductionGasOilRatio().getColumn()+",");
			//动液面
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getProducingfluidLevel().getColumn()+",");
			//泵挂
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getPumpSettingDepth().getColumn()+",");
			//泵筒类型
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBarrelType().getColumn()+",");
			//泵级别
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getPumpGrade().getColumn()+",");
			//泵径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getPumpBoreDiameter().getColumn()+",");
			//柱塞长
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getPlungerLength().getColumn()+",");
			//油管内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getTubingStringInsideDiameter().getColumn()+",");
			//套管内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getCasingStringInsideDiameter().getColumn()+",");
			//一级杆级别
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringGrade1().getColumn()+",");
			//一级杆外径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringOutsideDiameter1().getColumn()+",");
			//一级杆内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringInsideDiameter1().getColumn()+",");
			//一级杆长度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringLength1().getColumn()+",");
			//二级杆级别
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringGrade2().getColumn()+",");
			//二级杆外径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringOutsideDiameter2().getColumn()+",");
			//二级杆内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringInsideDiameter2().getColumn()+",");
			//二级杆长度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringLength2().getColumn()+",");
			//三级杆级别
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringGrade3().getColumn()+",");
			//三级杆外径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringOutsideDiameter3().getColumn()+",");
			//三级杆内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringInsideDiameter3().getColumn()+",");
			//三级杆长度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringLength3().getColumn()+",");
			//四级杆级别
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringGrade4().getColumn()+",");
			//四级杆外径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringOutsideDiameter4().getColumn()+",");
			//四级杆内径
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringInsideDiameter4().getColumn()+",");
			//四级杆长度
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getRodStringLength4().getColumn()+",");
			//曲柄旋转方向
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getCrankRotationDirection().getColumn()+",");
			//曲柄偏置角
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getOffsetAngleOfCrank().getColumn()+",");
			//平衡块1重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight1().getColumn()+",");
			//平衡块2重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight2().getColumn()+",");
			//平衡块3重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight3().getColumn()+",");
			//平衡块4重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight4().getColumn()+",");
			//平衡块5重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight5().getColumn()+",");
			//平衡块6重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight6().getColumn()+",");
			//平衡块7重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight7().getColumn()+",");
			//平衡块8重量
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getBalanceWeight8().getColumn()+",");
			//工况干预代码
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getManualInterventionCode().getColumn()+",");
			//净毛比
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getNetGrossRatio().getColumn()+",");
			//净毛值
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getNetGrossValue().getColumn()+",");
			//反演液面校正值
			sqlBuff.append(" "+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getLevelCorrectValue().getColumn());
			
			sqlBuff.append(" from "+dataRequestConfig.getAuxTable().getTableInfo().getName()+" t where 1=1");
			if( wellList!=null && wellList.size()>0 ){
				sqlBuff.append(" and t."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellName().getColumn()+" in ("+StringManagerUtils.joinStringArr2(wellList, ",")+")");
			}
			
			if(StringManagerUtils.isNotNull(dataRequestConfig.getAuxTable().getTableInfo().getColumns().getSaveTime().getColumn())){
				sqlBuff.append(" and t."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getSaveTime().getColumn());
				sqlBuff.append("= (select max(t2."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getSaveTime().getColumn()+") from "+dataRequestConfig.getAuxTable().getTableInfo().getName()+" t2 where t2."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellName().getColumn()+"=t."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellName().getColumn()+" )");
			}
			
//			sqlBuff.append(" and t.prod_id<=12");
//			sqlBuff.append(" and t."+dataRequestConfig.getAuxTable().getTableInfo().getColumns().getWellName().getColumn()+" in('rpc00001')");
		}
		return sqlBuff.toString();
	}
	
	public static String getDiagramQuerySql(String wellName,List<Long> list){
		String sql="";
		DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
		if(dataRequestConfig!=null 
				&& dataRequestConfig.getDiagramTable()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo()!=null 
				&& dataRequestConfig.getDiagramTable().getTableInfo().getColumns()!=null 
				&& DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getDiagramTable().getConnectInfo())
				){
			String currentDate=StringManagerUtils.getCurrentTime("yyyy-MM-dd");
			int defaultTimeSpan=Config.getInstance().configFile.getOther().getDefaultTimeSpan();
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
					+ " where t."+wellNameColumn+"='"+wellName+"' "
					+ " and t.diagramIdColumn in("+StringManagerUtils.join_long(list, ",")+")";
		}
		
		return sql;
	}
	
	public static RPCCalculateRequestData getRPCCalculateRequestData(List<Object> list){
		RPCCalculateRequestData rpcCalculateRequestData=null;
		try{
			rpcCalculateRequestData=new RPCCalculateRequestData();
			rpcCalculateRequestData.init();
			rpcCalculateRequestData.setWellName(list.get(1)+"");
			rpcCalculateRequestData.getFluidPVT().setCrudeOilDensity(StringManagerUtils.stringToFloat(list.get(2)+""));
			rpcCalculateRequestData.getFluidPVT().setWaterDensity(StringManagerUtils.stringToFloat(list.get(3)+""));
			rpcCalculateRequestData.getFluidPVT().setNaturalGasRelativeDensity(StringManagerUtils.stringToFloat(list.get(4)+""));
			rpcCalculateRequestData.getFluidPVT().setSaturationPressure(StringManagerUtils.stringToFloat(list.get(5)+""));
			
			rpcCalculateRequestData.getReservoir().setDepth(StringManagerUtils.stringToFloat(list.get(6)+""));
			rpcCalculateRequestData.getReservoir().setTemperature(StringManagerUtils.stringToFloat(list.get(7)+""));
			
			rpcCalculateRequestData.getProduction().setTubingPressure(StringManagerUtils.stringToFloat(list.get(8)+""));
			rpcCalculateRequestData.getProduction().setCasingPressure(StringManagerUtils.stringToFloat(list.get(9)+""));
			rpcCalculateRequestData.getProduction().setWellHeadTemperature(StringManagerUtils.stringToFloat(list.get(10)+""));
			rpcCalculateRequestData.getProduction().setWaterCut(StringManagerUtils.stringToFloat(list.get(11)+""));
			rpcCalculateRequestData.getProduction().setProductionGasOilRatio(StringManagerUtils.stringToFloat(list.get(12)+""));
			rpcCalculateRequestData.getProduction().setProducingfluidLevel(StringManagerUtils.stringToFloat(list.get(13)+""));
			rpcCalculateRequestData.getProduction().setPumpSettingDepth(StringManagerUtils.stringToFloat(list.get(14)+""));
			
			rpcCalculateRequestData.getPump().setBarrelType("组合泵".equals(list.get(15)+"")?"L":"H");
			rpcCalculateRequestData.getPump().setPumpGrade(StringManagerUtils.stringToInteger(list.get(16)+""));
			rpcCalculateRequestData.getPump().setPumpBoreDiameter( (float)(StringManagerUtils.stringToInteger(list.get(17)+"") *0.001) );
			rpcCalculateRequestData.getPump().setPlungerLength(StringManagerUtils.stringToFloat(list.get(18)+""));
			
			rpcCalculateRequestData.getTubingString().getEveryTubing().get(0).setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(19)+"") *0.001) );
			
			rpcCalculateRequestData.getCasingString().getEveryCasing().get(0).setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(20)+"") *0.001) );
			
			float rodStringLength1=0,rodStringLength2=0,rodStringLength3=0,rodStringLength4=0;
			rodStringLength1=StringManagerUtils.stringToFloat(list.get(24)+"");
			rodStringLength2=StringManagerUtils.stringToFloat(list.get(28)+"");
			rodStringLength3=StringManagerUtils.stringToFloat(list.get(32)+"");
			rodStringLength4=StringManagerUtils.stringToFloat(list.get(36)+"");
			if(rodStringLength1>0){
				RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
				everyRod.setGrade(list.get(21)+"");
				everyRod.setOutsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(22)+"") *0.001) );
				everyRod.setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(23)+"") *0.001) );
				everyRod.setLength(rodStringLength1);
				rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
			}
			if(rodStringLength2>0){
				RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
				everyRod.setGrade(list.get(25)+"");
				everyRod.setOutsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(26)+"") *0.001) );
				everyRod.setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(27)+"") *0.001) );
				everyRod.setLength(rodStringLength2);
				rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
			}
			if(rodStringLength3>0){
				RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
				everyRod.setGrade(list.get(29)+"");
				everyRod.setOutsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(30)+"") *0.001) );
				everyRod.setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(31)+"") *0.001) );
				everyRod.setLength(rodStringLength3);
				rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
			}
			if(rodStringLength4>0){
				RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
				everyRod.setGrade(list.get(33)+"");
				everyRod.setOutsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(34)+"") *0.001) );
				everyRod.setInsideDiameter( (float)(StringManagerUtils.stringToInteger(list.get(35)+"") *0.001) );
				everyRod.setLength(rodStringLength4);
				rpcCalculateRequestData.getRodString().getEveryRod().add(everyRod);
			}
			
			String crankRotationDirection=list.get(37)+"";
			float offsetAngleOfCrank=StringManagerUtils.stringToFloat(list.get(38)+"");
			float balanceWeight1=StringManagerUtils.stringToFloat(list.get(39)+"");
			float balanceWeight2=StringManagerUtils.stringToFloat(list.get(40)+"");
			float balanceWeight3=StringManagerUtils.stringToFloat(list.get(41)+"");
			float balanceWeight4=StringManagerUtils.stringToFloat(list.get(42)+"");
			float balanceWeight5=StringManagerUtils.stringToFloat(list.get(43)+"");
			float balanceWeight6=StringManagerUtils.stringToFloat(list.get(44)+"");
			float balanceWeight7=StringManagerUtils.stringToFloat(list.get(45)+"");
			float balanceWeight8=StringManagerUtils.stringToFloat(list.get(46)+"");
			
			if(balanceWeight1>0 || balanceWeight2>0 || balanceWeight3>0 || balanceWeight4>0 || balanceWeight1>5 || balanceWeight6>0 || balanceWeight7>0 || balanceWeight8>0){
				rpcCalculateRequestData.getPumpingUnit().setCrankRotationDirection("顺时针".equals(crankRotationDirection)?"Clockwise":"Anticlockwise");
				rpcCalculateRequestData.getPumpingUnit().setOffsetAngleOfCrank(offsetAngleOfCrank);
				if(balanceWeight1>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight1);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight2>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight2);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight3>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight3);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight4>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight4);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight5>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight5);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight6>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight6);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight7>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight7);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				if(balanceWeight8>0){
					RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
					everyBalance.setWeight(balanceWeight8);
					rpcCalculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
				}
				
				rpcCalculateRequestData.getManualIntervention().setCode(StringManagerUtils.stringToInteger(list.get(47)+""));
				rpcCalculateRequestData.getManualIntervention().setNetGrossRatio(StringManagerUtils.stringToFloat(list.get(48)+""));
				rpcCalculateRequestData.getManualIntervention().setNetGrossValue(StringManagerUtils.stringToFloat(list.get(49)+""));
				rpcCalculateRequestData.getManualIntervention().setLevelCorrectValue(StringManagerUtils.stringToFloat(list.get(50)+""));
			}
		}catch (Exception e) {
			rpcCalculateRequestData=null;
			e.printStackTrace();
			StringManagerUtils.printLogFile(logger, "error", e, "error");
		}
		return rpcCalculateRequestData;
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
