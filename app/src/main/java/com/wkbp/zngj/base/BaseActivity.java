package com.wkbp.zngj.base;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.wkbp.zngj.R;
import com.wkbp.zngj.util.NetWorkUtil;
import com.wkbp.zngj.util.ToastUtil;
import com.wkbp.zngj.util.Util;

/**
 * Created by weilin on 2016/11/4 10:04
 */
public class BaseActivity extends FragmentActivity {

    protected Activity mContext;

    private View loading;
    private ImageView ivIncludeLoading;
    private AnimationDrawable animDraw;
    private LinearLayout ll_loading;
    private View service_error;
    private TextView service_error_hint;
    private ImageView service_error_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        CrashApplication.allActivity.add(this);
        mContext = this;
        PushAgent.getInstance(mContext).onAppStart();
    }

    /**
     * 初始化自定义标题栏
     * @param titleName
     */
    protected void setTitleBar(String titleName){
        findViewById(R.id.custom_back_image).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.custom_title_text)).setText(titleName);
    }

    /**
     * 有加载事件
     */
    protected void load() {
    }

    /**
     * 加载更多
     */
    protected void loadMore() {
        if (!NetWorkUtil.isNetworkConnected(CrashApplication.context)) {
            ToastUtil.showMessageDefault(CrashApplication.context, "网络不给力");
            return;
        }
    }

    /**
     * @param isNeedToMinusTitleHeight 是否要减去Title的高度，默认减去Title高度为45dp
     */
    public void addIncludeLoading(boolean isNeedToMinusTitleHeight) {
        initLoading(isNeedToMinusTitleHeight, 45);
    }

    private void initLoading(boolean isNeedToMinusTitleHeight,
                             int titleHeightOfDip) {
        if (loading == null) {
            ViewGroup contentView = (ViewGroup) this.getWindow().getDecorView()
                    .findViewById(android.R.id.content);
            int h = -1;
            if (isNeedToMinusTitleHeight) {
                h = Util.getWindowHeight(this)
                        - Util.dip2px(this, titleHeightOfDip)
                        - Util.getStatusHeight(this);
            } else {
                h = Util.getWindowHeight(this)
                        - Util.getStatusHeight(this);
            }
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(-1,
                    h);
            params.gravity = Gravity.BOTTOM;
            loading = LayoutInflater.from(this).inflate(
                    R.layout.include_loading, contentView, false);
            loading.setLayoutParams(params);
            contentView.addView(loading);
        }
        // 主布局,控制颜色变化
        ivIncludeLoading = (ImageView) loading
                .findViewById(R.id.iv_include_loading);
        ll_loading = (LinearLayout) loading.findViewById(R.id.ll_loading);
        loading.setClickable(true);

        ivIncludeLoading.setBackgroundResource(R.drawable.anim_loading);
        animDraw = (AnimationDrawable) ivIncludeLoading.getBackground();

        stopLoadingAnim();
    }

    /**
     * 开始加载动画
     * 透明遮罩
     */
    public void startLoadingAnim() {
        ll_loading.setBackgroundColor(Color.parseColor("#20ffffff"));
        if (loading == null) {
            return;
        }
        loading.setVisibility(View.VISIBLE);
        if (animDraw != null) {
            animDraw.start();
        }
    }

    /**
     * 停止加载动画
     */
    public void stopLoadingAnim() {
        if (animDraw != null) {
            animDraw.stop();
        }
        if (loading != null) {
            loading.setVisibility(View.GONE);
        }
    }

    /**
     * 给子页面添加网络异常，和服务器忙的错误提示页面 注意：这个方法一定要在addIncludeLoading（）方法前调用
     */
    public void initErrorPage() {
        service_error = View.inflate(this, R.layout.service_error, null);
        // 获取根视图
        ViewGroup contentView = (ViewGroup) this.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        // 获取异常页面的高度
        int height = Util.getWindowHeight(this)
                - Util.dip2px(this, 45)
                - Util.getStatusHeight(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, height);
        layoutParams.gravity = Gravity.BOTTOM;
        service_error.setLayoutParams(layoutParams);
        contentView.addView(service_error);
        service_error_hint = (TextView) service_error
                .findViewById(R.id.service_error_hint);
        service_error_image = (ImageView) service_error
                .findViewById(R.id.service_error_image);
        Button service_error_btn = (Button) service_error
                .findViewById(R.id.service_error_btn);
        service_error_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errorLoading();
            }
        });
        service_error.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errorLoading();
            }
        });
        hideErrorPageState();
    }

    /**
     * 显示错误提示页面
     * @param pageIndex
     */
    protected void showErrorPageState(int pageIndex) {
        stopLoadingAnim();
        if (pageIndex == 1) {
            if (!NetWorkUtil.isNetworkConnected(CrashApplication.context)) {
                showServiceErrorView();
            } else {
                showServiceBusyView();
            }
        }
    }

    /**
     * 隐藏错误提示页面
     */
    protected void hideErrorPageState() {
        if (service_error != null) {
            service_error.setVisibility(View.GONE);
        }
    }

    /**
     * 屏幕被点击或者按钮被点击
     */
    private void errorLoading() {
        if (!NetWorkUtil.isNetworkConnected(CrashApplication.context)) {
            ToastUtil.showMessageDefault(CrashApplication.context, "网络不给力");
            return;
        }
        startLoadingAnim();
        load();
    }

    /**
     * 显示服务繁忙的页面
     */
    private void showServiceBusyView() {
        if (service_error != null) {
            service_error_hint.setText(getResources().getString(
                    R.string.service_error_hint));
            service_error_image.setImageResource(R.drawable.service_error);
            service_error.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示服务异常页面
     */
    private void showServiceErrorView() {
        if (service_error != null) {
            service_error_hint.setText(getResources().getString(
                    R.string.service_error_hint));
            service_error_image.setImageResource(R.drawable.service_error);
            service_error.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 页面为空
     * @param tip 空页面文案提示
     */
    protected void showEmpty(String tip) {
        stopLoadingAnim();
        if (service_error != null) {
            service_error_hint.setText(tip);
            service_error_image.setImageResource(R.drawable.content_empty);
            service_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CrashApplication.allActivity.remove(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
