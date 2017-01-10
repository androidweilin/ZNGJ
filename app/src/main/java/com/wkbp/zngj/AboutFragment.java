package com.wkbp.zngj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wkbp.zngj.base.BaseFragment;
import com.wkbp.zngj.base.CrashApplication;
import com.wkbp.zngj.bean.LoginInfoBean;
import com.wkbp.zngj.custom.CustomDialog;
import com.wkbp.zngj.util.SharedPreferenceUtils;

/**
 * Created by weilin on 2016/11/4 16:25
 */
public class AboutFragment extends BaseFragment{
    private View view;
    private TextView user_name,deptment_name;
    private Button btn_exit;
    private String name,deptmentname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_about, container, false);
        }
        name = SharedPreferenceUtils.getUserInfo(mContext).getName();
        deptmentname = SharedPreferenceUtils.getUserInfo(mContext).getDeptmentname();
        initView();
        return view;
    }

    private void initView() {
        user_name = (TextView) view.findViewById(R.id.user_name);
        deptment_name = (TextView) view.findViewById(R.id.deptment_name);
        user_name.setText(getResources().getString(R.string.user_name,name));
        deptment_name.setText(getResources().getString(R.string.deptment_name,deptmentname));
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               initDialog();
            }
        });
    }

    private void initDialog(){
        CustomDialog.initDialog(mContext);
        CustomDialog.tvTitle.setText("您确定要注销当前用户吗?");
        CustomDialog.btnLeft.setText("确定");
        CustomDialog.btnRight.setText("取消");
        CustomDialog.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInfoBean loginInfo = new LoginInfoBean();
                loginInfo.setUserName("");
                loginInfo.setPassWord("");
                loginInfo.setCheck(false);
                SharedPreferenceUtils.saveLoninInfo(mContext, loginInfo);
                for(Activity activity : CrashApplication.allActivity){
                    activity.finish();
                }
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
            }
        });
        CustomDialog.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.dialog.dismiss();
            }
        });
    }

}
