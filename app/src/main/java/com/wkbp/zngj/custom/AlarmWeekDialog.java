package com.wkbp.zngj.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.wkbp.zngj.R;

/**
 * 一周内告警
 * Created by weilin on 2016/11/4 10:09
 */
public class AlarmWeekDialog extends Activity {
    private static Dialog dialog;

    public static void initDialog(Context context, float a, float b, float c, float d) {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.CustomDialog);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        CustomFanChartView fanChartView = new CustomFanChartView(context, a, b, c, d);
        dialog.setContentView(fanChartView);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        fanChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
