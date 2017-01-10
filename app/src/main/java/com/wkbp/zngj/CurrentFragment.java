package com.wkbp.zngj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wkbp.zngj.adapter.CurrentItemAdapter;
import com.wkbp.zngj.base.BaseFragment;
import com.wkbp.zngj.base.CrashApplication;
import com.wkbp.zngj.bean.CurrentInfoBean;
import com.wkbp.zngj.custom.ProcessDialog;
import com.wkbp.zngj.util.SharedPreferenceUtils;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weilin on 2016/11/4 16:18
 */
public class CurrentFragment extends BaseFragment implements View.OnClickListener, AdapterView
        .OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    //数据请求url
    private String[] urls;
    private String url;
    private String rows = "10";
    //查询报警状态和报警等级的类型标签
    private String[] types;
    private String type;
    //数据处理url
    private String[] handleUrls ;
    private String handleUrl;
    //表头四个按钮
    private String[] titleTvs;

    private View view;
    private int[] ids = {R.id.tv001, R.id.tv002, R.id.tv003, R.id.tv004};
    private TextView tv005, tv006, tv007;
    private PullToRefreshListView listView;
    private CurrentItemAdapter itemAdapter;
    private List<CurrentInfoBean.ListBean> list = new ArrayList<>();

    //以下数据为处理报警信息时所用到的
    private ProcessDialog prosessDialog;
    private String telNumber;//处理意见时的电话号码
    private double longitude;//经度
    private double latitude;//纬度
    private String chooseIds;//选中需要处理数据的ID
    private String alarmstatus;//报警状态的选择
    private String suggestion;//报警意见

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10003:
                    hideErrorPageState();
                    stopLoadingAnim();
                    listView.onRefreshComplete();
                    itemAdapter.notifyDataSetChanged();
                    break;
                case 10004:
                    stopLoadingAnim();
                    //showErrorPageState(1);
                    ToastUtil.showMessageDefault(mContext, "网络请求失败");
                    listView.onRefreshComplete();
                    break;
                case 10005:
                    stopLoadingAnim();
                    //showErrorPageState(1);
                    ToastUtil.showMessageDefault(mContext, "获取失败，请稍后再试");
                    listView.onRefreshComplete();
                    break;
                case 10010:
                    Bundle bundle = msg.getData();
                    alarmstatus = bundle.getString("alarmstatus");
                    suggestion = bundle.getString("suggestion");
                    senProcess();
                    break;
                case 10011:
                    stopLoadingAnim();
                    ToastUtil.showMessageDefault(mContext, "处理意见已提交");
                    prosessDialog.dialog.dismiss();
                    break;
                case 10012:
                    stopLoadingAnim();
                    ToastUtil.showMessageDefault(mContext, "网络请求失败");
                    break;
                case 10013:
                    ToastUtil.showMessageDefault(mContext, "提交失败，请稍后再试");
                    stopLoadingAnim();
                    break;
                case 10014:
                    ToastUtil.showMessageDefault(mContext, "获取失败，请稍后再试");
                    stopLoadingAnim();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_current, container, false);
        }
        urls = getResources().getStringArray(R.array.current_request_urls);
        handleUrls = getResources().getStringArray(R.array.send_urls);
        titleTvs = getResources().getStringArray(R.array.current_title);
        types = getResources().getStringArray(R.array.level_status_type);
        url = urls[0];
        handleUrl = handleUrls[0];
        type = types[0];
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListener();
        initErrorPage();
        addIncludeLoading();
        load();
    }

    private void initView() {
        for (int i = 0; i < ids.length; i++) {
            ((TextView) view.findViewById(ids[i])).setText(titleTvs[i]);
            view.findViewById(ids[i]).setOnClickListener(this);
        }

        tv005 = (TextView) view.findViewById(R.id.tv005);
        tv006 = (TextView) view.findViewById(R.id.tv006);
        tv007 = (TextView) view.findViewById(R.id.tv007);
        tv005.setOnClickListener(this);
        tv006.setOnClickListener(this);
        tv007.setOnClickListener(this);

        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        itemAdapter = new CurrentItemAdapter(mContext, list);
        listView.setAdapter(itemAdapter);
    }

    @Override
    protected void load() {
        super.load();
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("rows", rows).build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
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
                    list.clear();
                    list.addAll(currentInfoBean.getList());
                    handler.sendEmptyMessage(10003);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv001:
            case R.id.tv002:
            case R.id.tv003:
            case R.id.tv004:
                for (int i = 0; i < ids.length; i++) {
                    if (v.getId() == ids[i]) {
                        view.findViewById(ids[i]).setBackgroundResource(R.drawable
                                .current_tv_shape);
                        ((TextView)view.findViewById(ids[i])).setTextColor(getResources()
                                .getColor(R.color.white));
                        url = urls[i];
                        handleUrl = handleUrls[i];
                        type = types[i];
                    } else {
                        view.findViewById(ids[i]).setBackgroundResource(R.drawable
                                .current_tv_shape_un);
                        ((TextView)view.findViewById(ids[i])).setTextColor(getResources()
                                .getColor(R.color.bule));
                    }
                }
                load();
                break;
            case R.id.tv005:
                for (CurrentInfoBean.ListBean item : list) {
                    item.setCheck(true);
                }
                itemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv006:
                for (CurrentInfoBean.ListBean item : list) {
                    if (item.isCheck() == true) {
                        item.setCheck(false);
                    } else {
                        item.setCheck(true);
                    }
                }
                itemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv007:
                getProcessParam();
                break;

        }
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

        StringBuffer str = new StringBuffer();
        for (CurrentInfoBean.ListBean item : list) {
            if (item.isCheck() == true) {
                str.append(item.getId());
                str.append(",");
            }
        }
        if (str.length() > 0) {
            chooseIds = str.substring(0, str.length() - 1);
            prosessDialog = new ProcessDialog(mContext, handler, type);
            prosessDialog.initDialog();
        } else {
           ToastUtil.showMessageDefault(mContext, "请选择处理的内容");
        }
    }

    /**
     * 发送处理意见
     */
    private void senProcess() {
        startLoadingAnim();

        RequestBody requestBody = new FormEncodingBuilder().add("alarmids", chooseIds).add
                ("phone", telNumber).add("x", String.valueOf(longitude)).add("y", String.valueOf
                (latitude)).add("alarmcontent", suggestion).add("alarmstatus", alarmstatus).build();
        final Request request = new Request.Builder().url(handleUrl).post(requestBody).build();
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
                CurrentInfoBean currentInfoBean = gson.fromJson(result, new
                        TypeToken<CurrentInfoBean>() {
                        }.getType());
                boolean success = currentInfoBean.isSuccess();
                if (success) {
                    handler.sendEmptyMessage(10011);
                } else {
                    handler.sendEmptyMessage(10013);
                }
            }
        });
    }

    /**
     * 主listView的点击监听事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailsIntent = new Intent(mContext, CurrentDetailsActivity.class);
        detailsIntent.putExtra("infoDetails", list.get(position - 1));
        detailsIntent.putExtra("type",type);
        detailsIntent.putExtra("handleUrl",handleUrl);
        startActivity(detailsIntent);
    }

    /**
     * 设置pullToRefresh的监听
     */
    private void setListener() {
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(this);
        listView.setPullLabel("下拉刷新最新数据...");// 刚下拉时，显示的提示
        listView.setRefreshingLabel("正在载入...");// 刷新时
        listView.setReleaseLabel("放开进行刷新...");// 下来达到一定距离时，显示的提示
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        load();
    }
}
