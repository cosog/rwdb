package com.cosog.model;

import java.util.List;

public class DataReadTimeInfo {
	private List<DataReadTime> wellList;
	public static class DataReadTime{
		public String wellName;
		public String readTime;
		public String getWellName() {
			return wellName;
		}
		public void setWellName(String wellName) {
			this.wellName = wellName;
		}
		public String getReadTime() {
			return readTime;
		}
		public void setReadTime(String readTime) {
			this.readTime = readTime;
		}
	}
	public List<DataReadTime> getWellList() {
		return wellList;
	}
	public void setWellList(List<DataReadTime> wellList) {
		this.wellList = wellList;
	}
}
