package com.cosog.model;

import java.util.List;

public class CPUProbeResponseData {

	private int Num;

    private List<Float> Percent;

    public void setNum(int Num){
        this.Num = Num;
    }
    public int getNum(){
        return this.Num;
    }
    public void setPercent(List<Float> Percent){
        this.Percent = Percent;
    }
    public List<Float> getPercent(){
        return this.Percent;
    }
}
