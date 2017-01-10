package com.wkbp.zngj.bean;

import java.io.Serializable;

/**
 * Created by weilin on 2016/11/4 13:53
 */
public class LoginInfoBean implements Serializable {
    private String userName;
    private String passWord;
    private boolean isCheck;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
