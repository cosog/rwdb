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

import com.cosog.model.AppRunStatusProbeResonanceData;
import com.cosog.model.DiagramExceptionData;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.DataModelMap;
import com.cosog.utils.StringManagerUtils;

public class ExceptionalDataProcessingThread  extends Thread{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public void run(){
		Map<String, Object> map = DataModelMap.getMapObject();
		ThreadPool executor = new ThreadPool("diagramExceptionDataProcessing",
				10, 
				10, 
				5, 
				TimeUnit.SECONDS, 
				0);
		do{
			AppRunStatusProbeResonanceData acStatusProbeResonanceData=CalculateUtils.appProbe("");
			String currentTime=StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			if(acStatusProbeResonanceData!=null && map.containsKey("diagramCalculateFailureMap")){
				Map<String,DiagramExceptionData> diagramCalculateFailureMap=(Map<String, DiagramExceptionData>) map.get("diagramCalculateFailureMap");
				Iterator<Map.Entry<String,DiagramExceptionData>> iterator = diagramCalculateFailureMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String,DiagramExceptionData> entry = iterator.next();
					String wellName=entry.getKey();
					DiagramExceptionData diagramExceptionData=entry.getValue();
					int recordCount=diagramExceptionData.getExceptionDataCount();
					if(recordCount==0){
						iterator.remove();
					}else{
						if(diagramExceptionData.getReCalculateTimes()<=5 
								|| (diagramExceptionData.getReCalculateTimes()>5 && diagramExceptionData.getReCalculateTimes()<=10 && StringManagerUtils.getTimeDifference(diagramExceptionData.getLastCalculateTime(), currentTime, "yyyy-MM-dd HH:mm:ss")>=5*60*1000  )
								|| (diagramExceptionData.getReCalculateTimes()>10 && StringManagerUtils.getTimeDifference(diagramExceptionData.getLastCalculateTime(), currentTime, "yyyy-MM-dd HH:mm:ss")>=10*60*1000  )
								){
							executor.execute(new SingleWellExceptionDataProcessingThread(wellName,diagramExceptionData));
						}
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
			}

			try {
				Thread.sleep(60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("error", e);
			}
		}while(true);
	}
}
