package com.wkbp.zngj.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wkbp.zngj.R;

/**
 * Created by weilin on 2016/11/4 10:09
 */
public class CustomDialog extends Activity {

    public static TextView tvTitle, btnLeft, btnRight;
    public static Dialog dialog;

    public static void initDialog(Context context) {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.CustomDialog);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog, null);

        dialog.setContentView(view);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager manager = ((Activity) context).getWindowManager();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        btnRight = (TextView) view.findViewById(R.id.btnRight);
    }
}
