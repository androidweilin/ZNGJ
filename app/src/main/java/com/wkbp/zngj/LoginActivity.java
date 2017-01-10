package com.wkbp.zngj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wkbp.zngj.base.BaseActivity;
import com.wkbp.zngj.bean.LoginInfoBean;
import com.wkbp.zngj.bean.UserInfoBean;
import com.wkbp.zngj.custom.MaterialEditText;
import com.wkbp.zngj.util.SharedPreferenceUtils;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private MaterialEditText et_userName, et_passWord;
    private Button btn_login;
    private CheckBox checkBox;

    private String url;
    private List<UserInfoBean.ListBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        url = getResources().getString(R.string.login_url);
        initView();
        initErrorPage();
        addIncludeLoading(true);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10001:
                    stopLoadingAnim();
                    if (checkBox.isChecked()) {
                        LoginInfoBean loginInfo = new LoginInfoBean();
                        loginInfo.setUserName(et_userName.getText().toString().trim());
                        loginInfo.setPassWord(et_passWord.getText().toString().trim());
                        loginInfo.setCheck(true);
                        SharedPreferenceUtils.saveLoninInfo(mContext, loginInfo);
                    } else {
                        LoginInfoBean loginInfo = new LoginInfoBean();
                        loginInfo.setUserName(et_userName.getText().toString().trim());
                        loginInfo.setPassWord("");
                        loginInfo.setCheck(false);
                        SharedPreferenceUtils.saveLoninInfo(mContext, loginInfo);
                    }

                    UserInfoBean.ListBean userInfo = list.get(0);
                    SharedPreferenceUtils.saveUserInfo(mContext,userInfo);

                    ToastUtil.showMessageDefault(mContext, "登录成功");
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 10002:
                    stopLoadingAnim();
                    LoginInfoBean loginInfo = new LoginInfoBean();
                    loginInfo.setUserName(et_userName.getText().toString().trim());
                    loginInfo.setPassWord("");
                    loginInfo.setCheck(false);
                    SharedPreferenceUtils.saveLoninInfo(mContext, loginInfo);
                    et_userName.setText(SharedPreferenceUtils.getLoginInfo(mContext).getUserName());
                    et_passWord.setText(SharedPreferenceUtils.getLoginInfo(mContext).getPassWord());
                    ToastUtil.showMessageDefault(mContext, "用户名或密码错误");
                    break;
                case 10003:
                    stopLoadingAnim();
                    ToastUtil.showMessageDefault(mContext, "请求失败");
                    break;
            }
        }
    };

    private void initView() {
        et_userName = (MaterialEditText) findViewById(R.id.et_userName);
        et_passWord = (MaterialEditText) findViewById(R.id.et_passWord);
        et_userName.setText(SharedPreferenceUtils.getLoginInfo(mContext).getUserName());
        et_passWord.setText(SharedPreferenceUtils.getLoginInfo(mContext).getPassWord());
        btn_login = (Button) findViewById(R.id.btn_login);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setChecked(SharedPreferenceUtils.getLoginInfo(mContext).isCheck());
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_userName.getText().toString().trim())) {
                    ToastUtil.showMessageDefault(mContext, "用户名不能为空");
                } else if (TextUtils.isEmpty(et_passWord.getText().toString().trim())) {
                    ToastUtil.showMessageDefault(mContext, "密码不能为空");
                } else {
                    load();
                }
                break;
        }
    }

    @Override
    protected void load() {
        super.load();
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("userid", et_userName.getText()
                .toString().trim()).add("password", et_passWord.getText().toString().trim())
                .build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(10003);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                UserInfoBean userInfoBean = gson.fromJson(result, new TypeToken<UserInfoBean>() {
                }.getType());
                list.clear();
                list.addAll(userInfoBean.getList());
                  if (list.size() > 0) {
                    handler.sendEmptyMessage(10001);
                } else {
                    handler.sendEmptyMessage(10002);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
