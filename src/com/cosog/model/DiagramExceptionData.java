package com.cosog.model;

import java.util.ArrayList;
import java.util.List;

import com.cosog.model.DiagramExceptionData.ExceptionInfo;

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
	
	public boolean containsResultStatus(int resultStatus){
		boolean bool=false;
		if(this!=null && this.exceptionDataList!=null && this.exceptionDataList.size()>0){
			for(int i=0;i<this.exceptionDataList.size();i++){
				if(this.exceptionDataList.get(i).getResultStatus()==resultStatus){
					if(this.exceptionDataList.get(i).getDiagramIdList()==null){
						this.exceptionDataList.get(i).setDiagramIdList(new ArrayList<Long>());
					}
					bool=true;
					break;
				}
			}
		}
		return bool;
	}
	
	public void addDiagramId(int resultStatus,long diagramId){
		if(this!=null){
			if(this.exceptionDataList==null){
				this.setExceptionDataList(new ArrayList<ExceptionInfo>());
			}
			if(containsResultStatus(resultStatus)){
				for(int i=0;i<this.exceptionDataList.size();i++){
					if(this.exceptionDataList.get(i).getResultStatus()==resultStatus){
						if(this.exceptionDataList.get(i).getDiagramIdList()==null){
							this.exceptionDataList.get(i).setDiagramIdList(new ArrayList<Long>());
						}
						this.exceptionDataList.get(i).getDiagramIdList().add(diagramId);
						break;
					}
				}
			}else{
				ExceptionInfo exceptionInfo=new ExceptionInfo();
				exceptionInfo.setResultStatus(resultStatus);
				exceptionInfo.setDiagramIdList(new ArrayList<Long>());
				exceptionInfo.getDiagramIdList().add(diagramId);
				this.exceptionDataList.add(exceptionInfo);
			}
		}
	}
	

	public List<ExceptionInfo> getExceptionDataList() {
		return exceptionDataList;
	}

	public void setExceptionDataList(List<ExceptionInfo> exceptionDataList) {
		this.exceptionDataList = exceptionDataList;
	}
}
