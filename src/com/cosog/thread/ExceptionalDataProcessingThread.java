package com.cosog.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cosog.utils.DataModelMap;

public class ExceptionalDataProcessingThread  extends Thread{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public void run(){
		Map<String, Object> map = DataModelMap.getMapObject();
		Map<String,Map<Integer,List<Long>>> diagramCalculateFailureMap=(Map<String,Map<Integer,List<Long>>>) map.get("diagramCalculateFailureMap");
		
		ThreadPool executor = new ThreadPool("diagramExceptionDataProcessing",
				10, 
				10, 
				5, 
				TimeUnit.SECONDS, 
				0);
		do{
			if(map.containsKey("diagramCalculateFailureMap")){
				Iterator<Map.Entry<String,Map<Integer,List<Long>>>> iterator = diagramCalculateFailureMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String,Map<Integer,List<Long>>> entry = iterator.next();
					String wellName=entry.getKey();
					Map<Integer,List<Long>> calculateFailureMap=entry.getValue();
					int recordCount=0;
					if(calculateFailureMap!=null){
						Iterator<Map.Entry<Integer,List<Long>>> wellIterator=calculateFailureMap.entrySet().iterator();
						while(wellIterator.hasNext()){
							Map.Entry<Integer,List<Long>> wellEntry = wellIterator.next();
							List<Long> list=wellEntry.getValue();
							recordCount+=list.size();
						}
					}
					if(recordCount==0){
						iterator.remove();
					}else{
						executor.execute(new SingleWellExceptionDataProcessingThread(wellName,calculateFailureMap));
					}
				}
				while (!executor.isCompletedByTaskCount()) {
					try {
						Thread.sleep(1000*1);
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("error", e);
					}
			    }
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error("error", e);
				}
			}
		}while(true);
	}
}
