package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 统计告警的近7天告警
 * Created by weilin on 2016/11/10 16:43
 */
public class AlarmWeekBean implements Serializable {

    /**
     * datalist : [{"name":"毅格","value":"13"},{"name":"华为","value":"13"},{"name":"动环",
     * "value":"32"},{"name":"UT","value":"3"}]
     * endtime : 20161110
     * error : 0
     * legendlist : ["毅格","华为","动环","UT"]
     * response : null
     * starttime : 20161103
     * success : true
     * xAxislist : ["毅格","华为","动环","UT"]
     */

    private String endtime;
    private int error;
    private Object response;
    private String starttime;
    private boolean success;
    /**
     * name : 毅格
     * value : 13
     */

    private List<DatalistBean> datalist;
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

    public List<DatalistBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<DatalistBean> datalist) {
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

    public static class DatalistBean implements Serializable{
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
