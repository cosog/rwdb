package com.cosog.model;

import com.cosog.utils.StringManagerUtils;

public class DataRequestConfig {
	
	private DiagramTableConfig DiagramTable;

    private AuxDataTableConfig AuxTable;
    
    public static boolean ConnectInfoEquals(ConnectInfoConfig info1,ConnectInfoConfig info2){
    	boolean result=false;
    	if(info1!=null && info2!=null 
    			&& info1.getIP().equalsIgnoreCase(info2.getIP())
    			&& info1.getPort()==info2.getPort()
    			&& info1.getInstanceName().equalsIgnoreCase(info2.getInstanceName())
    			&& info1.getVersion()==info2.getVersion()
    			&& info1.getUser().equalsIgnoreCase(info2.getUser())
    			&& info1.getPassword().equalsIgnoreCase(info2.getPassword())
    			){
    		result=true;
    	}
    	return result;
    }
    
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
	}
	
	public static class FESDiagramTableColumns
	{
		private ColumnInfo DiagramId;
		
		private ColumnInfo WellName;

	    private ColumnInfo AcqTime;

	    private ColumnInfo Stroke;

	    private ColumnInfo SPM;

	    private ColumnInfo PointCount;

	    private ColumnInfo S;

	    private ColumnInfo F;

	    private ColumnInfo I;

	    private ColumnInfo KWatt;

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

		public ColumnInfo getStroke() {
			return Stroke;
		}

		public void setStroke(ColumnInfo stroke) {
			Stroke = stroke;
		}

		public ColumnInfo getSPM() {
			return SPM;
		}

		public void setSPM(ColumnInfo sPM) {
			SPM = sPM;
		}

		public ColumnInfo getPointCount() {
			return PointCount;
		}

		public void setPointCount(ColumnInfo pointCount) {
			PointCount = pointCount;
		}

		public ColumnInfo getS() {
			return S;
		}

		public void setS(ColumnInfo s) {
			S = s;
		}

		public ColumnInfo getF() {
			return F;
		}

		public void setF(ColumnInfo f) {
			F = f;
		}

		public ColumnInfo getI() {
			return I;
		}

		public void setI(ColumnInfo i) {
			I = i;
		}

		public ColumnInfo getKWatt() {
			return KWatt;
		}

		public void setKWatt(ColumnInfo kWatt) {
			KWatt = kWatt;
		}

		public ColumnInfo getDiagramId() {
			return DiagramId;
		}

		public void setDiagramId(ColumnInfo diagramId) {
			DiagramId = diagramId;
		}
	}
	
	public static class FESDiagramTableStructure
	{
	    private String Name;

	    private FESDiagramTableColumns Columns;

	    public void setName(String Name){
	        this.Name = Name;
	    }
	    public String getName(){
	        return this.Name;
	    }
	    public void setColumns(FESDiagramTableColumns Columns){
	        this.Columns = Columns;
	    }
	    public FESDiagramTableColumns getColumns(){
	        return this.Columns;
	    }
	}
	
	public static class DiagramTableConfig
	{

	    private ConnectInfoConfig ConnectInfo;

	    private FESDiagramTableStructure TableInfo;
	    
	    public void setConnectInfo(ConnectInfoConfig ConnectInfo){
	        this.ConnectInfo = ConnectInfo;
	    }
	    public ConnectInfoConfig getConnectInfo(){
	        return this.ConnectInfo;
	    }
	    public void setTableInfo(FESDiagramTableStructure TableInfo){
	        this.TableInfo = TableInfo;
	    }
	    public FESDiagramTableStructure getTableInfo(){
	        return this.TableInfo;
	    }
	}
	
	public static class AuxDataTableColumns
	{
	    private ColumnInfo WellId;

	    private ColumnInfo WellName;

	    private ColumnInfo SaveTime;

	    private ColumnInfo CrudeOilDensity;

	    private ColumnInfo WaterDensity;

	    private ColumnInfo NaturalGasRelativeDensity;

	    private ColumnInfo SaturationPressure;

	    private ColumnInfo ReservoirDepth;

	    private ColumnInfo ReservoirTemperature;

	    private ColumnInfo TubingPressure;

	    private ColumnInfo CasingPressure;

	    private ColumnInfo WellHeadTemperature;

	    private ColumnInfo WaterCut;

	    private ColumnInfo ProductionGasOilRatio;

	    private ColumnInfo ProducingfluidLevel;

	    private ColumnInfo PumpSettingDepth;

	    private ColumnInfo BarrelType;

	    private ColumnInfo PumpGrade;

	    private ColumnInfo PumpBoreDiameter;

	    private ColumnInfo PlungerLength;

	    private ColumnInfo TubingStringInsideDiameter;

	    private ColumnInfo CasingStringInsideDiameter;

	    private ColumnInfo RodStringGrade1;

	    private ColumnInfo RodStringOutsideDiameter1;

	    private ColumnInfo RodStringInsideDiameter1;

	    private ColumnInfo RodStringLength1;

	    private ColumnInfo RodStringGrade2;

	    private ColumnInfo RodStringOutsideDiameter2;

	    private ColumnInfo RodStringInsideDiameter2;

	    private ColumnInfo RodStringLength2;

	    private ColumnInfo RodStringGrade3;

	    private ColumnInfo RodStringOutsideDiameter3;

	    private ColumnInfo RodStringInsideDiameter3;

	    private ColumnInfo RodStringLength3;

	    private ColumnInfo RodStringGrade4;

	    private ColumnInfo RodStringOutsideDiameter4;

	    private ColumnInfo RodStringInsideDiameter4;

	    private ColumnInfo RodStringLength4;

	    private ColumnInfo CrankRotationDirection;

	    private ColumnInfo OffsetAngleOfCrank;

	    private ColumnInfo BalanceWeight1;

	    private ColumnInfo BalanceWeight2;

	    private ColumnInfo BalanceWeight3;

	    private ColumnInfo BalanceWeight4;

	    private ColumnInfo BalanceWeight5;

	    private ColumnInfo BalanceWeight6;

	    private ColumnInfo BalanceWeight7;

	    private ColumnInfo BalanceWeight8;

	    private ColumnInfo ManualInterventionCode;

	    private ColumnInfo NetGrossRatio;

	    private ColumnInfo NetGrossValue;

	    private ColumnInfo LevelCorrectValue;

		public ColumnInfo getWellId() {
			return WellId;
		}

		public void setWellId(ColumnInfo wellId) {
			WellId = wellId;
		}

		public ColumnInfo getWellName() {
			return WellName;
		}

		public void setWellName(ColumnInfo wellName) {
			WellName = wellName;
		}

		public ColumnInfo getSaveTime() {
			return SaveTime;
		}

		public void setSaveTime(ColumnInfo saveTime) {
			SaveTime = saveTime;
		}

		public ColumnInfo getCrudeOilDensity() {
			return CrudeOilDensity;
		}

		public void setCrudeOilDensity(ColumnInfo crudeOilDensity) {
			CrudeOilDensity = crudeOilDensity;
		}

		public ColumnInfo getWaterDensity() {
			return WaterDensity;
		}

		public void setWaterDensity(ColumnInfo waterDensity) {
			WaterDensity = waterDensity;
		}

		public ColumnInfo getNaturalGasRelativeDensity() {
			return NaturalGasRelativeDensity;
		}

		public void setNaturalGasRelativeDensity(ColumnInfo naturalGasRelativeDensity) {
			NaturalGasRelativeDensity = naturalGasRelativeDensity;
		}

		public ColumnInfo getSaturationPressure() {
			return SaturationPressure;
		}

		public void setSaturationPressure(ColumnInfo saturationPressure) {
			SaturationPressure = saturationPressure;
		}

		public ColumnInfo getReservoirDepth() {
			return ReservoirDepth;
		}

		public void setReservoirDepth(ColumnInfo reservoirDepth) {
			ReservoirDepth = reservoirDepth;
		}

		public ColumnInfo getReservoirTemperature() {
			return ReservoirTemperature;
		}

		public void setReservoirTemperature(ColumnInfo reservoirTemperature) {
			ReservoirTemperature = reservoirTemperature;
		}

		public ColumnInfo getTubingPressure() {
			return TubingPressure;
		}

		public void setTubingPressure(ColumnInfo tubingPressure) {
			TubingPressure = tubingPressure;
		}

		public ColumnInfo getCasingPressure() {
			return CasingPressure;
		}

		public void setCasingPressure(ColumnInfo casingPressure) {
			CasingPressure = casingPressure;
		}

		public ColumnInfo getWellHeadTemperature() {
			return WellHeadTemperature;
		}

		public void setWellHeadTemperature(ColumnInfo wellHeadTemperature) {
			WellHeadTemperature = wellHeadTemperature;
		}

		public ColumnInfo getWaterCut() {
			return WaterCut;
		}

		public void setWaterCut(ColumnInfo waterCut) {
			WaterCut = waterCut;
		}

		public ColumnInfo getProductionGasOilRatio() {
			return ProductionGasOilRatio;
		}

		public void setProductionGasOilRatio(ColumnInfo productionGasOilRatio) {
			ProductionGasOilRatio = productionGasOilRatio;
		}

		public ColumnInfo getProducingfluidLevel() {
			return ProducingfluidLevel;
		}

		public void setProducingfluidLevel(ColumnInfo producingfluidLevel) {
			ProducingfluidLevel = producingfluidLevel;
		}

		public ColumnInfo getPumpSettingDepth() {
			return PumpSettingDepth;
		}

		public void setPumpSettingDepth(ColumnInfo pumpSettingDepth) {
			PumpSettingDepth = pumpSettingDepth;
		}

		public ColumnInfo getBarrelType() {
			return BarrelType;
		}

		public void setBarrelType(ColumnInfo barrelType) {
			BarrelType = barrelType;
		}

		public ColumnInfo getPumpGrade() {
			return PumpGrade;
		}

		public void setPumpGrade(ColumnInfo pumpGrade) {
			PumpGrade = pumpGrade;
		}

		public ColumnInfo getPumpBoreDiameter() {
			return PumpBoreDiameter;
		}

		public void setPumpBoreDiameter(ColumnInfo pumpBoreDiameter) {
			PumpBoreDiameter = pumpBoreDiameter;
		}

		public ColumnInfo getPlungerLength() {
			return PlungerLength;
		}

		public void setPlungerLength(ColumnInfo plungerLength) {
			PlungerLength = plungerLength;
		}

		public ColumnInfo getTubingStringInsideDiameter() {
			return TubingStringInsideDiameter;
		}

		public void setTubingStringInsideDiameter(ColumnInfo tubingStringInsideDiameter) {
			TubingStringInsideDiameter = tubingStringInsideDiameter;
		}

		public ColumnInfo getCasingStringInsideDiameter() {
			return CasingStringInsideDiameter;
		}

		public void setCasingStringInsideDiameter(ColumnInfo casingStringInsideDiameter) {
			CasingStringInsideDiameter = casingStringInsideDiameter;
		}

		public ColumnInfo getRodStringGrade1() {
			return RodStringGrade1;
		}

		public void setRodStringGrade1(ColumnInfo rodStringGrade1) {
			RodStringGrade1 = rodStringGrade1;
		}

		public ColumnInfo getRodStringOutsideDiameter1() {
			return RodStringOutsideDiameter1;
		}

		public void setRodStringOutsideDiameter1(ColumnInfo rodStringOutsideDiameter1) {
			RodStringOutsideDiameter1 = rodStringOutsideDiameter1;
		}

		public ColumnInfo getRodStringInsideDiameter1() {
			return RodStringInsideDiameter1;
		}

		public void setRodStringInsideDiameter1(ColumnInfo rodStringInsideDiameter1) {
			RodStringInsideDiameter1 = rodStringInsideDiameter1;
		}

		public ColumnInfo getRodStringLength1() {
			return RodStringLength1;
		}

		public void setRodStringLength1(ColumnInfo rodStringLength1) {
			RodStringLength1 = rodStringLength1;
		}

		public ColumnInfo getRodStringGrade2() {
			return RodStringGrade2;
		}

		public void setRodStringGrade2(ColumnInfo rodStringGrade2) {
			RodStringGrade2 = rodStringGrade2;
		}

		public ColumnInfo getRodStringOutsideDiameter2() {
			return RodStringOutsideDiameter2;
		}

		public void setRodStringOutsideDiameter2(ColumnInfo rodStringOutsideDiameter2) {
			RodStringOutsideDiameter2 = rodStringOutsideDiameter2;
		}

		public ColumnInfo getRodStringInsideDiameter2() {
			return RodStringInsideDiameter2;
		}

		public void setRodStringInsideDiameter2(ColumnInfo rodStringInsideDiameter2) {
			RodStringInsideDiameter2 = rodStringInsideDiameter2;
		}

		public ColumnInfo getRodStringLength2() {
			return RodStringLength2;
		}

		public void setRodStringLength2(ColumnInfo rodStringLength2) {
			RodStringLength2 = rodStringLength2;
		}

		public ColumnInfo getRodStringGrade3() {
			return RodStringGrade3;
		}

		public void setRodStringGrade3(ColumnInfo rodStringGrade3) {
			RodStringGrade3 = rodStringGrade3;
		}

		public ColumnInfo getRodStringOutsideDiameter3() {
			return RodStringOutsideDiameter3;
		}

		public void setRodStringOutsideDiameter3(ColumnInfo rodStringOutsideDiameter3) {
			RodStringOutsideDiameter3 = rodStringOutsideDiameter3;
		}

		public ColumnInfo getRodStringInsideDiameter3() {
			return RodStringInsideDiameter3;
		}

		public void setRodStringInsideDiameter3(ColumnInfo rodStringInsideDiameter3) {
			RodStringInsideDiameter3 = rodStringInsideDiameter3;
		}

		public ColumnInfo getRodStringLength3() {
			return RodStringLength3;
		}

		public void setRodStringLength3(ColumnInfo rodStringLength3) {
			RodStringLength3 = rodStringLength3;
		}

		public ColumnInfo getRodStringGrade4() {
			return RodStringGrade4;
		}

		public void setRodStringGrade4(ColumnInfo rodStringGrade4) {
			RodStringGrade4 = rodStringGrade4;
		}

		public ColumnInfo getRodStringOutsideDiameter4() {
			return RodStringOutsideDiameter4;
		}

		public void setRodStringOutsideDiameter4(ColumnInfo rodStringOutsideDiameter4) {
			RodStringOutsideDiameter4 = rodStringOutsideDiameter4;
		}

		public ColumnInfo getRodStringInsideDiameter4() {
			return RodStringInsideDiameter4;
		}

		public void setRodStringInsideDiameter4(ColumnInfo rodStringInsideDiameter4) {
			RodStringInsideDiameter4 = rodStringInsideDiameter4;
		}

		public ColumnInfo getRodStringLength4() {
			return RodStringLength4;
		}

		public void setRodStringLength4(ColumnInfo rodStringLength4) {
			RodStringLength4 = rodStringLength4;
		}

		public ColumnInfo getCrankRotationDirection() {
			return CrankRotationDirection;
		}

		public void setCrankRotationDirection(ColumnInfo crankRotationDirection) {
			CrankRotationDirection = crankRotationDirection;
		}

		public ColumnInfo getOffsetAngleOfCrank() {
			return OffsetAngleOfCrank;
		}

		public void setOffsetAngleOfCrank(ColumnInfo offsetAngleOfCrank) {
			OffsetAngleOfCrank = offsetAngleOfCrank;
		}

		public ColumnInfo getBalanceWeight1() {
			return BalanceWeight1;
		}

		public void setBalanceWeight1(ColumnInfo balanceWeight1) {
			BalanceWeight1 = balanceWeight1;
		}

		public ColumnInfo getBalanceWeight2() {
			return BalanceWeight2;
		}

		public void setBalanceWeight2(ColumnInfo balanceWeight2) {
			BalanceWeight2 = balanceWeight2;
		}

		public ColumnInfo getBalanceWeight3() {
			return BalanceWeight3;
		}

		public void setBalanceWeight3(ColumnInfo balanceWeight3) {
			BalanceWeight3 = balanceWeight3;
		}

		public ColumnInfo getBalanceWeight4() {
			return BalanceWeight4;
		}

		public void setBalanceWeight4(ColumnInfo balanceWeight4) {
			BalanceWeight4 = balanceWeight4;
		}

		public ColumnInfo getBalanceWeight5() {
			return BalanceWeight5;
		}

		public void setBalanceWeight5(ColumnInfo balanceWeight5) {
			BalanceWeight5 = balanceWeight5;
		}

		public ColumnInfo getBalanceWeight6() {
			return BalanceWeight6;
		}

		public void setBalanceWeight6(ColumnInfo balanceWeight6) {
			BalanceWeight6 = balanceWeight6;
		}

		public ColumnInfo getBalanceWeight7() {
			return BalanceWeight7;
		}

		public void setBalanceWeight7(ColumnInfo balanceWeight7) {
			BalanceWeight7 = balanceWeight7;
		}

		public ColumnInfo getBalanceWeight8() {
			return BalanceWeight8;
		}

		public void setBalanceWeight8(ColumnInfo balanceWeight8) {
			BalanceWeight8 = balanceWeight8;
		}

		public ColumnInfo getManualInterventionCode() {
			return ManualInterventionCode;
		}

		public void setManualInterventionCode(ColumnInfo manualInterventionCode) {
			ManualInterventionCode = manualInterventionCode;
		}

		public ColumnInfo getNetGrossRatio() {
			return NetGrossRatio;
		}

		public void setNetGrossRatio(ColumnInfo netGrossRatio) {
			NetGrossRatio = netGrossRatio;
		}

		public ColumnInfo getNetGrossValue() {
			return NetGrossValue;
		}

		public void setNetGrossValue(ColumnInfo netGrossValue) {
			NetGrossValue = netGrossValue;
		}

		public ColumnInfo getLevelCorrectValue() {
			return LevelCorrectValue;
		}

		public void setLevelCorrectValue(ColumnInfo levelCorrectValue) {
			LevelCorrectValue = levelCorrectValue;
		}
	}

	public static class AuxDataTableStructure
	{
	    private String Name;

	    private AuxDataTableColumns Columns;

	    public void setName(String Name){
	        this.Name = Name;
	    }
	    public String getName(){
	        return this.Name;
	    }
		public AuxDataTableColumns getColumns() {
			return Columns;
		}
		public void setColumns(AuxDataTableColumns columns) {
			Columns = columns;
		}
	}
	
	public static class AuxDataTableConfig
	{

	    private ConnectInfoConfig ConnectInfo;

	    private AuxDataTableStructure TableInfo;

	    public void setConnectInfo(ConnectInfoConfig ConnectInfo){
	        this.ConnectInfo = ConnectInfo;
	    }
	    public ConnectInfoConfig getConnectInfo(){
	        return this.ConnectInfo;
	    }
		public AuxDataTableStructure getTableInfo() {
			return TableInfo;
		}
		public void setTableInfo(AuxDataTableStructure tableInfo) {
			TableInfo = tableInfo;
		}
	}

	public DiagramTableConfig getDiagramTable() {
		return DiagramTable;
	}

	public void setDiagramTable(DiagramTableConfig diagramTable) {
		DiagramTable = diagramTable;
	}

	public AuxDataTableConfig getAuxTable() {
		return AuxTable;
	}

	public void setAuxTable(AuxDataTableConfig auxTable) {
		AuxTable = auxTable;
	}

}
