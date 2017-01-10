package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 统计告警等级分布柱状图的数据
 * Created by weilin on 2016/11/10 17:18
 */
public class AlarmLevelBean implements Serializable{

    /**
     * datalist : [3,5,97]
     * endtime : 20160930
     * error : 0
     * legendlist : ["一般","重要","紧急"]
     * response : null
     * starttime : 20160928
     * success : true
     * xAxislist : ["一般","重要","紧急"]
     */

    private String endtime;
    private int error;
    private Object response;
    private String starttime;
    private boolean success;
    private List<Integer> datalist;
    private List<String> legendlist;
    private List<String> xAxislist;

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Integer> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<Integer> datalist) {
        this.datalist = datalist;
    }

    public List<String> getLegendlist() {
        return legendlist;
    }

    public void setLegendlist(List<String> legendlist) {
        this.legendlist = legendlist;
    }

    public List<String> getXAxislist() {
        return xAxislist;
    }

    public void setXAxislist(List<String> xAxislist) {
        this.xAxislist = xAxislist;
    }
}
