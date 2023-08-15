package com.cosog.model;

public class WorkType {
	public Integer resultCode;
	
	public String resultName;
	
	public String optimizationSuggestion;
	
	public WorkType(Integer resultCode, String resultName, String optimizationSuggestion) {
		super();
		this.resultCode = resultCode;
		this.resultName = resultName;
		this.optimizationSuggestion = optimizationSuggestion;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultName() {
		return resultName;
	}
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	public String getOptimizationSuggestion() {
		return optimizationSuggestion;
	}
	public void setOptimizationSuggestion(String optimizationSuggestion) {
		this.optimizationSuggestion = optimizationSuggestion;
	}
}
