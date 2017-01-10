package com.wkbp.zngj;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.wkbp.zngj.adapter.PopWindowAdapter;
import com.wkbp.zngj.base.BaseFragment;
import com.wkbp.zngj.base.CrashApplication;
import com.wkbp.zngj.bean.CurrentInfoBean;
import com.wkbp.zngj.bean.LevelOrStatusBean;
import com.wkbp.zngj.custom.DateTimePicker;
import com.wkbp.zngj.custom.ProcessDialog;
import com.wkbp.zngj.util.DateTimeUtil;
import com.wkbp.zngj.util.SharedPreferenceUtils;
import com.wkbp.zngj.util.ToastUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shangshuaibo on 2016/11/4 16:19
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener, AdapterView
        .OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    //数据请求url
    private String[] urls;
    private String url;
    //查询报警状态和报警等级的类型url及标签
    private String[] types;
    private String type;
    private String typeUrl;
    //数据处理url
    private String[] handleUrls;
    private String handleUrl;
    //表头四个按钮
    private String[] titleTvs;

    private View view;
    private int[] ids = {R.id.tv001, R.id.tv002, R.id.tv003, R.id.tv004};
    private TextView tv005, tv006, tv007;
    private TextView tv_start_date, tv_start_time, tv_end_date, tv_end_time, tv_level, tv_status;
    private ImageView iv_start_time, iv_end_time, iv_level, iv_status;
    private LinearLayout ll_start_time, ll_end_time, ll_level, ll_status;
    private PullToRefreshListView listView;
    private CurrentItemAdapter itemAdapter;
    private List<CurrentInfoBean.ListBean> list = new ArrayList<>();
    private List<LevelOrStatusBean.ListBean> typeList = new ArrayList<>();
    private List<String> strTypeList = new ArrayList<>();

    private PopupWindow popupWindow;
    private String levelKey = "ALL"; //报警等级KeyDate
    private String statusKey = "ALL";  //报警状态KeyDate
    private int nowPage = 1;  //当前页
    private int totlePage = 1;  //总页数

    //以下数据为处理报警信息时所用到的
    private ProcessDialog processDialog;
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
                    //showErrorPageState(nowPage);
                    ToastUtil.showMessageDefault(mContext, "网络请求失败");
                    listView.onRefreshComplete();
                    break;
                case 10005:
                    stopLoadingAnim();
                    //showErrorPageState(nowPage);
                    ToastUtil.showMessageDefault(mContext, "获取失败，请稍后再试");
                    listView.onRefreshComplete();
                    break;
                case 10006:
                    stopLoadingAnim();
                    levelStatusPop(strTypeList, ll_level, iv_level, tv_level);
                    break;
                case 10007:
                    stopLoadingAnim();
                    levelStatusPop(strTypeList, ll_status, iv_status, tv_status);
                    break;
                case 10008:
                    listView.onRefreshComplete();
                    ToastUtil.showMessageDefault(mContext, "没有更多数据了");
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
                    processDialog.dialog.dismiss();
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
            view = inflater.inflate(R.layout.fragment_history, container, false);
        }
        urls = getResources().getStringArray(R.array.historyt_request_urls);
        handleUrls = getResources().getStringArray(R.array.send_urls);
        titleTvs = getResources().getStringArray(R.array.current_title);
        types = getResources().getStringArray(R.array.level_status_type);
        url = urls[0];
        handleUrl = handleUrls[0];
        type = types[0];
        typeUrl = getResources().getString(R.string.level_status_url);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

        tv_start_date = (TextView) view.findViewById(R.id.tv_start_date);
        tv_start_time = (TextView) view.findViewById(R.id.tv_start_time);
        tv_end_date = (TextView) view.findViewById(R.id.tv_end_date);
        tv_end_time = (TextView) view.findViewById(R.id.tv_end_time);
        tv_level = (TextView) view.findViewById(R.id.tv_level);
        tv_status = (TextView) view.findViewById(R.id.tv_status);

        iv_start_time = (ImageView) view.findViewById(R.id.iv_start_time);
        iv_end_time = (ImageView) view.findViewById(R.id.iv_end_time);
        iv_level = (ImageView) view.findViewById(R.id.iv_level);
        iv_status = (ImageView) view.findViewById(R.id.iv_status);

        ll_start_time = (LinearLayout) view.findViewById(R.id.ll_start_time);
        ll_end_time = (LinearLayout) view.findViewById(R.id.ll_end_time);
        ll_level = (LinearLayout) view.findViewById(R.id.ll_level);
        ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        ll_start_time.setOnClickListener(this);
        ll_end_time.setOnClickListener(this);
        ll_level.setOnClickListener(this);
        ll_status.setOnClickListener(this);

        tv005 = (TextView) view.findViewById(R.id.tv005);
        tv006 = (TextView) view.findViewById(R.id.tv006);
        tv007 = (TextView) view.findViewById(R.id.tv007);
        tv005.setOnClickListener(this);
        tv006.setOnClickListener(this);
        tv007.setOnClickListener(this);

        Date end = new Date();
        Date start = DateTimeUtil.addDay(-7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strStart = sdf.format(start);
        String strEnd = sdf.format(end);

        String startDate = strStart.substring(0, 10);
        String startTime = strStart.substring(11, strStart.length());
        tv_start_date.setText(startDate);
        tv_start_time.setText(startTime);
        String endDate = strEnd.substring(0, 10);
        String endTime = strEnd.substring(11, strEnd.length());
        tv_end_date.setText(endDate);
        tv_end_time.setText(endTime);


        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        itemAdapter = new CurrentItemAdapter(mContext, list);
        listView.setAdapter(itemAdapter);
    }

    @Override
    protected void load() {
        super.load();
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("alarmlevel", levelKey).add
                ("alarmstatus", statusKey).add("starttime", tv_start_date.getText().toString()
                + " " + tv_start_time.getText().toString()).add("endtime", tv_end_date.getText
                ().toString() + " " + tv_end_time.getText().toString()).add("page", String
                .valueOf(nowPage)).build();
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
                    totlePage = currentInfoBean.getTotal() % 10 == 0 ? currentInfoBean.getTotal()
                            / 10 : currentInfoBean.getTotal() / 10 + 1;
                    handler.sendEmptyMessage(10003);
                } else {
                    handler.sendEmptyMessage(10005);
                }
            }
        });
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("alarmlevel", levelKey).add
                ("alarmstatus", statusKey).add("starttime", tv_start_date.getText().toString()
                + " " + tv_start_time.getText().toString()).add("endtime", tv_end_date.getText
                ().toString() + " " + tv_end_time.getText().toString()).add("page", String
                .valueOf(nowPage)).build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(10004);
                nowPage--;
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
                    totlePage = currentInfoBean.getTotal() % 10 == 0 ? currentInfoBean.getTotal()
                            / 10 : currentInfoBean.getTotal() / 10 + 1;
                    list.addAll(currentInfoBean.getList());
                    handler.sendEmptyMessage(10003);
                } else {
                    handler.sendEmptyMessage(10005);
                    nowPage--;
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
                        type = types[i];
                        handleUrl = handleUrls[i];
                    } else {
                        view.findViewById(ids[i]).setBackgroundResource(R.drawable
                                .current_tv_shape_un);
                        ((TextView)view.findViewById(ids[i])).setTextColor(getResources()
                                .getColor(R.color.bule));
                    }
                }
                nowPage = 1;
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
            case R.id.ll_start_time:
                createDateTimeDialog(tv_start_date, tv_start_time);
                break;
            case R.id.ll_end_time:
                createDateTimeDialog(tv_end_date, tv_end_time);
                break;
            case R.id.ll_level:
                iv_level.setImageResource(R.drawable.ic_up);
                loadType("alarmlevel", 10006);
                break;
            case R.id.ll_status:
                iv_status.setImageResource(R.drawable.ic_up);
                loadType("alarmstatus", 10007);
                break;
        }
    }

    /**
     * 获取报警状态和报警等级列表中的数据
     *
     * @param levelOrStatus
     * @param arg           为了区分是点击的报警状态还是报警等级
     */
    private void loadType(String levelOrStatus, final int arg) {
        startLoadingAnim();
        RequestBody requestBody = new FormEncodingBuilder().add("type", levelOrStatus).add
                ("work", type).build();
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
                    strTypeList.add("全部");
                    typeList.addAll(typeBean.getList());
                    for (int i = 0; i < typeList.size(); i++) {
                        strTypeList.add(typeList.get(i).getValue());
                    }
                    handler.sendEmptyMessage(arg);
                } else {
                    handler.sendEmptyMessage(10014);
                }
            }
        });
    }

    /**
     * 点击报警状态或者报警等级时弹出
     *
     * @param data 下拉列表中的数据
     * @param ll   所点击下拉列表的LinearLayout
     * @param iv   下拉列表中的向下或向上箭头
     * @param tv   下拉列表中显示的文字
     */
    private void levelStatusPop(final List<String> data, LinearLayout ll, final ImageView iv, final
    TextView tv) {
        popupWindow = new PopupWindow(mContext);
        View view = View.inflate(mContext, R.layout.popupwindow, null);
        popupWindow.setContentView(view);
        int width = ll_start_time.getWidth();
        popupWindow.setWidth(width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(ll);
        popupWindow.update();

        ListView popListView = (ListView) view.findViewById(R.id.pop_list);
        popListView.setAdapter(new PopWindowAdapter(mContext, data));

        popListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position,
                                            long id) {
                        popupWindow.dismiss();
                        tv.setText(data.get(position));

                        //计算KeyData
                        if (tv == tv_level) {
                            if (tv_level.getText().toString().equals("全部")) {
                                levelKey = "ALL";
                            } else {
                                for (int i = 0; i < strTypeList.size(); i++) {
                                    if (strTypeList.get(i).equals(tv_level.getText().toString())) {
                                        levelKey = typeList.get(i - 1).getKeydata();
                                    }
                                }
                            }
                        } else if (tv == tv_status) {
                            if (tv_status.getText().toString().equals("全部")) {
                                statusKey = "ALL";
                            } else {
                                for (int i = 0; i < strTypeList.size(); i++) {
                                    if (strTypeList.get(i).equals(tv_status.getText().toString())) {
                                        statusKey = typeList.get(i - 1).getKeydata();
                                    }
                                }
                            }
                        }
                        nowPage = 1;
                        load();
                        iv.setImageResource(R.drawable.ic_down);
                    }
                }

        );
    }

    /**
     * 创建选择日期时间的Dialog
     *
     * @param tvDate
     * @param tvTime
     */
    private void createDateTimeDialog(final TextView tvDate, final TextView tvTime) {
        DateTimePicker timePicker = new DateTimePicker(getActivity(),
                new DateTimePicker.ICustomDateTimeListener() {
                    @Override
                    public void onSet(Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = format.format(dateSelected);
                        String strDate = str.substring(0, 10);
                        String strTime = str.substring(11, str.length());
                        tvDate.setText(strDate);
                        tvTime.setText(strTime);
                        nowPage = 1;
                        load();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("datetimepickerdialog", "canceled");
                    }
                });
        timePicker.set24HourFormat(true);
        timePicker.showDialog();
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
            processDialog = new ProcessDialog(mContext, handler, type);
            processDialog.initDialog();
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
        Intent detailsIntent = new Intent(getActivity(), CurrentDetailsActivity.class);
        detailsIntent.putExtra("infoDetails", list.get(position - 1));
        detailsIntent.putExtra("type", type);
        detailsIntent.putExtra("handleUrl", handleUrl);
        startActivity(detailsIntent);
    }

    /**
     * 设置pullToRefresh的监听
     */
    private void setListener() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);
        listView.setPullLabel("下拉刷新最新数据...");// 刚下拉时，显示的提示
        listView.setRefreshingLabel("正在载入...");// 刷新时
        listView.setReleaseLabel("放开进行刷新...");// 下来达到一定距离时，显示的提示

        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多数据...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在载入...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开进行加载... ");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        nowPage = 1;
        load();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (nowPage < totlePage) {
            nowPage++;
            loadMore();
        } else {
            handler.sendEmptyMessage(10008);
        }
    }
}