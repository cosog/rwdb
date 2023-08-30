package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.cosog.utils.DataProcessingUtils;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class SingleWellExceptionDataProcessingThread extends Thread{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String wellName;
	private DiagramExceptionData diagramExceptionData;
	public SingleWellExceptionDataProcessingThread(String wellName, DiagramExceptionData diagramExceptionData) {
		super();
		this.wellName = wellName;
		this.diagramExceptionData = diagramExceptionData;
	}
	public void run(){
		if(diagramExceptionData!=null && diagramExceptionData.getExceptionDataList()!=null && diagramExceptionData.getExceptionDataCount()>0 && StringManagerUtils.isNotNull(wellName)){
			DataRequestConfig dataRequestConfig=MemoryDataUtils.getDataReqConfig();
			DataResponseConfig dataResponseConfig=MemoryDataUtils.getDataResponseConfig();
			
			List<String> wellList=new ArrayList<>();
			wellList.add(wellName);
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			Connection prodConn = null;
			PreparedStatement prodPstmt = null;
			ResultSet prodRs = null;
			
			Connection writeBackConn = null;
			PreparedStatement writeBackPstmt = null;
			ResultSet writeBackRs = null;
			String sql="";
			String writeBackSql="";
			Gson gson = new Gson();
			try{
				if( (!DataRequestConfig.ConnectInfoEffective(dataRequestConfig.getProductionDataTable().getConnectInfo())) || DataRequestConfig.ConnectInfoEquals(dataRequestConfig.getProductionDataTable().getConnectInfo(), dataRequestConfig.getDiagramTable().getConnectInfo())  ){
					prodConn=OracleJdbcUtis.getDiagramConnection();//配置无效或者和功图数据表连接配置相同，获取功图数据表连接
				}else{
					prodConn=OracleJdbcUtis.getProductionDataConnection();
				}
				conn=OracleJdbcUtis.getDiagramConnection();
				writeBackConn=OracleJdbcUtis.getDataWriteBackConnection();
				if(prodConn!=null && conn!=null && writeBackConn!=null){
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
					
					RPCCalculateRequestData calculateRequestData=new RPCCalculateRequestData();
					calculateRequestData.init();
					
					sql=DataProcessingUtils.getProductionDataSql(wellList);
					prodPstmt = prodConn.prepareStatement(sql);
					prodRs=prodPstmt.executeQuery();
					while(rs.next()){
						try{
							calculateRequestData.setWellName(rs.getString(2));
							calculateRequestData.getFluidPVT().setCrudeOilDensity(rs.getFloat(3));
							calculateRequestData.getFluidPVT().setWaterDensity(rs.getFloat(4));
							calculateRequestData.getFluidPVT().setNaturalGasRelativeDensity(rs.getFloat(5));
							calculateRequestData.getFluidPVT().setSaturationPressure(rs.getFloat(6));
							
							calculateRequestData.getReservoir().setDepth(rs.getFloat(7));
							calculateRequestData.getReservoir().setTemperature(rs.getFloat(8));
							
							calculateRequestData.getProduction().setTubingPressure(rs.getFloat(9));
							calculateRequestData.getProduction().setCasingPressure(rs.getFloat(10));
							calculateRequestData.getProduction().setWellHeadTemperature(rs.getFloat(11));
							calculateRequestData.getProduction().setWaterCut(rs.getFloat(12));
							calculateRequestData.getProduction().setProductionGasOilRatio(rs.getFloat(13));
							calculateRequestData.getProduction().setProducingfluidLevel(rs.getFloat(14));
							calculateRequestData.getProduction().setPumpSettingDepth(rs.getFloat(15));
							
							calculateRequestData.getPump().setBarrelType("组合泵".equals(rs.getString(16))?"L":"H");
							calculateRequestData.getPump().setPumpGrade(rs.getInt(17));
							calculateRequestData.getPump().setPumpBoreDiameter( (float)(rs.getInt(18) *0.001) );
							calculateRequestData.getPump().setPlungerLength(rs.getFloat(19));
							
							RPCCalculateRequestData.EveryTubing everyTubing=new RPCCalculateRequestData.EveryTubing();
							everyTubing.setInsideDiameter( (float)(rs.getInt(20) *0.001) );
							calculateRequestData.getTubingString().getEveryTubing().add(everyTubing);
							
							RPCCalculateRequestData.EveryCasing everyCasing=new RPCCalculateRequestData.EveryCasing();
							everyCasing.setInsideDiameter( (float)(rs.getInt(21) *0.001) );
							calculateRequestData.getCasingString().getEveryCasing().add(everyCasing);
							
							float rodStringLength1=0,rodStringLength2=0,rodStringLength3=0,rodStringLength4=0;
							rodStringLength1=rs.getFloat(25);
							rodStringLength2=rs.getFloat(29);
							rodStringLength3=rs.getFloat(33);
							rodStringLength4=rs.getFloat(37);
							if(rodStringLength1>0){
								RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
								everyRod.setGrade(rs.getString(22));
								everyRod.setOutsideDiameter( (float)(rs.getInt(23) *0.001) );
								everyRod.setInsideDiameter( (float)(rs.getInt(24) *0.001) );
								everyRod.setLength(rodStringLength1);
								calculateRequestData.getRodString().getEveryRod().add(everyRod);
							}
							if(rodStringLength2>0){
								RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
								everyRod.setGrade(rs.getString(26));
								everyRod.setOutsideDiameter( (float)(rs.getInt(27) *0.001) );
								everyRod.setInsideDiameter( (float)(rs.getInt(28) *0.001) );
								everyRod.setLength(rodStringLength2);
								calculateRequestData.getRodString().getEveryRod().add(everyRod);
							}
							if(rodStringLength3>0){
								RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
								everyRod.setGrade(rs.getString(30));
								everyRod.setOutsideDiameter( (float)(rs.getInt(31) *0.001) );
								everyRod.setInsideDiameter( (float)(rs.getInt(32) *0.001) );
								everyRod.setLength(rodStringLength3);
								calculateRequestData.getRodString().getEveryRod().add(everyRod);
							}
							if(rodStringLength4>0){
								RPCCalculateRequestData.EveryRod everyRod=new RPCCalculateRequestData.EveryRod();
								everyRod.setGrade(rs.getString(34));
								everyRod.setOutsideDiameter( (float)(rs.getInt(35) *0.001) );
								everyRod.setInsideDiameter( (float)(rs.getInt(36) *0.001) );
								everyRod.setLength(rodStringLength4);
								calculateRequestData.getRodString().getEveryRod().add(everyRod);
							}
							
							String crankRotationDirection=rs.getString(38);
							float offsetAngleOfCrank=rs.getFloat(39);
							float balanceWeight1=rs.getInt(40);
							float balanceWeight2=rs.getInt(41);
							float balanceWeight3=rs.getInt(42);
							float balanceWeight4=rs.getInt(43);
							float balanceWeight5=rs.getInt(44);
							float balanceWeight6=rs.getInt(45);
							float balanceWeight7=rs.getInt(46);
							float balanceWeight8=rs.getInt(47);
							
							if(balanceWeight1>0 || balanceWeight2>0 || balanceWeight3>0 || balanceWeight4>0 || balanceWeight1>5 || balanceWeight6>0 || balanceWeight7>0 || balanceWeight8>0){
								calculateRequestData.getPumpingUnit().setCrankRotationDirection("顺时针".equals(crankRotationDirection)?"Clockwise":"Anticlockwise");
								calculateRequestData.getPumpingUnit().setOffsetAngleOfCrank(offsetAngleOfCrank);
								if(balanceWeight1>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight1);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight2>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight2);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight3>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight3);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight4>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight4);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight5>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight5);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight6>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight6);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight7>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight7);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								if(balanceWeight8>0){
									RPCCalculateRequestData.EveryBalance everyBalance=new RPCCalculateRequestData.EveryBalance();
									everyBalance.setWeight(balanceWeight8);
									calculateRequestData.getPumpingUnit().getBalance().getEveryBalance().add(everyBalance);
								}
								
								calculateRequestData.getManualIntervention().setCode(rs.getInt(48));
								calculateRequestData.getManualIntervention().setNetGrossRatio(rs.getFloat(49));
								calculateRequestData.getManualIntervention().setNetGrossValue(rs.getFloat(50));
								calculateRequestData.getManualIntervention().setLevelCorrectValue(rs.getFloat(51));
							}
						}catch (Exception e) {
							e.printStackTrace();
							logger.error("error", e);
						}
					}
					
					Iterator<ExceptionInfo> wellIterator = diagramExceptionData.getExceptionDataList().iterator();
					List<Long> reCalSucessList=new ArrayList<>();
					String diagramSql="";
					while(wellIterator.hasNext()){
						ExceptionInfo exceptionInfo = wellIterator.next();
						int resultStatus=exceptionInfo.getResultStatus();
						List<Long> list=exceptionInfo.getDiagramIdList();
						if(list==null || list.size()==0){
							wellIterator.remove();
						}else{
							 diagramSql="select "+diagramIdColumn+","
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
							 try{
								 pstmt = conn.prepareStatement(diagramSql);
								 rs=pstmt.executeQuery();
								 while(rs.next()){
										try{
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
													}
													reCalSucessList.add(diagramId);
												}
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
							 } catch (SQLException e1) {
									e1.printStackTrace();
									logger.error("error", e1);
									StringManagerUtils.printLog("Failed to query production data,sql:"+sql);
									logger.error("Failed to query production data,sql:"+sql);
								} catch (Exception e1) {
									e1.printStackTrace();
									logger.error("error", e1);
								}
						}
					}

					 //将计算成功的记录移除
					if(reCalSucessList.size()>0){
						diagramExceptionData.setReCalculateTimes(0);
						wellIterator = diagramExceptionData.getExceptionDataList().iterator();
						while(wellIterator.hasNext()){
							ExceptionInfo exceptionInfo = wellIterator.next();
							int resultStatus=exceptionInfo.getResultStatus();
							List<Long> list=exceptionInfo.getDiagramIdList();
							if(list==null || list.size()==0){
								wellIterator.remove();
							}else{
								Iterator<Long> it = list.iterator();
								while(it.hasNext()){
									if(StringManagerUtils.existOrNot_long(reCalSucessList, it.next())){
										it.remove();
									}
								}
							}
						}
					}else{
						diagramExceptionData.setReCalculateTimes(diagramExceptionData.getReCalculateTimes()+1);
					}
					diagramExceptionData.setLastCalculateTime(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.error("error", e1);
				StringManagerUtils.printLog("Failed to query production data,sql:"+sql);
				logger.error("Failed to query production data,sql:"+sql);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("error", e1);
			}finally{
				OracleJdbcUtis.closeDBConnection(conn, pstmt, rs);
				OracleJdbcUtis.closeDBConnection(prodConn, prodPstmt, prodRs);
				OracleJdbcUtis.closeDBConnection(writeBackConn, writeBackPstmt, writeBackRs);
			}
		}
	}
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
	}
	public DiagramExceptionData getDiagramExceptionData() {
		return diagramExceptionData;
	}
	public void setDiagramExceptionData(DiagramExceptionData diagramExceptionData) {
		this.diagramExceptionData = diagramExceptionData;
	}
}
