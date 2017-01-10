package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weilin on 2016/11/7 19:01
 */
public class LevelOrStatusBean implements Serializable {

    /**
     * id : null
     * keydata : null
     * type : alarmstatus
     * value : null
     * work : DH
     */

    private AlarmDictionaryBean alarmDictionary;
    /**
     * alarmDictionary : {"id":null,"keydata":null,"type":"alarmstatus","value":null,"work":"DH"}
     * error : 0
     * fromIndex : 0
     * id : null
     * keydata : null
     * list : [{"id":26,"keydata":"1","type":"alarmstatus","value":"新增","work":"DH"},{"id":25,
     * "keydata":"2","type":"alarmstatus","value":"新增","work":"DH"},{"id":24,"keydata":"3",
     * "type":"alarmstatus","value":"已处理","work":"DH"}]
     * model : {"id":null,"keydata":null,"type":"alarmstatus","value":null,"work":"DH"}
     * page : 1
     * response : null
     * rows : 10
     * success : true
     * total : 3
     * type : null
     * value : null
     * work : null
     */

    private int error;
    private int fromIndex;
    private Object id;
    private Object keydata;
    /**
     * id : null
     * keydata : null
     * type : alarmstatus
     * value : null
     * work : DH
     */

    private ModelBean model;
    private int page;
    private Object response;
    private int rows;
    private boolean success;
    private int total;
    private Object type;
    private Object value;
    private Object work;
    /**
     * id : 26
     * keydata : 1
     * type : alarmstatus
     * value : 新增
     * work : DH
     */

    private List<ListBean> list;

    public AlarmDictionaryBean getAlarmDictionary() {
        return alarmDictionary;
    }

    public void setAlarmDictionary(AlarmDictionaryBean alarmDictionary) {
        this.alarmDictionary = alarmDictionary;
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

    public Object getKeydata() {
        return keydata;
    }

    public void setKeydata(Object keydata) {
        this.keydata = keydata;
    }

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
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

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getWork() {
        return work;
    }

    public void setWork(Object work) {
        this.work = work;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class AlarmDictionaryBean {
        private Object id;
        private Object keydata;
        private String type;
        private Object value;
        private String work;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getKeydata() {
            return keydata;
        }

        public void setKeydata(Object keydata) {
            this.keydata = keydata;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }
    }

    public static class ModelBean {
        private Object id;
        private Object keydata;
        private String type;
        private Object value;
        private String work;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getKeydata() {
            return keydata;
        }

        public void setKeydata(Object keydata) {
            this.keydata = keydata;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }
    }

    public static class ListBean implements Serializable{
        private int id;
        private String keydata;
        private String type;
        private String value;
        private String work;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeydata() {
            return keydata;
        }

        public void setKeydata(String keydata) {
            this.keydata = keydata;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }
    }
}
