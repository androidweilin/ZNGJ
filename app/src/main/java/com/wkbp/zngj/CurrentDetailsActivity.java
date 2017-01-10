package com.wkbp.zngj;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wkbp.zngj.base.BaseActivity;
import com.wkbp.zngj.base.CrashApplication;
import com.wkbp.zngj.bean.CurrentInfoBean;
import com.wkbp.zngj.custom.ProcessDialog;
import com.wkbp.zngj.util.SharedPreferenceUtils;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;

public class CurrentDetailsActivity extends BaseActivity {
    private TextView tv_equid, tv_alarmtime, tv_alarmlevel, tv_stationname, tv_alarmstatus,
            tv_alarmlocation, tv_alarminfo, tv_alarmvalue, tv_send;
    private CurrentInfoBean.ListBean infoDetails;
    private String type;
    private String handleUrl;
    //以下数据为处理报警信息时所用到的
    private ProcessDialog prosessDialog;
    private String telNumber;//处理意见时的电话号码
    private double longitude;//经度
    private double latitude;//纬度
    private String chooseId;//选中需要处理数据的ID
    private String alarmstatus;//报警状态的选择
    private String suggestion;//报警意见

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10004:
                    ToastUtil.showMessageDefault(mContext, "请求失败");
                    break;
                case 10005:
                    ToastUtil.showMessageDefault(mContext, "获取失败");
                    break;
                case 10010:
                    Bundle bundle = msg.getData();
                    alarmstatus = bundle.getString("alarmstatus");
                    suggestion = bundle.getString("suggestion");
                    senProcess();
                    break;
                case 10011:
                    ToastUtil.showMessageDefault(mContext, "处理意见已提交");
                    prosessDialog.dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_current_details);
        setTitleBar("详细信息");
        infoDetails = (CurrentInfoBean.ListBean) getIntent().getSerializableExtra("infoDetails");
        type = getIntent().getStringExtra("type");
        handleUrl = getIntent().getStringExtra("handleUrl");
        initView();
    }

    private void initView() {
        tv_equid = (TextView) findViewById(R.id.tv_equid);
        tv_alarmtime = (TextView) findViewById(R.id.tv_alarmtime);
        tv_alarmlevel = (TextView) findViewById(R.id.tv_alarmlevel);
        tv_stationname = (TextView) findViewById(R.id.tv_stationname);
        tv_alarmstatus = (TextView) findViewById(R.id.tv_alarmstatus);
        tv_alarmlocation = (TextView) findViewById(R.id.tv_alarmlocation);
        tv_alarminfo = (TextView) findViewById(R.id.tv_alarminfo);
        tv_alarmvalue = (TextView) findViewById(R.id.tv_alarmvalue);

        tv_equid.setText(infoDetails.getEquid());
        tv_alarmtime.setText(infoDetails.getAlarmtime());
        tv_alarmlevel.setText(infoDetails.getLevelname());
        tv_stationname.setText(infoDetails.getStationname());
        tv_alarmstatus.setText(infoDetails.getStatusname());
        tv_alarmlocation.setText(infoDetails.getAlarmlocation());
        tv_alarminfo.setText(infoDetails.getAlarminfo());
        tv_alarmvalue.setText(infoDetails.getAlarmvalue());

        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProcessParam();
            }
        });
    }

    /**
     * 得到需要上传处理信息的参数
     * 弹出处理的Dialog
     */
    private void getProcessParam() {
        //得到本机SIM卡1的手机号码
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context
                .TELEPHONY_SERVICE);
        telNumber = tm.getLine1Number();
        if (telNumber.length() > 3) {
            telNumber = telNumber.substring(3, telNumber.length());
        } else {
            telNumber = SharedPreferenceUtils.getUserInfo(mContext).getPhone();
        }
        //得到经纬度
        longitude = CrashApplication.longitude;
        latitude = CrashApplication.latitude;

        chooseId = String.valueOf(infoDetails.getId());
        prosessDialog = new ProcessDialog(mContext, handler, type);
        prosessDialog.initDialog();
    }

    /**
     * 发送处理意见
     */
    private void senProcess() {
        RequestBody requestBody = new FormEncodingBuilder().add("alarmids", chooseId).add
                ("phone", telNumber).add("x", String.valueOf(longitude)).add("y", String.valueOf
                (latitude)).add("alarmcontent", suggestion).add("alarmstatus", alarmstatus).build();
        final Request request = new Request.Builder().url(handleUrl).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(10004);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                CurrentInfoBean currentInfoBean = gson.fromJson(result, new
                        TypeToken<CurrentInfoBean>() {
                        }.getType());
                boolean success = currentInfoBean.isSuccess();
                if (success) {
                    handler.sendEmptyMessage(10011);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }
}
