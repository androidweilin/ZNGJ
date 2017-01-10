package com.wkbp.zngj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weilin on 2016/11/4 13:53
 */
public class UserInfoBean implements Serializable {

    /**
     * error : 0
     * fromIndex : 0
     * list : [{"deptmentid":"3","deptmentname":"通信1班","id":9,"level":"普通员工","name":"system",
     * "password":"","phone":"13522335566","role":"告警处理员","userid":"system"}]
     * model : {"password":"system","userid":"system"}
     * page : 1
     * rows : 10
     * success : true
     * sysUser : {"password":"system","userid":"system"}
     * total : 0
     */

    private int error;
    private int fromIndex;
    /**
     * password : system
     * userid : system
     */

    private ModelBean model;
    private int page;
    private int rows;
    private boolean success;
    /**
     * password : system
     * userid : system
     */

    private SysUserBean sysUser;
    private int total;
    /**
     * deptmentid : 3
     * deptmentname : 通信1班
     * id : 9
     * level : 普通员工
     * name : system
     * password :
     * phone : 13522335566
     * role : 告警处理员
     * userid : system
     */

    private List<ListBean> list;

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

    public SysUserBean getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserBean sysUser) {
        this.sysUser = sysUser;
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

    public static class ModelBean implements Serializable{
        private String password;
        private String userid;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }

    public static class SysUserBean implements Serializable{
        private String password;
        private String userid;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }

    public static class ListBean implements Serializable{
        private String deptmentid;
        private String deptmentname;
        private int id;
        private String level;
        private String name;
        private String password;
        private String phone;
        private String role;
        private String userid;

        public String getDeptmentid() {
            return deptmentid;
        }

        public void setDeptmentid(String deptmentid) {
            this.deptmentid = deptmentid;
        }

        public String getDeptmentname() {
            return deptmentname;
        }

        public void setDeptmentname(String deptmentname) {
            this.deptmentname = deptmentname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
