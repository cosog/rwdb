package com.cosog.utils;

public class ConfigFile {

    private Ac ac;
	
	public static class AcProbe
	{
	    private String app;

	    private String mem;

	    private String disk;

	    private String host;

	    private String cpu;

	    public void setApp(String app){
	        this.app = app;
	    }
	    public String getApp(){
	        return this.app;
	    }
	    public void setMem(String mem){
	        this.mem = mem;
	    }
	    public String getMem(){
	        return this.mem;
	    }
	    public void setDisk(String disk){
	        this.disk = disk;
	    }
	    public String getDisk(){
	        return this.disk;
	    }
	    public void setHost(String host){
	        this.host = host;
	    }
	    public String getHost(){
	        return this.host;
	    }
	    public void setCpu(String cpu){
	        this.cpu = cpu;
	    }
	    public String getCpu(){
	        return this.cpu;
	    }
	}
	
	public static class Inversion
	{
	    private boolean enable;

	    private String[] url;

	    public void setEnable(boolean enable){
	        this.enable = enable;
	    }
	    public boolean getEnable(){
	        return this.enable;
	    }
	    public void setUrl(String[] url){
	        this.url = url;
	    }
	    public String[] getUrl(){
	        return this.url;
	    }
	}
	
	public static class Ac
	{
		
		private AcProbe probe;

	    private String FESDiagram;

	    private String RPM;

	    private String communication;

	    private String run;

	    private String energy;

	    private String totalCalculation;

	    public void setProbe(AcProbe probe){
	        this.probe = probe;
	    }
	    public AcProbe getProbe(){
	        return this.probe;
	    }
	    public void setFESDiagram(String FESDiagram){
	        this.FESDiagram = FESDiagram;
	    }
	    public String getFESDiagram(){
	        return this.FESDiagram;
	    }
	    public void setCommunication(String communication){
	        this.communication = communication;
	    }
	    public String getCommunication(){
	        return this.communication;
	    }
	    public void setRun(String run){
	        this.run = run;
	    }
	    public String getRun(){
	        return this.run;
	    }
	    public void setEnergy(String energy){
	        this.energy = energy;
	    }
	    public String getEnergy(){
	        return this.energy;
	    }
		public String getRPM() {
			return RPM;
		}
		public void setRPM(String rPM) {
			RPM = rPM;
		}
		public String getTotalCalculation() {
			return totalCalculation;
		}
		public void setTotalCalculation(String totalCalculation) {
			this.totalCalculation = totalCalculation;
		}
	}

	public Ac getAc() {
		return ac;
	}

	public void setAc(Ac ac) {
		this.ac = ac;
	}
}
