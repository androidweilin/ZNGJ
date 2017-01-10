package com.wkbp.zngj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wkbp.zngj.base.BaseFragment;
import com.wkbp.zngj.bean.AlarmDistributedBean;
import com.wkbp.zngj.bean.AlarmLevelBean;
import com.wkbp.zngj.bean.AlarmWeekBean;
import com.wkbp.zngj.custom.AlarmDistributedDialog;
import com.wkbp.zngj.custom.AlarmLevelDialog;
import com.wkbp.zngj.custom.AlarmWeekDialog;
import com.wkbp.zngj.util.DateTimeUtil;
import com.wkbp.zngj.util.StatisticsUtil;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by weilin on 2016/11/4 16:24
 */
public class StatisticsFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tv_week, tv_trend, tv_distributed;
    private String[] urls;
    private String startDate, endDate;
    private List<AlarmWeekBean.DatalistBean> weekList = new ArrayList<>();
    private List<String> level = new ArrayList<>();
    private List<Integer> levelValues = new ArrayList<>();
    private List<AlarmDistributedBean.DatalistBean> distributedList = new ArrayList<>();
    private List<String> yearMonth = new ArrayList<>();
    private String[] valuesY;
    private String[] month;
    private String[] ygValues;
    private String[] dhValues;
    private String[] hwValues;
    private String[] utValues;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10004:
                    stopLoadingAnim();
                    ToastUtil.showMessageDefault(mContext, "请求失败");
                    break;
                case 10005:
                    stopLoadingAnim();
                    ToastUtil.showMessageDefault(mContext, "获取失败");
                    break;
                case 10006:
                    stopLoadingAnim();
                    int a = 0;
                    int b = 0;
                    int c = 0;
                    int d = 0;
                    for (int i = 0; i < 4; i++) {
                        if (weekList.get(i).getName().equals("毅格")) {
                            a = Integer.valueOf(weekList.get(i).getValue());
                        } else if (weekList.get(i).getName().equals("动环")) {
                            b = Integer.valueOf(weekList.get(i).getValue());
                        } else if (weekList.get(i).getName().equals("华为")) {
                            c = Integer.valueOf(weekList.get(i).getValue());
                        } else if (weekList.get(i).getName().equals("UT")) {
                            d = Integer.valueOf(weekList.get(i).getValue());
                        }
                    }
                    // ToastUtil.showMessageDefault(mContext, a + "-" + b + "-" + c + "-" + d);
                    AlarmWeekDialog.initDialog(mContext, a, b, c, d);
                    break;
                case 10007:
                    stopLoadingAnim();
                    int[] values = new int[levelValues.size()];
                    for (int i = 0; i < levelValues.size(); i++) {
                        values[i] = levelValues.get(i);
                    }
                    String[] levelName = new String[level.size()];
                    for (int i = 0; i < level.size(); i++) {
                        levelName[i] = level.get(i);
                    }
                    int n = StatisticsUtil.getAryMax(values);
                    n = StatisticsUtil.getAvgValues(n);
                    valuesY = StatisticsUtil.getValuesY(n);
                    AlarmLevelDialog.initDialog(mContext, valuesY, values,levelName);
                    break;
                case 10008:
                    stopLoadingAnim();
                    for (int i = 0; i < distributedList.size(); i++) {
                        if (distributedList.get(i).getName().equals("毅格")) {
                            ygValues = new String[distributedList.get(i).getData().size()];
                            for (int j = 0; j < distributedList.get(i).getData().size(); j++) {
                                ygValues[j] = String.valueOf(distributedList.get(i).getData().get
                                        (j));
                            }
                        } else if (distributedList.get(i).getName().equals("动环")) {
                            dhValues = new String[distributedList.get(i).getData().size()];
                            for (int j = 0; j < distributedList.get(i).getData().size(); j++) {
                                dhValues[j] = String.valueOf(distributedList.get(i).getData().get
                                        (j));
                            }
                        } else if (distributedList.get(i).getName().equals("华为")) {
                            hwValues = new String[distributedList.get(i).getData().size()];
                            for (int j = 0; j < distributedList.get(i).getData().size(); j++) {
                                hwValues[j] = String.valueOf(distributedList.get(i).getData().get
                                        (j));
                            }
                        } else if (distributedList.get(i).getName().equals("UT")) {
                            utValues = new String[distributedList.get(i).getData().size()];
                            for (int j = 0; j < distributedList.get(i).getData().size(); j++) {
                                utValues[j] = String.valueOf(distributedList.get(i).getData().get
                                        (j));
                            }
                        }
                    }
                    int m = StatisticsUtil.getAryMax(ygValues, dhValues, hwValues, utValues);
                    m = StatisticsUtil.getAvgValues(m);
                    valuesY = StatisticsUtil.getValuesY(m);
                    AlarmDistributedDialog.initDialog(mContext, valuesY, month, ygValues, dhValues,
                            hwValues, utValues);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_statistics, container, false);
        }
        urls = getResources().getStringArray(R.array.statistics_url);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initErrorPage();
        addIncludeLoading();
        initView();
    }

    private void initView() {
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_trend = (TextView) view.findViewById(R.id.tv_trend);
        tv_distributed = (TextView) view.findViewById(R.id.tv_distributed);

        Date end = new Date();
        Date start = DateTimeUtil.addDay(-7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        startDate = sdf.format(start);
        endDate = sdf.format(end);

        tv_week.setOnClickListener(this);
        tv_trend.setOnClickListener(this);
        tv_distributed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_week:
                loadWeek();
                break;
            case R.id.tv_trend:
                loadLevel();
                break;
            case R.id.tv_distributed:
                loadDistributed();
                break;

        }
    }

    /**
     * 加载近7天报警扇形图
     */
    private void loadWeek() {
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("starttime", startDate).add
                ("endtime", endDate).build();
        final Request request = new Request.Builder().url(urls[0]).post(requestBody).build();
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
                AlarmWeekBean weekBean = gson.fromJson(result, new
                        TypeToken<AlarmWeekBean>() {
                        }.getType());
                boolean success = weekBean.isSuccess();
                if (success) {
                    weekList.clear();
                    weekList.addAll(weekBean.getDatalist());
                    handler.sendEmptyMessage(10006);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }

    /**
     * 加载告警等级柱状图
     */
    private void loadLevel() {
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("starttime", startDate).add
                ("endtime", endDate).build();
        final Request request = new Request.Builder().url(urls[1]).post(requestBody).build();
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
                AlarmLevelBean levelBean = gson.fromJson(result, new
                        TypeToken<AlarmLevelBean>() {
                        }.getType());
                boolean success = levelBean.isSuccess();
                if (success) {
                    level.clear();
                    levelValues.clear();
                    level.addAll(levelBean.getXAxislist());
                    levelValues.addAll(levelBean.getDatalist());
                    handler.sendEmptyMessage(10007);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }

    /**
     * 加载告警趋势图
     */
    private void loadDistributed() {
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("starttime", startDate).add
                ("endtime", endDate).build();
        final Request request = new Request.Builder().url(urls[2]).post(requestBody).build();
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
                AlarmDistributedBean distributedBean = gson.fromJson(result, new
                        TypeToken<AlarmDistributedBean>() {
                        }.getType());
                boolean success = distributedBean.isSuccess();
                if (success) {
                    distributedList.clear();
                    yearMonth.clear();
                    distributedList.addAll(distributedBean.getDatalist());
                    yearMonth.addAll(distributedBean.getXAxislist());
                    month = new String[yearMonth.size()];
                    for (int i = 0; i < yearMonth.size(); i++) {
                        month[i] = yearMonth.get(i).substring(4, 6);
                    }
                    handler.sendEmptyMessage(10008);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }
}
