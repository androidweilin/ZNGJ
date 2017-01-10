package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 统计告警告警趋势的数据
 * Created by weilin on 2016/11/10 19:04
 */
public class AlarmDistributedBean implements Serializable {

    /**
     * datalist : [{"data":[2,19,38,2,28,37,9,26,7,32,41,17],"name":"动环","type":"line"},
     * {"data":[48,28,31,18,16,18,43,37,39,22,8,21],"name":"华为","type":"line"},{"data":[47,17,47,
     * 43,36,46,42,34,50,49,10,4],"name":"UT","type":"line"},{"data":[11,14,7,20,9,2,35,17,35,37,
     * 24,25],"name":"毅格","type":"line"}]
     * endtime : 201512
     * error : 0
     * legendlist : ["动环","华为","UT","毅格"]
     * response : null
     * starttime : 201501
     * success : true
     * xAxislist : ["201501","201502","201503","201504","201505","201506","201507","201508",
     * "201509","201510","201511","201512"]
     */

    private String endtime;
    private int error;
    private Object response;
    private String starttime;
    private boolean success;
    /**
     * data : [2,19,38,2,28,37,9,26,7,32,41,17]
     * name : 动环
     * type : line
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
        private String type;
        private List<Integer> data;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }
}
