package com.wkbp.zngj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wkbp.zngj.base.BaseActivity;
import com.wkbp.zngj.util.ToastUtil;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_current, ll_history, ll_statistics, ll_about;
    private ImageView iv_current, iv_history, iv_statistics, iv_about;
    private TextView tv_current, tv_history, tv_statistics, tv_about;

    private int index;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        initView();
        initFragments();
    }

    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new CurrentFragment(), "CurrentFragment");
        ft.commit();
    }

    private void initView() {
        iv_current = (ImageView) findViewById(R.id.iv_current);
        iv_history = (ImageView) findViewById(R.id.iv_history);
        iv_statistics = (ImageView) findViewById(R.id.iv_statistics);
        iv_about = (ImageView) findViewById(R.id.iv_about);

        tv_current = (TextView) findViewById(R.id.tv_current);
        tv_history = (TextView) findViewById(R.id.tv_history);
        tv_statistics = (TextView) findViewById(R.id.tv_statistics);
        tv_about = (TextView) findViewById(R.id.tv_about);

        ll_current = (LinearLayout) findViewById(R.id.ll_current);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        ll_statistics = (LinearLayout) findViewById(R.id.ll_statistics);
        ll_about = (LinearLayout) findViewById(R.id.ll_about);

        ll_current.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_statistics.setOnClickListener(this);
        ll_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        switch (v.getId()) {
            case R.id.ll_current:
                index = 0;
                ft.replace(R.id.fragment_container, new CurrentFragment(), "CurrentFragment");
                break;
            case R.id.ll_history:
                index = 1;
                ft.replace(R.id.fragment_container, new HistoryFragment(), "HistoryFragment");
                break;
            case R.id.ll_statistics:
                index = 2;
                ft.replace(R.id.fragment_container, new StatisticsFragment(), "StatisticsFragment");
                break;
            case R.id.ll_about:
                index = 3;
                ft.replace(R.id.fragment_container, new AboutFragment(), "AboutFragment");
                break;
        }
        ft.commit();
        updateBottom(index);
    }

    private void updateBottom(int position) {
        switch (position) {
            case 0:
                iv_current.setImageResource(R.drawable.bottom01_selecte);
                iv_history.setImageResource(R.drawable.bottom02_unselecte);
                iv_statistics.setImageResource(R.drawable.bottom03_unselecte);
                iv_about.setImageResource(R.drawable.bottom04_unselecte);
                tv_current.setTextColor(getResources().getColor(R.color.bule));
                tv_history.setTextColor(getResources().getColor(R.color.gray));
                tv_statistics.setTextColor(getResources().getColor(R.color.gray));
                tv_about.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 1:
                iv_current.setImageResource(R.drawable.bottom01_unselecte);
                iv_history.setImageResource(R.drawable.bottom02_selecte);
                iv_statistics.setImageResource(R.drawable.bottom03_unselecte);
                iv_about.setImageResource(R.drawable.bottom04_unselecte);
                tv_current.setTextColor(getResources().getColor(R.color.gray));
                tv_history.setTextColor(getResources().getColor(R.color.bule));
                tv_statistics.setTextColor(getResources().getColor(R.color.gray));
                tv_about.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 2:
                iv_current.setImageResource(R.drawable.bottom01_unselecte);
                iv_history.setImageResource(R.drawable.bottom02_unselecte);
                iv_statistics.setImageResource(R.drawable.bottom03_selecte);
                iv_about.setImageResource(R.drawable.bottom04_unselecte);
                tv_current.setTextColor(getResources().getColor(R.color.gray));
                tv_history.setTextColor(getResources().getColor(R.color.gray));
                tv_statistics.setTextColor(getResources().getColor(R.color.bule));
                tv_about.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 3:
                iv_current.setImageResource(R.drawable.bottom01_unselecte);
                iv_history.setImageResource(R.drawable.bottom02_unselecte);
                iv_statistics.setImageResource(R.drawable.bottom03_unselecte);
                iv_about.setImageResource(R.drawable.bottom04_selecte);
                tv_current.setTextColor(getResources().getColor(R.color.gray));
                tv_history.setTextColor(getResources().getColor(R.color.gray));
                tv_statistics.setTextColor(getResources().getColor(R.color.gray));
                tv_about.setTextColor(getResources().getColor(R.color.bule));
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime >= 2000) {
                ToastUtil.showMessageDefault(this, "再点一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
