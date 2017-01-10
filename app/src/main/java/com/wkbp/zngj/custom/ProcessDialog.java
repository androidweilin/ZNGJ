package com.wkbp.zngj.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wkbp.zngj.R;
import com.wkbp.zngj.adapter.PopWindowAdapter;
import com.wkbp.zngj.bean.LevelOrStatusBean;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weilin on 2016/11/4 10:09
 */
public class ProcessDialog extends Activity {

    public Dialog dialog;
    public TextView btnLeft, btnRight;
    private LinearLayout ll_status;
    private TextView tv_status;
    private ImageView iv_status;
    private EditText et_status;
    private String suggestion = "无意见";//处理意见
    private String alarmstatus;//报警状态的选择

    private String typeUrl = "http://139.129.200" +
            ".127:8080/alarm/alarmDictionary!findAlarmDictionaryByCondition.action?";
    private List<LevelOrStatusBean.ListBean> typeList = new ArrayList<>();
    private List<String> strTypeList = new ArrayList<>();

    private Context context;
    private Handler handler;
    private String workType;

    public ProcessDialog(Context context, Handler handler, String workType) {
        this.context = context;
        this.handler = handler;
        this.workType = workType;
    }

    public void initDialog() {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.CustomDialog);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.upload_dialog, null);

        dialog.setContentView(view);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager manager = ((Activity) context).getWindowManager();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        tv_status = (TextView) view.findViewById(R.id.tv_status);
        iv_status = (ImageView) view.findViewById(R.id.iv_status);
        ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        et_status = (EditText) view.findViewById(R.id.et_status);
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        btnRight = (TextView) view.findViewById(R.id.btnRight);

        ll_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadType();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                //得到选择报警状态的keyData
                if (tv_status.getText().toString().equals("请选择")) {
                    ToastUtil.showMessageDefault(context,"请选择告警状态");
                    return;
                } else {
                    for (int i = 0; i < strTypeList.size(); i++) {
                        if (strTypeList.get(i).equals(tv_status.getText().toString())) {
                            alarmstatus = typeList.get(i).getKeydata();
                        }
                    }
                }
                bundle.putString("alarmstatus", alarmstatus);
                //得到处理意见
                if (!TextUtils.isEmpty(et_status.getText().toString().trim())) {
                    suggestion = et_status.getText().toString().trim();
                }
                bundle.putString("suggestion", suggestion);
                msg.setData(bundle);
                msg.what = 10010;
                handler.sendMessage(msg);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 获取报警状态列表中的数据
     */
    private void loadType() {
        RequestBody requestBody = new FormEncodingBuilder().add("type", "alarmstatus").add
                ("work", workType).build();
        final Request request = new Request.Builder().url(typeUrl).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(10012);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                LevelOrStatusBean typeBean = gson.fromJson(result, new
                        TypeToken<LevelOrStatusBean>() {
                        }.getType());
                boolean success = typeBean.isSuccess();
                if (success) {
                    typeList.clear();
                    strTypeList.clear();
                    //strTypeList.add("全部");
                    typeList.addAll(typeBean.getList());
                    for (int i = 0; i < typeList.size(); i++) {
                        strTypeList.add(typeList.get(i).getValue());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_status.setImageResource(R.drawable.ic_up);
                            levelStatusPop();
                        }
                    });
                } else {
                    handler.sendEmptyMessage(10014);
                }
            }
        });
    }


    private void levelStatusPop() {
        final PopupWindow popupWindow = new PopupWindow(context);
        View view = View.inflate(context, R.layout.popupwindow, null);
        popupWindow.setContentView(view);
        int width = ll_status.getWidth();
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(ll_status);
        popupWindow.update();

        ListView popListView = (ListView) view.findViewById(R.id.pop_list);
        popListView.setAdapter(new PopWindowAdapter(context, strTypeList));

        popListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position,
                                            long id) {
                        popupWindow.dismiss();
                        tv_status.setText(strTypeList.get(position));
                        iv_status.setImageResource(R.drawable.ic_down);
                    }
                }

        );
    }
}
