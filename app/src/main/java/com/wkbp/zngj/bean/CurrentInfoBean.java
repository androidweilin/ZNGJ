package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weilin on 2016/11/5 10:11
 */
public class CurrentInfoBean implements Serializable{

    /**
     * alarmlevel : null
     * alarmstatus : null
     * alarmtime : null
     * endtime : null
     * equid : null
     * error : 0
     * fromIndex : 0
     * id : null
     * jsonstr : null
     * list : [{"alarminfo":"6","alarmlevel":2,"alarmlocation":"机房温度告警","alarmstatus":3,
     * "alarmtime":"2016-10-18 14:47:33","alarmvalue":"6","equid":"6","id":43,
     * "stationname":"变电站3"},{"alarminfo":"3","alarmlevel":3,"alarmlocation":"电池组",
     * "alarmstatus":3,"alarmtime":"2016-10-18 14:47:33","alarmvalue":"3","equid":"1","id":42,
     * "stationname":"变电站2"},{"alarminfo":"2","alarmlevel":2,"alarmlocation":"机房门",
     * "alarmstatus":2,"alarmtime":"2016-10-18 14:47:33","alarmvalue":"2","equid":"1","id":41,
     * "stationname":"变电站1"},{"alarminfo":"1","alarmlevel":1,"alarmlocation":"1号电池",
     * "alarmstatus":1,"alarmtime":"2016-10-18 14:47:33","alarmvalue":"1","equid":"1","id":40,
     * "stationname":"变电站"},{"alarminfo":"6","alarmlevel":2,"alarmlocation":"6","alarmstatus":3,
     * "alarmtime":"2016-10-14 09:24:41","alarmvalue":"6","equid":"6","id":35,
     * "stationname":"变电站3"},{"alarminfo":"3","alarmlevel":3,"alarmlocation":"3","alarmstatus":3,
     * "alarmtime":"2016-10-14 09:24:41","alarmvalue":"3","equid":"1","id":34,
     * "stationname":"变电站2"},{"alarminfo":"2","alarmlevel":2,"alarmlocation":"2","alarmstatus":3,
     * "alarmtime":"2016-10-14 09:24:41","alarmvalue":"2","equid":"1","id":33,
     * "stationname":"变电站1"},{"alarminfo":"1","alarmlevel":1,"alarmlocation":"1","alarmstatus":3,
     * "alarmtime":"2016-10-14 09:24:41","alarmvalue":"1","equid":"1","id":32,
     * "stationname":"变电站"},{"alarminfo":"6","alarmlevel":2,"alarmlocation":"6","alarmstatus":1,
     * "alarmtime":"2016-10-08 14:30:27","alarmvalue":"6","equid":"6","id":31,
     * "stationname":"变电站3"},{"alarminfo":"3","alarmlevel":3,"alarmlocation":"3","alarmstatus":3,
     * "alarmtime":"2016-10-08 14:30:27","alarmvalue":"3","equid":"1","id":30,"stationname":"变电站2"}]
     * page : 1
     * response : null
     * rows : 10
     * starttime : null
     * stationname : null
     * success : true
     * total : 0
     */

    private Object alarmlevel;
    private Object alarmstatus;
    private Object alarmtime;
    private Object endtime;
    private Object equid;
    private int error;
    private int fromIndex;
    private Object id;
    private Object jsonstr;
    private int page;
    private Object response;
    private int rows;
    private Object starttime;
    private Object stationname;
    private boolean success;
    private int total;
    /**
     * alarminfo : 6
     * alarmlevel : 2
     * alarmlocation : 机房温度告警
     * alarmstatus : 3
     * alarmtime : 2016-10-18 14:47:33
     * alarmvalue : 6
     * equid : 6
     * id : 43
     * stationname : 变电站3
     */

    private List<ListBean> list;

    public Object getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(Object alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public Object getAlarmstatus() {
        return alarmstatus;
    }

    public void setAlarmstatus(Object alarmstatus) {
        this.alarmstatus = alarmstatus;
    }

    public Object getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(Object alarmtime) {
        this.alarmtime = alarmtime;
    }

    public Object getEndtime() {
        return endtime;
    }

    public void setEndtime(Object endtime) {
        this.endtime = endtime;
    }

    public Object getEquid() {
        return equid;
    }

    public void setEquid(Object equid) {
        this.equid = equid;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getJsonstr() {
        return jsonstr;
    }

    public void setJsonstr(Object jsonstr) {
        this.jsonstr = jsonstr;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Object getStarttime() {
        return starttime;
    }

    public void setStarttime(Object starttime) {
        this.starttime = starttime;
    }

    public Object getStationname() {
        return stationname;
    }

    public void setStationname(Object stationname) {
        this.stationname = stationname;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private boolean isCheck;   //chenckBox是否选中
        private int id;   //告警编号
        private String equid;   //设备编号
        private String alarmtime;   //告警时间
        private String statusname;    //告警状态
        private String levelname;    //告警等级
        private String alarmlocation;   //告警点
        private String alarmvalue;   //告警值
        private String alarminfo;  //告警信息
        private int alarmlevel;   //告警等级编号
        private int alarmstatus;    //告警状态编号
        private String stationname;   //告警变电站

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getAlarminfo() {
            return alarminfo;
        }

        public void setAlarminfo(String alarminfo) {
            this.alarminfo = alarminfo;
        }

        public int getAlarmlevel() {
            return alarmlevel;
        }

        public void setAlarmlevel(int alarmlevel) {
            this.alarmlevel = alarmlevel;
        }

        public String getAlarmlocation() {
            return alarmlocation;
        }

        public void setAlarmlocation(String alarmlocation) {
            this.alarmlocation = alarmlocation;
        }

        public int getAlarmstatus() {
            return alarmstatus;
        }

        public void setAlarmstatus(int alarmstatus) {
            this.alarmstatus = alarmstatus;
        }

        public String getAlarmtime() {
            return alarmtime;
        }

        public void setAlarmtime(String alarmtime) {
            this.alarmtime = alarmtime;
        }

        public String getAlarmvalue() {
            return alarmvalue;
        }

        public void setAlarmvalue(String alarmvalue) {
            this.alarmvalue = alarmvalue;
        }

        public String getEquid() {
            return equid;
        }

        public void setEquid(String equid) {
            this.equid = equid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStationname() {
            return stationname;
        }

        public void setStationname(String stationname) {
            this.stationname = stationname;
        }

        public String getLevelname() {
            return levelname;
        }

        public void setLevelname(String levelname) {
            this.levelname = levelname;
        }

        public String getStatusname() {
            return statusname;
        }

        public void setStatusname(String statusname) {
            this.statusname = statusname;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "isCheck=" + isCheck +
                    ", id=" + id +
                    ", equid='" + equid + '\'' +
                    ", alarmtime='" + alarmtime + '\'' +
                    ", statusname='" + statusname + '\'' +
                    ", levelname='" + levelname + '\'' +
                    ", alarmlocation='" + alarmlocation + '\'' +
                    ", alarmvalue='" + alarmvalue + '\'' +
                    ", alarminfo='" + alarminfo + '\'' +
                    ", alarmlevel=" + alarmlevel +
                    ", alarmstatus=" + alarmstatus +
                    ", stationname='" + stationname + '\'' +
                    '}';
        }
    }
}
