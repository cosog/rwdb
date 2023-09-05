package com.cosog.utils;

import com.cosog.utils.ConfigFile.Ac;

public class ConfigFile {

    private Ac ac;
    
    private ThreadPool threadPool;
    
    private Log log;
    
    private Other other;
    
    public void init(){
		if(this.getAc()==null){
			this.setAc(new Ac());
		}
		if(this.getAc().getProbe()==null){
			this.getAc().setProbe(new AcProbe());
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getProbe().getApp())){
			this.getAc().getProbe().setApp("http://127.0.0.1:18100/api/probe/app");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getProbe().getMem())){
			this.getAc().getProbe().setMem("http://127.0.0.1:18100/api/probe/mem");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getProbe().getDisk())){
			this.getAc().getProbe().setDisk("http://127.0.0.1:18100/api/probe/disk");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getProbe().getHost())){
			this.getAc().getProbe().setHost("http://127.0.0.1:18100/api/probe/host");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getProbe().getCpu())){
			this.getAc().getProbe().setCpu("http://127.0.0.1:18100/api/probe/cpu");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getFESDiagram())){
			this.getAc().setFESDiagram("http://127.0.0.1:18100/api/calc/rpc/fesdiagram/pro");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getRPM())){
			this.getAc().setRPM("http://127.0.0.1:18100/api/calc/pcp/rpm");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getCommunication())){
			this.getAc().setCommunication("http://127.0.0.1:18100/api/calc/plugin/timeeff/comm");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getRun())){
			this.getAc().setRun("http://127.0.0.1:18100/api/calc/plugin/timeeff/run");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getEnergy())){
			this.getAc().setEnergy("http://127.0.0.1:18100/api/calc/plugin/energy");
		}
		if(!StringManagerUtils.isNotNull(this.getAc().getTotalCalculation())){
			this.getAc().setTotalCalculation("http://127.0.0.1:18100/api/analy/total/well");
		}
		
		if(this.getThreadPool()==null){
			this.setThreadPool(new ThreadPool());
		}
		if(this.getThreadPool().getOuterDatabaseSync()==null){
			this.getThreadPool().setOuterDatabaseSync(new ThreadPoolConfig());
			this.getThreadPool().getOuterDatabaseSync().setCorePoolSize(80);
			this.getThreadPool().getOuterDatabaseSync().setMaximumPoolSize(100);
			this.getThreadPool().getOuterDatabaseSync().setKeepAliveTime(5);
			this.getThreadPool().getOuterDatabaseSync().setWattingCount(0);
		}
		
		if(this.log==null){
			this.setLog(new Log());
			this.getLog().setStdout(true);
			this.getLog().setFile(true);
		}
		
		if(this.getOther()==null){
			this.setOther(new Other());
			this.getOther().setDefaultTimeSpan(0);
		}
    }
	
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
	
	public static class ThreadPoolConfig
	{

	    private int corePoolSize;

	    private int maximumPoolSize;

	    private int keepAliveTime;

	    private int wattingCount;

		public int getCorePoolSize() {
			return corePoolSize;
		}

		public void setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
		}

		public int getMaximumPoolSize() {
			return maximumPoolSize;
		}

		public void setMaximumPoolSize(int maximumPoolSize) {
			this.maximumPoolSize = maximumPoolSize;
		}

		public int getKeepAliveTime() {
			return keepAliveTime;
		}

		public void setKeepAliveTime(int keepAliveTime) {
			this.keepAliveTime = keepAliveTime;
		}

		public int getWattingCount() {
			return wattingCount;
		}

		public void setWattingCount(int wattingCount) {
			this.wattingCount = wattingCount;
		}
	}
	
	public static class ThreadPool
	{
		ThreadPoolConfig outerDatabaseSync;
		public ThreadPoolConfig getOuterDatabaseSync() {
			return outerDatabaseSync;
		}
		
		public void setOuterDatabaseSync(ThreadPoolConfig outerDatabaseSync) {
			this.outerDatabaseSync = outerDatabaseSync;
		}
	}
	
	public static class Log{
		public boolean stdout;
		public boolean file;
		
		public boolean getStdout() {
			return stdout;
		}
		public void setStdout(boolean stdout) {
			this.stdout = stdout;
		}
		public boolean getFile() {
			return file;
		}
		public void setFile(boolean file) {
			this.file = file;
		}
	}
	
	public static class Other{
		
		public int defaultTimeSpan;

		public int getDefaultTimeSpan() {
			return defaultTimeSpan;
		}

		public void setDefaultTimeSpan(int defaultTimeSpan) {
			this.defaultTimeSpan = defaultTimeSpan;
		}
	}

	public Ac getAc() {
		return ac;
	}

	public void setAc(Ac ac) {
		this.ac = ac;
	}

	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	public Other getOther() {
		return other;
	}

	public void setOther(Other other) {
		this.other = other;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
