package com.cosog.model;

import com.cosog.utils.StringManagerUtils;

public class DataResponseConfig {

	private DiagramTableConfig DiagramTable;
    
    public static boolean ConnectInfoEffective(ConnectInfoConfig info){
    	boolean result=false;
    	if(info!=null
    			&& StringManagerUtils.isNotNull(info.getIP())
    			&& info.getPort()>0
    			&& StringManagerUtils.isNotNull(info.getInstanceName())
    			&& info.getVersion()>0
    			&& StringManagerUtils.isNotNull(info.getUser())
    			&& StringManagerUtils.isNotNull(info.getPassword())
    			){
    		result=true;
    	}
    	return result;
    }
    
    public static class ConnectInfoConfig
	{
	    private String IP;

	    private int Port;

	    private String InstanceName;

	    private int Version;

	    private String User;

	    private String Password;

	    public void setIP(String IP){
	        this.IP = IP;
	    }
	    public String getIP(){
	        return this.IP;
	    }
	    public void setPort(int Port){
	        this.Port = Port;
	    }
	    public int getPort(){
	        return this.Port;
	    }
	    public void setInstanceName(String InstanceName){
	        this.InstanceName = InstanceName;
	    }
	    public String getInstanceName(){
	        return this.InstanceName;
	    }
	    public void setVersion(int Version){
	        this.Version = Version;
	    }
	    public int getVersion(){
	        return this.Version;
	    }
	    public void setUser(String User){
	        this.User = User;
	    }
	    public String getUser(){
	        return this.User;
	    }
	    public void setPassword(String Password){
	        this.Password = Password;
	    }
	    public String getPassword(){
	        return this.Password;
	    }
	}
	
	public static class ColumnInfo
	{
	    private String Column;

	    private String Type;
	    
	    private float Ratio;

	    private boolean Enable;

	    public void setColumn(String Column){
	        this.Column = Column;
	    }
	    public String getColumn(){
	        return this.Column;
	    }
	    public void setType(String Type){
	        this.Type = Type;
	    }
	    public String getType(){
	        return this.Type;
	    }
	    public void setEnable(boolean Enable){
	        this.Enable = Enable;
	    }
	    public boolean getEnable(){
	        return this.Enable;
	    }
		public float getRatio() {
			return Ratio;
		}
		public void setRatio(float ratio) {
			Ratio = ratio;
		}
	}
	
	public static class DiagramTableColumn
	{
	    private ColumnInfo WellName;

	    private ColumnInfo AcqTime;

	    private ColumnInfo FMax;

	    private ColumnInfo FMin;
	    
	    private ColumnInfo UpperLoadLine;
	    
	    private ColumnInfo LowerLoadLine;

	    private ColumnInfo FullnessCoefficient;
	    
	    private ColumnInfo NoLiquidFullnessCoefficient;

	    private ColumnInfo ResultName;

	    private ColumnInfo ResultCode;

	    private ColumnInfo OptimizationSuggestion;

	    private ColumnInfo LiquidVolumetricProduction;

	    private ColumnInfo OilVolumetricProduction;

	    private ColumnInfo WaterVolumetricProduction;

	    private ColumnInfo LiquidWeightProduction;
	    
	    private ColumnInfo OilWeightProduction;

	    private ColumnInfo WaterWeightProduction;
	    
	    private ColumnInfo TheoreticalProduction;
	    
	    private ColumnInfo PumpEff;
	    
	    private ColumnInfo CalcProducingfluidLevel;
	    
	    private ColumnInfo LevelDifferenceValue;
	    
	    private ColumnInfo Submergence;
	    
	    private ColumnInfo DownStrokeIMax;
	    
	    private ColumnInfo UpStrokeIMax;
	    
	    private ColumnInfo IDegreeBalance;
	    
	    private ColumnInfo DownStrokeWattMax;
	    
	    private ColumnInfo UpStrokeWattMax;
	    
	    private ColumnInfo WattDegreeBalance;
	    
	    private ColumnInfo DeltaRadius;
	    
	    private ColumnInfo WellDownSystemEfficiency;
	    
	    private ColumnInfo SurfaceSystemEfficiency;
	    
	    private ColumnInfo SystemEfficiency;
	    
	    private ColumnInfo EnergyPer100mLift;

		public ColumnInfo getWellName() {
			return WellName;
		}

		public void setWellName(ColumnInfo wellName) {
			WellName = wellName;
		}

		public ColumnInfo getAcqTime() {
			return AcqTime;
		}

		public void setAcqTime(ColumnInfo acqTime) {
			AcqTime = acqTime;
		}

		public ColumnInfo getFMax() {
			return FMax;
		}

		public void setFMax(ColumnInfo fMax) {
			FMax = fMax;
		}

		public ColumnInfo getFMin() {
			return FMin;
		}

		public void setFMin(ColumnInfo fMin) {
			FMin = fMin;
		}

		public ColumnInfo getFullnessCoefficient() {
			return FullnessCoefficient;
		}

		public void setFullnessCoefficient(ColumnInfo fullnessCoefficient) {
			FullnessCoefficient = fullnessCoefficient;
		}

		public ColumnInfo getResultName() {
			return ResultName;
		}

		public void setResultName(ColumnInfo reusltName) {
			ResultName = reusltName;
		}

		public ColumnInfo getResultCode() {
			return ResultCode;
		}

		public void setResultCode(ColumnInfo reusltCode) {
			ResultCode = reusltCode;
		}

		public ColumnInfo getOptimizationSuggestion() {
			return OptimizationSuggestion;
		}

		public void setOptimizationSuggestion(ColumnInfo optimizationSuggestion) {
			OptimizationSuggestion = optimizationSuggestion;
		}

		public ColumnInfo getLiquidVolumetricProduction() {
			return LiquidVolumetricProduction;
		}

		public void setLiquidVolumetricProduction(ColumnInfo liquidVolumetricProduction) {
			LiquidVolumetricProduction = liquidVolumetricProduction;
		}

		public ColumnInfo getOilVolumetricProduction() {
			return OilVolumetricProduction;
		}

		public void setOilVolumetricProduction(ColumnInfo oilVolumetricProduction) {
			OilVolumetricProduction = oilVolumetricProduction;
		}

		public ColumnInfo getWaterVolumetricProduction() {
			return WaterVolumetricProduction;
		}

		public void setWaterVolumetricProduction(ColumnInfo waterVolumetricProduction) {
			WaterVolumetricProduction = waterVolumetricProduction;
		}

		public ColumnInfo getLiquidWeightProduction() {
			return LiquidWeightProduction;
		}

		public void setLiquidWeightProduction(ColumnInfo liquidWeightProduction) {
			LiquidWeightProduction = liquidWeightProduction;
		}

		public ColumnInfo getWaterWeightProduction() {
			return WaterWeightProduction;
		}

		public void setWaterWeightProduction(ColumnInfo waterWeightProduction) {
			WaterWeightProduction = waterWeightProduction;
		}

		public ColumnInfo getOilWeightProduction() {
			return OilWeightProduction;
		}

		public void setOilWeightProduction(ColumnInfo oilWeightProduction) {
			OilWeightProduction = oilWeightProduction;
		}

		public ColumnInfo getUpperLoadLine() {
			return UpperLoadLine;
		}

		public void setUpperLoadLine(ColumnInfo upperLoadLine) {
			UpperLoadLine = upperLoadLine;
		}

		public ColumnInfo getLowerLoadLine() {
			return LowerLoadLine;
		}

		public void setLowerLoadLine(ColumnInfo lowerLoadLine) {
			LowerLoadLine = lowerLoadLine;
		}

		public ColumnInfo getNoLiquidFullnessCoefficient() {
			return NoLiquidFullnessCoefficient;
		}

		public void setNoLiquidFullnessCoefficient(ColumnInfo noLiquidFullnessCoefficient) {
			NoLiquidFullnessCoefficient = noLiquidFullnessCoefficient;
		}

		public ColumnInfo getTheoreticalProduction() {
			return TheoreticalProduction;
		}

		public void setTheoreticalProduction(ColumnInfo theoreticalProduction) {
			TheoreticalProduction = theoreticalProduction;
		}

		public ColumnInfo getPumpEff() {
			return PumpEff;
		}

		public void setPumpEff(ColumnInfo pumpEff) {
			PumpEff = pumpEff;
		}

		public ColumnInfo getCalcProducingfluidLevel() {
			return CalcProducingfluidLevel;
		}

		public void setCalcProducingfluidLevel(ColumnInfo calcProducingfluidLevel) {
			CalcProducingfluidLevel = calcProducingfluidLevel;
		}

		public ColumnInfo getLevelDifferenceValue() {
			return LevelDifferenceValue;
		}

		public void setLevelDifferenceValue(ColumnInfo levelDifferenceValue) {
			LevelDifferenceValue = levelDifferenceValue;
		}

		public ColumnInfo getSubmergence() {
			return Submergence;
		}

		public void setSubmergence(ColumnInfo submergence) {
			Submergence = submergence;
		}

		public ColumnInfo getDownStrokeIMax() {
			return DownStrokeIMax;
		}

		public void setDownStrokeIMax(ColumnInfo downStrokeIMax) {
			DownStrokeIMax = downStrokeIMax;
		}

		public ColumnInfo getUpStrokeIMax() {
			return UpStrokeIMax;
		}

		public void setUpStrokeIMax(ColumnInfo upStrokeIMax) {
			UpStrokeIMax = upStrokeIMax;
		}

		public ColumnInfo getIDegreeBalance() {
			return IDegreeBalance;
		}

		public void setIDegreeBalance(ColumnInfo iDegreeBalance) {
			IDegreeBalance = iDegreeBalance;
		}

		public ColumnInfo getDownStrokeWattMax() {
			return DownStrokeWattMax;
		}

		public void setDownStrokeWattMax(ColumnInfo downStrokeWattMax) {
			DownStrokeWattMax = downStrokeWattMax;
		}

		public ColumnInfo getUpStrokeWattMax() {
			return UpStrokeWattMax;
		}

		public void setUpStrokeWattMax(ColumnInfo upStrokeWattMax) {
			UpStrokeWattMax = upStrokeWattMax;
		}

		public ColumnInfo getWattDegreeBalance() {
			return WattDegreeBalance;
		}

		public void setWattDegreeBalance(ColumnInfo wattDegreeBalance) {
			WattDegreeBalance = wattDegreeBalance;
		}

		public ColumnInfo getDeltaRadius() {
			return DeltaRadius;
		}

		public void setDeltaRadius(ColumnInfo deltaRadius) {
			DeltaRadius = deltaRadius;
		}

		public ColumnInfo getWellDownSystemEfficiency() {
			return WellDownSystemEfficiency;
		}

		public void setWellDownSystemEfficiency(ColumnInfo wellDownSystemEfficiency) {
			WellDownSystemEfficiency = wellDownSystemEfficiency;
		}

		public ColumnInfo getSurfaceSystemEfficiency() {
			return SurfaceSystemEfficiency;
		}

		public void setSurfaceSystemEfficiency(ColumnInfo surfaceSystemEfficiency) {
			SurfaceSystemEfficiency = surfaceSystemEfficiency;
		}

		public ColumnInfo getSystemEfficiency() {
			return SystemEfficiency;
		}

		public void setSystemEfficiency(ColumnInfo systemEfficiency) {
			SystemEfficiency = systemEfficiency;
		}

		public ColumnInfo getEnergyPer100mLift() {
			return EnergyPer100mLift;
		}

		public void setEnergyPer100mLift(ColumnInfo energyPer100mLift) {
			EnergyPer100mLift = energyPer100mLift;
		}
	}
	
	public static class DiagramTableInfo
	{
	    private String Name;

	    private DiagramTableColumn Columns;

		public DiagramTableColumn getColumns() {
			return Columns;
		}

		public void setColumns(DiagramTableColumn columns) {
			Columns = columns;
		}

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}
	}

	public static class DiagramTableConfig{
		private String WriteType;
		private ConnectInfoConfig ConnectInfo;
		private DiagramTableInfo TableInfo;
		public String getWriteType() {
			return WriteType;
		}
		public void setWriteType(String writeType) {
			WriteType = writeType;
		}
		public ConnectInfoConfig getConnectInfo() {
			return ConnectInfo;
		}
		public void setConnectInfo(ConnectInfoConfig connectInfo) {
			ConnectInfo = connectInfo;
		}
		public DiagramTableInfo getTableInfo() {
			return TableInfo;
		}
		public void setTableInfo(DiagramTableInfo tableInfo) {
			TableInfo = tableInfo;
		}
		
	}

	public DiagramTableConfig getDiagramTable() {
		return DiagramTable;
	}

	public void setDiagramTable(DiagramTableConfig diagramTable) {
		DiagramTable = diagramTable;
	}
	
}
