package com.cosog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PCPCalculateRequestData  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String AKString;                                              //秘钥
	private String WellName;                                              //井名
	private String Scene;
	private String AcqTime;                                       //采集时间
	private float RPM;                                                    //螺杆泵井转�??
	private FluidPVT FluidPVT;                                            //流体PVT物�??
	private Reservoir Reservoir;                                          //油藏物�??
	private RodString RodString;                                          //抽油杆参�?
	private TubingString TubingString;                                    //油管参数
	private Pump Pump;                                                    //抽油泵参�?
	private CasingString CasingString;                                    //套管参数
	private Production Production;                      //生产数据
	private ManualIntervention ManualIntervention;                        //人工干预
	
	public PCPCalculateRequestData(String aKString, String WellName,
			com.cosog.model.PCPCalculateRequestData.FluidPVT fluidPVT,
			com.cosog.model.PCPCalculateRequestData.Reservoir reservoir,
			com.cosog.model.PCPCalculateRequestData.RodString rodString,
			com.cosog.model.PCPCalculateRequestData.TubingString tubingString,
			com.cosog.model.PCPCalculateRequestData.Pump pump,
			com.cosog.model.PCPCalculateRequestData.CasingString casingString,
			com.cosog.model.PCPCalculateRequestData.Production production,
			com.cosog.model.PCPCalculateRequestData.ManualIntervention manualIntervention) {
		super();
		AKString = aKString;
		this.WellName = WellName;
		FluidPVT = fluidPVT;
		Reservoir = reservoir;
		RodString = rodString;
		TubingString = tubingString;
		Pump = pump;
		CasingString = casingString;
		Production = production;
		ManualIntervention = manualIntervention;
	}

	public void init(){
		this.setFluidPVT(new FluidPVT());
    	this.setReservoir(new Reservoir());
    	
    	this.setTubingString(new TubingString());
    	this.getTubingString().setEveryTubing(new ArrayList<EveryTubing>());
    	this.getTubingString().getEveryTubing().add(new EveryTubing());
    	
    	this.setCasingString(new CasingString());
    	this.getCasingString().setEveryCasing(new ArrayList<EveryCasing>());
    	this.getCasingString().getEveryCasing().add(new EveryCasing());
    	
    	this.setRodString(new RodString());
    	this.getRodString().setEveryRod(new ArrayList<EveryRod>());
    	
    	this.setPump(new Pump());
    	this.setProduction(new Production());
    	
    	this.setManualIntervention(new ManualIntervention());
	}

	public String getAKString() {
		return AKString;
	}


	public void setAKString(String aKString) {
		AKString = aKString;
	}


	public PCPCalculateRequestData() {
		super();
	}


	public String getWellName() {
		return WellName;
	}


	public void setWellName(String WellName) {
		this.WellName = WellName;
	}


	public FluidPVT getFluidPVT() {
		return FluidPVT;
	}


	public void setFluidPVT(FluidPVT fluidPVT) {
		FluidPVT = fluidPVT;
	}


	public Reservoir getReservoir() {
		return Reservoir;
	}


	public void setReservoir(Reservoir reservoir) {
		Reservoir = reservoir;
	}


	public RodString getRodString() {
		return RodString;
	}


	public void setRodString(RodString rodString) {
		RodString = rodString;
	}


	public TubingString getTubingString() {
		return TubingString;
	}


	public void setTubingString(TubingString tubingString) {
		TubingString = tubingString;
	}


	public Pump getPump() {
		return Pump;
	}


	public void setPump(Pump pump) {
		Pump = pump;
	}


	public CasingString getCasingString() {
		return CasingString;
	}


	public void setCasingString(CasingString casingString) {
		CasingString = casingString;
	}


	public Production getProduction() {
		return Production;
	}


	public void setProduction(Production production) {
		Production = production;
	}

	public ManualIntervention getManualIntervention() {
		return ManualIntervention;
	}

	public void setManualIntervention(ManualIntervention manualIntervention) {
		ManualIntervention = manualIntervention;
	}

	public String getAcqTime() {
		return AcqTime;
	}


	public void setAcqTime(String acqTime) {
		AcqTime = acqTime;
	}


	public float getRPM() {
		return RPM;
	}


	public void setRPM(float rPM) {
		RPM = rPM;
	}
	
	public static class FluidPVT implements Serializable {
		private static final long serialVersionUID = 1L;
	    private float CrudeOilDensity;

	    private float WaterDensity;

	    private float NaturalGasRelativeDensity;

	    private float SaturationPressure;

	    public void setCrudeOilDensity(float CrudeOilDensity){
	        this.CrudeOilDensity = CrudeOilDensity;
	    }
	    public float getCrudeOilDensity(){
	        return this.CrudeOilDensity;
	    }
	    public void setWaterDensity(float WaterDensity){
	        this.WaterDensity = WaterDensity;
	    }
	    public float getWaterDensity(){
	        return this.WaterDensity;
	    }
	    public void setNaturalGasRelativeDensity(float NaturalGasRelativeDensity){
	        this.NaturalGasRelativeDensity = NaturalGasRelativeDensity;
	    }
	    public float getNaturalGasRelativeDensity(){
	        return this.NaturalGasRelativeDensity;
	    }
	    public void setSaturationPressure(float SaturationPressure){
	        this.SaturationPressure = SaturationPressure;
	    }
	    public float getSaturationPressure(){
	        return this.SaturationPressure;
	    }
	}
	
	public static class Reservoir implements Serializable {
		private static final long serialVersionUID = 1L;
	    private float Depth;

	    private float Temperature;

	    public void setDepth(float Depth){
	        this.Depth = Depth;
	    }
	    public float getDepth(){
	        return this.Depth;
	    }
	    public void setTemperature(float Temperature){
	        this.Temperature = Temperature;
	    }
	    public float getTemperature(){
	        return this.Temperature;
	    }
	}
	
	public static class WellboreTrajectory implements Serializable {
		private static final long serialVersionUID = 1L;
	    private List<Float> MeasuringDepth;

	    private List<Float> DeviationAngle;

	    private List<Float> AzimuthAngle;

	    public void setMeasuringDepth(List<Float> MeasuringDepth){
	        this.MeasuringDepth = MeasuringDepth;
	    }
	    public List<Float> getMeasuringDepth(){
	        return this.MeasuringDepth;
	    }
	    public void setDeviationAngle(List<Float> DeviationAngle){
	        this.DeviationAngle = DeviationAngle;
	    }
	    public List<Float> getDeviationAngle(){
	        return this.DeviationAngle;
	    }
	    public void setAzimuthAngle(List<Float> AzimuthAngle){
	        this.AzimuthAngle = AzimuthAngle;
	    }
	    public List<Float> getAzimuthAngle(){
	        return this.AzimuthAngle;
	    }
	}
	
	public static class EveryRod implements Serializable {
		private static final long serialVersionUID = 1L;

	    private String Grade;

	    private float Length;

	    private float OutsideDiameter;

	    private float InsideDiameter;

	    public void setGrade(String Grade){
	        this.Grade = Grade;
	    }
	    public String getGrade(){
	        return this.Grade;
	    }
	    public void setLength(float Length){
	        this.Length = Length;
	    }
	    public float getLength(){
	        return this.Length;
	    }
	    public void setOutsideDiameter(float OutsideDiameter){
	        this.OutsideDiameter = OutsideDiameter;
	    }
	    public float getOutsideDiameter(){
	        return this.OutsideDiameter;
	    }
	    public void setInsideDiameter(float InsideDiameter){
	        this.InsideDiameter = InsideDiameter;
	    }
	    public float getInsideDiameter(){
	        return this.InsideDiameter;
	    }
	}
	
	public static class RodString implements Serializable {
		private static final long serialVersionUID = 1L;
	    private List<EveryRod> EveryRod;

	    public void setEveryRod(List<EveryRod> EveryRod){
	        this.EveryRod = EveryRod;
	    }
	    public List<EveryRod> getEveryRod(){
	        return this.EveryRod;
	    }
	}
	
	public static class EveryTubing implements Serializable {
		private static final long serialVersionUID = 1L;

		private float OutsideDiameter;
		
	    private float InsideDiameter;
	    
	    public void setInsideDiameter(float InsideDiameter){
	        this.InsideDiameter = InsideDiameter;
	    }
	    public float getInsideDiameter(){
	        return this.InsideDiameter;
	    }
		public float getOutsideDiameter() {
			return OutsideDiameter;
		}
		public void setOutsideDiameter(float outsideDiameter) {
			OutsideDiameter = outsideDiameter;
		}
	}
	
	public static class TubingString implements Serializable {
		private static final long serialVersionUID = 1L;
	    private List<EveryTubing> EveryTubing;

	    public void setEveryTubing(List<EveryTubing> EveryTubing){
	        this.EveryTubing = EveryTubing;
	    }
	    public List<EveryTubing> getEveryTubing(){
	        return this.EveryTubing;
	    }
	}

	public static class Pump implements Serializable {
		private static final long serialVersionUID = 1L;
	    private float BarrelLength;//泵筒�?
	    
	    private int BarrelSeries;//螺杆泵井泵级�?
	    
	    private float RotorDiameter;//螺杆泵井转子直径
	    
	    private float QPR;//螺杆泵井公称排量

		public float getBarrelLength() {
			return BarrelLength;
		}
		public void setBarrelLength(float barrelLength) {
			BarrelLength = barrelLength;
		}
		public int getBarrelSeries() {
			return BarrelSeries;
		}
		public void setBarrelSeries(int barrelSeries) {
			BarrelSeries = barrelSeries;
		}
		public float getRotorDiameter() {
			return RotorDiameter;
		}
		public void setRotorDiameter(float rotorDiameter) {
			RotorDiameter = rotorDiameter;
		}
		public float getQPR() {
			return QPR;
		}
		public void setQPR(float qPR) {
			QPR = qPR;
		}
	}
	
	public static class EveryEquipment implements Serializable {
		private static final long serialVersionUID = 1L;
	    private int EquipmentType; //设备类型 1-尾管�?2-滤管�?3-锚定器，4-油气分离�?

	    private int Grade;//级别  1-H�?2-J�?3-K�?4-N�?5-L�?6-C�?7-T�?8-P�?9-Q

	    private float Length;//尾管长度

	    private float OutsideDiameter;//尾管外径

	    private float InsideDiameter;//尾管内径

	    private float Density;//尾管密度
	    
	    private float WeightPerMeter;//每米管重

	    private float GasAnchorEfficiency;//气锚效率  无气锚填0

	    public float getWeightPerMeter() {
			return WeightPerMeter;
		}
		public void setWeightPerMeter(float weightPerMeter) {
			WeightPerMeter = weightPerMeter;
		}
		public void setEquipmentType(int EquipmentType){
	        this.EquipmentType = EquipmentType;
	    }
	    public int getEquipmentType(){
	        return this.EquipmentType;
	    }
	    public void setGrade(int Grade){
	        this.Grade = Grade;
	    }
	    public int getGrade(){
	        return this.Grade;
	    }
	    public void setLength(float Length){
	        this.Length = Length;
	    }
	    public float getLength(){
	        return this.Length;
	    }
	    public void setOutsideDiameter(float OutsideDiameter){
	        this.OutsideDiameter = OutsideDiameter;
	    }
	    public float getOutsideDiameter(){
	        return this.OutsideDiameter;
	    }
	    public void setInsideDiameter(float InsideDiameter){
	        this.InsideDiameter = InsideDiameter;
	    }
	    public float getInsideDiameter(){
	        return this.InsideDiameter;
	    }
	    public void setDensity(float Density){
	        this.Density = Density;
	    }
	    public float getDensity(){
	        return this.Density;
	    }
	    public void setGasAnchorEfficiency(float GasAnchorEfficiency){
	        this.GasAnchorEfficiency = GasAnchorEfficiency;
	    }
	    public float getGasAnchorEfficiency(){
	        return this.GasAnchorEfficiency;
	    }
	}

	
	public static class TailTubingString implements Serializable {
		private static final long serialVersionUID = 1L;
	    private ArrayList<EveryEquipment> EveryEquipment;

	    public void setEveryEquipment(ArrayList<EveryEquipment> EveryEquipment){
	        this.EveryEquipment = EveryEquipment;
	    }
	    public List<EveryEquipment> getEveryEquipment(){
	        return this.EveryEquipment;
	    }
	}

	
	public static class EveryCasing implements Serializable {
		private static final long serialVersionUID = 1L;

	    private float InsideDiameter;//套管内径

	    public void setInsideDiameter(float InsideDiameter){
	        this.InsideDiameter = InsideDiameter;
	    }
	    public float getInsideDiameter(){
	        return this.InsideDiameter;
	    }
	}

	
	public static class CasingString implements Serializable {
		private static final long serialVersionUID = 1L;
	    private ArrayList<EveryCasing> EveryCasing;

	    public void setEveryCasing(ArrayList<EveryCasing> EveryCasing){
	        this.EveryCasing = EveryCasing;
	    }
	    public List<EveryCasing> getEveryCasing(){
	        return this.EveryCasing;
	    }
	}

	
	public static class Production implements Serializable {
		private static final long serialVersionUID = 1L;
	    private float WaterCut;//体积含水�?
	    
	    private float WeightWaterCut;//重量含水�?

	    private float ProductionGasOilRatio;//生产气油�?

	    private float TubingPressure;//油压

	    private float CasingPressure;//套压

	    private float WellHeadTemperature;//井口温度

	    private float ProducingfluidLevel;//动液�?

	    private float PumpSettingDepth;//泵挂

		public void setWaterCut(float WaterCut){
	        this.WaterCut = WaterCut;
	    }
	    public float getWaterCut(){
	        return this.WaterCut;
	    }
	    public void setProductionGasOilRatio(float ProductionGasOilRatio){
	        this.ProductionGasOilRatio = ProductionGasOilRatio;
	    }
	    public float getProductionGasOilRatio(){
	        return this.ProductionGasOilRatio;
	    }
	    public void setTubingPressure(float TubingPressure){
	        this.TubingPressure = TubingPressure;
	    }
	    public float getTubingPressure(){
	        return this.TubingPressure;
	    }
	    public void setCasingPressure(float CasingPressure){
	        this.CasingPressure = CasingPressure;
	    }
	    public float getCasingPressure(){
	        return this.CasingPressure;
	    }
	    public void setWellHeadTemperature(float WellHeadTemperature){
	        this.WellHeadTemperature = WellHeadTemperature;
	    }
	    public float getWellHeadTemperature(){
	        return this.WellHeadTemperature;
	    }
	    public void setProducingfluidLevel(float ProducingfluidLevel){
	        this.ProducingfluidLevel = ProducingfluidLevel;
	    }
	    public float getProducingfluidLevel(){
	        return this.ProducingfluidLevel;
	    }
	    public void setPumpSettingDepth(float PumpSettingDepth){
	        this.PumpSettingDepth = PumpSettingDepth;
	    }
	    public float getPumpSettingDepth(){
	        return this.PumpSettingDepth;
	    }
		public float getWeightWaterCut() {
			return WeightWaterCut;
		}
		public void setWeightWaterCut(float weightWaterCut) {
			WeightWaterCut = weightWaterCut;
		}
	}

	
	public static class ManualIntervention implements Serializable {
		private static final long serialVersionUID = 1L;
		
	    private int Code;

	    private float NetGrossRatio;
	    
	    private float NetGrossValue;

		public int getCode() {
			return Code;
		}

		public void setCode(int code) {
			Code = code;
		}

		public float getNetGrossRatio() {
			return NetGrossRatio;
		}

		public void setNetGrossRatio(float netGrossRatio) {
			NetGrossRatio = netGrossRatio;
		}

		public float getNetGrossValue() {
			return NetGrossValue;
		}

		public void setNetGrossValue(float netGrossValue) {
			NetGrossValue = netGrossValue;
		}
	}
	
	public static class SystemEfficiency implements Serializable {
		private static final long serialVersionUID = 1L;
	    private float MotorInputWatt;

	    public void setMotorInputWatt(float MotorInputWatt){
	        this.MotorInputWatt = MotorInputWatt;
	    }
	    public float getMotorInputWatt(){
	        return this.MotorInputWatt;
	    }
	}

	public String getScene() {
		return Scene;
	}

	public void setScene(String scene) {
		Scene = scene;
	}
}
