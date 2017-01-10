package com.wkbp.zngj.base;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wkbp.zngj.R;
import com.wkbp.zngj.util.NetWorkUtil;
import com.wkbp.zngj.util.ToastUtil;
import com.wkbp.zngj.util.Util;

/**
 * Created by weilin on 2016/11/4 10:15
 */
public class BaseFragment extends Fragment {

    protected Activity mContext;
    private boolean superOnCreateViewCalled;

    private View loading;
    private ImageView ivIncludeLoading;
    private AnimationDrawable animDraw;
    private LinearLayout ll_loading;
    private View service_error;
    private TextView service_error_hint;
    private ImageView service_error_image;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!superOnCreateViewCalled)
            throw new IllegalStateException(
                    "每个子类必须调用超类的onCreateView方法,来获取mFloatWindow对象");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        superOnCreateViewCalled = true;
        return super.onCreateView(inflater, container, savedInstanceState);
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

    public void addIncludeLoading() {
        initLoading();
    }

    private void initLoading() {
        if (loading == null) {
            ViewGroup contentView = (ViewGroup) getActivity().getWindow()
                    .getDecorView().findViewById(android.R.id.content);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1,
                    -1);
            params.gravity = Gravity.BOTTOM;
            loading = LayoutInflater.from(getActivity()).inflate(
                    R.layout.include_loading, contentView, false);
            loading.setLayoutParams(params);
            contentView.addView(loading);
        }
        ll_loading = (LinearLayout) loading.findViewById(R.id.ll_loading);
        ivIncludeLoading = (ImageView) loading
                .findViewById(R.id.iv_include_loading);
        loading.setClickable(true);
        ivIncludeLoading.setBackgroundResource(R.drawable.anim_loading);
        animDraw = (AnimationDrawable) ivIncludeLoading.getBackground();
        stopLoadingAnim();
    }

    /**
     * @param titleHeightOfDip
     *
     *            titleHeightOfDip：要减去的Title的高度
     */
    public void addIncludeLoading(int titleHeightOfDip) {
        initLoading(true, titleHeightOfDip);
    }

    private void initLoading(boolean isNeedToMinusTitleHeight,
                             int titleHeightOfDip) {
        if (loading == null) {
            FrameLayout contentView = (FrameLayout) getView();
            int h = -1;
            if (isNeedToMinusTitleHeight) {
                h = Util.getWindowHeight(getActivity())
                        - Util.dip2px(getActivity(), titleHeightOfDip)
                        - Util.getStatusHeight(getActivity());
            } else {
                h = Util.getWindowHeight(getActivity())
                        - Util.getStatusHeight(getActivity());
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1,
                    h);
            params.gravity = Gravity.BOTTOM;
            loading = LayoutInflater.from(getActivity()).inflate(
                    R.layout.include_loading, contentView, false);
            loading.setLayoutParams(params);
            contentView.addView(loading);
        }
        ivIncludeLoading = (ImageView) loading
                .findViewById(R.id.iv_include_loading);
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
        if (service_error == null) {
            service_error = View.inflate(getActivity(), R.layout.service_error,
                    null);
        }
        ViewGroup parent = (ViewGroup) service_error.getParent();
        if (parent != null) {
            parent.removeView(service_error);
        }
        ((FrameLayout) getView()).addView(service_error);
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
     *
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
     *
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
}
