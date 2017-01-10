package com.wkbp.zngj.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.wkbp.zngj.R;

/**
 * 告警趋势
 * Created by weilin on 2016/11/4 10:09
 */
public class AlarmDistributedDialog extends Activity {
    private static Dialog dialog;

    public static void initDialog(Context context, String[] valuesY, String[] month, String[]
            ygValues, String[] dhValues, String[] hwValues, String[] utValues) {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.CustomDialog);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        CustomLineView lineView = new CustomLineView(context, valuesY, month, ygValues, dhValues,
                hwValues, utValues);
        dialog.setContentView(lineView);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        lineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
