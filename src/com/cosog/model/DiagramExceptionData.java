package com.cosog.model;

import java.util.List;

public class DiagramExceptionData {
	public String wellName;
	
	public int reCalculateTimes;
	
	public String lastCalculateTime;
	
	public List<ExceptionInfo> exceptionDataList;
	
	public static class ExceptionInfo{
		public int resultStatus;
		
		public List<Long> diagramIdList;

		public List<Long> getDiagramIdList() {
			return diagramIdList;
		}

		public void setDiagramIdList(List<Long> diagramIdList) {
			this.diagramIdList = diagramIdList;
		}

		public int getResultStatus() {
			return resultStatus;
		}

		public void setResultStatus(int resultStatus) {
			this.resultStatus = resultStatus;
		}
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public int getReCalculateTimes() {
		return reCalculateTimes;
	}

	public void setReCalculateTimes(int reCalculateTimes) {
		this.reCalculateTimes = reCalculateTimes;
	}

	public String getLastCalculateTime() {
		return lastCalculateTime;
	}

	public void setLastCalculateTime(String lastCalculateTime) {
		this.lastCalculateTime = lastCalculateTime;
	}

	
	
	public int getExceptionDataCount(){
		int result=0;
		if(this!=null && this.exceptionDataList!=null && this.exceptionDataList.size()>0){
			for(int i=0;i<this.exceptionDataList.size();i++){
				if(this.exceptionDataList.get(i).getDiagramIdList()!=null){
					result+=this.exceptionDataList.get(i).getDiagramIdList().size();
				}
			}
		}
		return result;
	}

	public List<ExceptionInfo> getExceptionDataList() {
		return exceptionDataList;
	}

	public void setExceptionDataList(List<ExceptionInfo> exceptionDataList) {
		this.exceptionDataList = exceptionDataList;
	}
}
