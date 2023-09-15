package com.cosog.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cosog.model.DataRequestConfig;
import com.cosog.model.DataResponseConfig;
import com.cosog.model.DiagramExceptionData;
import com.cosog.model.DiagramExceptionData.ExceptionInfo;
import com.cosog.model.RPCCalculateRequestData;
import com.cosog.model.RPCCalculateResponseData;
import com.cosog.utils.CalculateUtils;
import com.cosog.utils.CounterUtils;
import com.cosog.utils.DataProcessingUtils;
import com.cosog.utils.MemoryDataUtils;
import com.cosog.utils.OracleJdbcUtis;
import com.cosog.utils.StringManagerUtils;
import com.google.gson.Gson;

public class SingleWellExceptionDataProcessingThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String wellName;
    private DiagramExceptionData diagramExceptionData;
    public SingleWellExceptionDataProcessingThread(String wellName, DiagramExceptionData diagramExceptionData) {
        super();
        this.wellName = wellName;
        this.diagramExceptionData = diagramExceptionData;
    }
    public void run() {
        if (diagramExceptionData != null && diagramExceptionData.getExceptionDataList() != null && diagramExceptionData.getExceptionDataCount() > 0 && StringManagerUtils.isNotNull(wellName)) {
            List < String > wellList = new ArrayList < > ();
            wellList.add(wellName);
            String sql = "";
            String writeBackSql = "";
            Gson gson = new Gson();
            try {
                sql = DataProcessingUtils.getProductionDataSql(wellList);
                List < List < Object >> prodList = OracleJdbcUtis.queryProductionData(sql);
                RPCCalculateRequestData calculateRequestData = null;
                if (prodList != null && prodList.size() > 0) {
                    for (int i = 0; i < prodList.size(); i++) {
                        List < Object > list = prodList.get(i);
                        calculateRequestData = DataProcessingUtils.getRPCCalculateRequestData(list);
                        break;
                    }
                }

                if (calculateRequestData != null) {
                    Iterator < ExceptionInfo > wellIterator = diagramExceptionData.getExceptionDataList().iterator();
                    List < Long > reCalSucessList = new ArrayList < > ();
                    String diagramSql = "";
                    while (wellIterator.hasNext()) {
                        ExceptionInfo exceptionInfo = wellIterator.next();
                        int resultStatus = exceptionInfo.getResultStatus();
                        List < Long > diagramIdList = exceptionInfo.getDiagramIdList();
                        if (diagramIdList == null || diagramIdList.size() == 0) {
                            wellIterator.remove();
                        } else {
                            diagramSql = DataProcessingUtils.getDiagramQuerySql(wellName, diagramIdList);
                            List < List < Object >> diagramList = OracleJdbcUtis.queryFESDiagramData(sql);
                            if (diagramList != null && diagramList.size() > 0) {
                                for (int i = 0; i < diagramList.size(); i++) {
                                    List < Object > list = diagramList.get(i);
                                    try {
                                        long diagramId = StringManagerUtils.stringToLong(list.get(0) + "");
                                        String fesdiagramAcqtimeStr = list.get(2) + "";
                                        float stroke = StringManagerUtils.stringToFloat(list.get(3) + "");;
                                        float spm = StringManagerUtils.stringToFloat(list.get(4) + "");;
                                        int pointCount = StringManagerUtils.stringToInteger(list.get(5) + "");;
                                        String sStr = list.get(6) + "";
                                        String fStr = list.get(7) + "";
                                        String iStr = list.get(8) + "";
                                        String KWattStr = list.get(9) + "";

                                        calculateRequestData.setFESDiagram(new RPCCalculateRequestData.FESDiagram());
                                        calculateRequestData.getFESDiagram().setAcqTime(fesdiagramAcqtimeStr);
                                        calculateRequestData.getFESDiagram().setStroke(stroke);
                                        calculateRequestData.getFESDiagram().setSPM(spm);
                                        calculateRequestData.getFESDiagram().setS(new ArrayList < Float > ());
                                        calculateRequestData.getFESDiagram().setF(new ArrayList < Float > ());
                                        calculateRequestData.getFESDiagram().setWatt(new ArrayList < Float > ());
                                        calculateRequestData.getFESDiagram().setI(new ArrayList < Float > ());

                                        String[] sArr = sStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
                                        String[] fArr = fStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
                                        String[] wattArr = KWattStr.replaceAll(";", ",").replaceAll(",", ",").split(",");
                                        String[] iArr = iStr.replaceAll(";", ",").replaceAll(",", ",").split(",");

                                        for (int j = 0; j < sArr.length; j++) {
                                            calculateRequestData.getFESDiagram().getS().add(StringManagerUtils.stringToFloat(sArr[j]));
                                        }
                                        for (int j = 0; j < fArr.length; j++) {
                                            calculateRequestData.getFESDiagram().getF().add(StringManagerUtils.stringToFloat(fArr[j]));
                                        }
                                        for (int j = 0; j < wattArr.length; j++) {
                                            calculateRequestData.getFESDiagram().getWatt().add(StringManagerUtils.stringToFloat(wattArr[j]));
                                        }
                                        for (int j = 0; j < iArr.length; j++) {
                                            calculateRequestData.getFESDiagram().getI().add(StringManagerUtils.stringToFloat(iArr[j]));
                                        }

                                        //功图计算
                                        RPCCalculateResponseData calculateResponseData = CalculateUtils.fesDiagramCalculate(gson.toJson(calculateRequestData));

                                        //结果回写
                                        if (calculateResponseData != null) {
                                            if (calculateResponseData.getCalculationStatus().getResultStatus() == 1) {
                                                writeBackSql = DataProcessingUtils.getWriteBackSql(calculateResponseData);
                                                if (StringManagerUtils.isNotNull(writeBackSql)) {
                                                    int iNum = OracleJdbcUtis.writeBackDiagramCalculateData(writeBackSql);
                                                }
                                                reCalSucessList.add(diagramId);
                                            }
                                            CounterUtils.exceptionCalCountIncr();
                                        } else {
                                            StringManagerUtils.printLog("Calculation failed, no response data" + ",wellName:" + calculateRequestData.getWellName() + ",acqTime:" + calculateRequestData.getFESDiagram().getAcqTime());
                                            StringManagerUtils.printLogFile(logger, "Calculation failed, no response data" + ",wellName:" + calculateRequestData.getWellName() + ",acqTime:" + calculateRequestData.getFESDiagram().getAcqTime(), "info");
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        StringManagerUtils.printLogFile(logger, "error", e1, "error");
                                    }
                                }
                            }
                        }
                    }

                    //将计算成功的记录移除
                    if (reCalSucessList.size() > 0) {
                        diagramExceptionData.setReCalculateTimes(0);
                        wellIterator = diagramExceptionData.getExceptionDataList().iterator();
                        while (wellIterator.hasNext()) {
                            ExceptionInfo exceptionInfo = wellIterator.next();
                            int resultStatus = exceptionInfo.getResultStatus();
                            List < Long > list = exceptionInfo.getDiagramIdList();
                            if (list == null || list.size() == 0) {
                                wellIterator.remove();
                            } else {
                                Iterator < Long > it = list.iterator();
                                while (it.hasNext()) {
                                    if (StringManagerUtils.existOrNot_long(reCalSucessList, it.next())) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    } else {
                        diagramExceptionData.setReCalculateTimes(diagramExceptionData.getReCalculateTimes() + 1);
                    }
                    diagramExceptionData.setLastCalculateTime(StringManagerUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                StringManagerUtils.printLogFile(logger, "error", e1, "error");
            }
        }
    }
    public String getWellName() {
        return wellName;
    }
    public void setWellName(String wellName) {
        this.wellName = wellName;
    }
    public DiagramExceptionData getDiagramExceptionData() {
        return diagramExceptionData;
    }
    public void setDiagramExceptionData(DiagramExceptionData diagramExceptionData) {
        this.diagramExceptionData = diagramExceptionData;
    }
}
