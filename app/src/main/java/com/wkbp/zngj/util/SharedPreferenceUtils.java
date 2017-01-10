package com.wkbp.zngj.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.wkbp.zngj.bean.LoginInfoBean;
import com.wkbp.zngj.bean.UserInfoBean;

/**
 * Created by weilin on 2016/11/4 10:09
 */
public class SharedPreferenceUtils {

    private static SharedPreferenceUtils instance;
    private static Editor editor;
    private static SharedPreferences sp;

    public static SharedPreferenceUtils getInstance() {
        if (instance == null) {
            instance = new SharedPreferenceUtils();
        }
        return instance;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences("zngj", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void saveLoninInfo(Context context, LoginInfoBean loginInfo) {
        editor.putString("userName", loginInfo.getUserName());
        editor.putString("passWord", loginInfo.getPassWord());
        editor.putBoolean("isCheck", loginInfo.isCheck());
        editor.commit();
    }

    public static LoginInfoBean getLoginInfo(Context context) {
        LoginInfoBean loginInfo = new LoginInfoBean();
        loginInfo.setUserName(sp.getString("userName", ""));
        loginInfo.setPassWord(sp.getString("passWord", ""));
        loginInfo.setCheck(sp.getBoolean("isCheck", false));
        return loginInfo;
    }

    public static void saveUserInfo(Context context,UserInfoBean.ListBean userInfo){
        editor.putString("name", userInfo.getName());
        editor.putString("deptmentname", userInfo.getDeptmentname());
        editor.putString("phone", userInfo.getPhone());
        editor.commit();
    }

    public static UserInfoBean.ListBean getUserInfo(Context context){
        UserInfoBean.ListBean userInfo = new UserInfoBean.ListBean();
        userInfo.setName(sp.getString("name", ""));
        userInfo.setDeptmentname(sp.getString("deptmentname", ""));
        userInfo.setPhone(sp.getString("phone", ""));
        return userInfo;
    }


}
