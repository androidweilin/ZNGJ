package com.wkbp.zngj.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;

import com.wkbp.zngj.util.Util;

/**
 * 折线图
 * Created by weilin on 2016/11/10 18:51
 */
public class CustomLineView extends View {
    private int ScrWidth, ScrHeight;
    private Paint paintYG, paintDH, paintHW, paintUT, paintG, paintB;
    private TextPaint mPaint;
    private int mHeight;// 每条横线之间的高度
    private int mWidth;// 横向月份之间的宽度
    private String[] valuesY;
    private String[] month;
    private String[] ygValues;
    private String[] dhValues;
    private String[] hwValues;
    private String[] utValues;

    private Context context;

    public CustomLineView(Context context, String[] valuesY, String[] month, String[] ygValues,
                          String[] dhValues, String[] hwValues, String[] utValues) {
        super(context);
        this.month = month;
        this.ygValues = ygValues;
        this.dhValues = dhValues;
        this.hwValues = hwValues;
        this.utValues = utValues;
        this.valuesY = valuesY;
        this.context = context;
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;

        mHeight = ScrHeight * 3 / 5 / (valuesY.length + 2);
        mWidth = ScrWidth / (month.length + 1);

    }

    public void onDraw(Canvas canvas) {
        //画布背景
        canvas.drawColor(Color.WHITE);
        //画笔初始化
        paintG = new Paint();
        paintG.setStyle(Paint.Style.FILL);
        paintG.setColor(Color.GRAY);

        paintB = new Paint();
        paintB.setStyle(Paint.Style.FILL);
        paintB.setColor(Color.BLACK);
        paintB.setStrokeWidth(Util.dip2px(context, 2));

        mPaint = new TextPaint();
        mPaint.setTextSize(Util.sp2px(context, 16));
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL);

        paintYG = new Paint();
        paintYG.setColor(Color.parseColor("#FF7E50"));
        paintYG.setStyle(Paint.Style.FILL);
        paintYG.setStrokeWidth(Util.dip2px(context, 2));

        paintDH = new Paint();
        paintDH.setColor(Color.parseColor("#DB70D7"));
        paintDH.setStyle(Paint.Style.FILL);
        paintDH.setStrokeWidth(Util.dip2px(context, 2));

        paintHW = new Paint();
        paintHW.setColor(Color.parseColor("#87CFFB"));
        paintHW.setStyle(Paint.Style.FILL);
        paintHW.setStrokeWidth(Util.dip2px(context, 2));

        paintUT = new Paint();
        paintUT.setColor(Color.parseColor("#32CD32"));
        paintUT.setStyle(Paint.Style.FILL);
        paintUT.setStrokeWidth(Util.dip2px(context, 2));

        //抗锯齿
        paintG.setAntiAlias(true);
        paintB.setAntiAlias(true);
        mPaint.setAntiAlias(true);
        paintYG.setAntiAlias(true);
        paintDH.setAntiAlias(true);
        paintHW.setAntiAlias(true);
        paintUT.setAntiAlias(true);

        drawXLine(canvas);
        drawYLine(canvas);
        drawXText(canvas);
        drawXExplain(canvas);
        drawLineOrText(canvas);
    }

    /**
     * 画横轴
     *
     * @param canvas
     */
    private void drawXLine(Canvas canvas) {
        for (int i = 0; i < valuesY.length; i++) {
            if (i == valuesY.length - 1) {
                canvas.drawLine(mWidth, (i + 2) * mHeight, mWidth * month.length, (i + 2) *
                        mHeight, paintB);
            } else {
                canvas.drawLine(mWidth, (i + 2) * mHeight, mWidth * month.length, (i + 2) *
                        mHeight, paintG);
            }
            canvas.save();//保存画布状态
            StaticLayout layout = new StaticLayout(valuesY[i], mPaint, mWidth, Layout
                    .Alignment.ALIGN_CENTER, 1.0F,
                    0.0F, true);
            canvas.translate(5, (i + 2) * mHeight - Util.dip2px(context, 10));
            layout.draw(canvas);
            canvas.restore();//取出画布状态
        }
    }

    /**
     * 画纵轴
     *
     * @param canvas
     */
    private void drawYLine(Canvas canvas) {
        for (int i = 0; i < month.length; i++) {
            if (i == 0) {
                canvas.drawLine(mWidth * (i + 1), 2 * mHeight, mWidth * (i + 1), (valuesY
                        .length + 1) * mHeight, paintB);
            } else {
                canvas.drawLine(mWidth * (i + 1), 2 * mHeight, mWidth * (i + 1), (valuesY
                        .length + 1) * mHeight, paintG);
            }

        }
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawXExplain(Canvas canvas) {
        // 画毅格的线
        for (int j = 0; j < month.length - 1; j++) {
            canvas.drawLine((float) (mWidth * (j + 1)), drawYG(j), (float) (mWidth * (j + 2)),
                    drawYG(j + 1), paintYG);
        }

        // 画动环的线
        for (int j = 0; j < month.length - 1; j++) {
            canvas.drawLine((float) (mWidth * (j + 1)), drawDH(j), (float) (mWidth * (j + 2)),
                    drawDH(j + 1), paintDH);
        }

        // 画华为的线
        for (int j = 0; j < month.length - 1; j++) {
            canvas.drawLine((float) (mWidth * (j + 1)), drawHW(j), (float) (mWidth * (j + 2)),
                    drawHW(j + 1), paintHW);
        }

        // 画UT的线
        for (int j = 0; j < month.length - 1; j++) {
            canvas.drawLine((float) (mWidth * (j + 1)), drawUT(j), (float) (mWidth * (j + 2)),
                    drawUT(j + 1), paintUT);
        }
    }

    /**
     * 毅格的值
     *
     * @param y
     * @return
     */
    private float drawYG(int y) {
        float y0 = 0;
        float y1 = 0;
        y1 = Integer.parseInt(valuesY[0]);
        y0 = Integer.parseInt(ygValues[y]);
        float y2 = (1 - y0 / y1) * mHeight * (valuesY.length - 1) + mHeight * 2;
        return y2;
    }

    /**
     * 动环的值
     *
     * @param y
     * @return
     */
    private float drawDH(int y) {
        float y0 = 0;
        float y1 = 0;
        y1 = Integer.parseInt(valuesY[0]);
        y0 = Integer.parseInt(dhValues[y]);
        float y2 = (1 - y0 / y1) * mHeight * (valuesY.length - 1) + mHeight * 2;
        return y2;
    }

    /**
     * 华为的值
     *
     * @param y
     * @return
     */
    private float drawHW(int y) {
        float y0 = 0;
        float y1 = 0;
        y1 = Integer.parseInt(valuesY[0]);
        y0 = Integer.parseInt(hwValues[y]);
        float y2 = (1 - y0 / y1) * mHeight * (valuesY.length - 1) + mHeight * 2;
        return y2;
    }

    /**
     * UT的值
     *
     * @param y
     * @return
     */
    private float drawUT(int y) {
        float y0 = 0;
        float y1 = 0;
        y1 = Integer.parseInt(valuesY[0]);
        y0 = Integer.parseInt(utValues[y]);
        float y2 = (1 - y0 / y1) * mHeight * (valuesY.length - 1) + mHeight * 2;
        return y2;
    }

    /**
     * 画底下四个月份
     *
     * @param canvas
     */
    private void drawXText(Canvas canvas) {
        for (int j = 0; j < month.length; j++) {
            canvas.save();
            StaticLayout layout = new StaticLayout(month[j], mPaint, mWidth, Layout.Alignment
                    .ALIGN_NORMAL, 1.0F, 0.0F, true);
            canvas.translate((j + 1) * mWidth - 15, (valuesY.length + 1) * mHeight + Util.dip2px
                    (context, 15));
            layout.draw(canvas);
            canvas.restore();
        }

    }

    /**
     * 设置底部标注
     *
     * @param canvas
     */
    private void drawLineOrText(Canvas canvas) {
        float cirY = ScrHeight / 3;
        float height4 = ScrHeight / 4;
        //设置线宽
        paintYG.setStrokeWidth((float) Util.dip2px(context, 18));
        paintDH.setStrokeWidth((float) Util.dip2px(context, 18));
        paintHW.setStrokeWidth((float) Util.dip2px(context, 18));
        paintUT.setStrokeWidth((float) Util.dip2px(context, 18));
        //设置字体大小
        paintYG.setTextSize(Util.sp2px(context, 18));
        paintDH.setTextSize(Util.sp2px(context, 18));
        paintHW.setTextSize(Util.sp2px(context, 18));
        paintUT.setTextSize(Util.sp2px(context, 18));

        canvas.drawLine(Util.dip2px(context, 60), ScrHeight - cirY, Util.dip2px(context, 60) +
                ScrWidth / 8, ScrHeight - cirY, paintYG);
        canvas.drawText("毅格", Util.dip2px(context, 60) + ScrWidth / 8 + Util.dip2px(context, 10),
                ScrHeight - cirY + Util.dip2px(context, 8), paintYG);

        canvas.drawLine(ScrWidth / 2, ScrHeight - cirY, ScrWidth / 2 + ScrWidth / 8, ScrHeight -
                cirY, paintDH);
        canvas.drawText("动环", ScrWidth / 2 + ScrWidth / 8 + Util.dip2px(context, 10), ScrHeight -
                cirY + Util.dip2px(context, 8), paintDH);

        canvas.drawLine(Util.dip2px(context, 60), ScrHeight - height4, Util.dip2px(context, 60) +
                ScrWidth / 8, ScrHeight - height4, paintHW);
        canvas.drawText("华为", Util.dip2px(context, 60) + ScrWidth / 8 + Util.dip2px(context, 10),
                ScrHeight - height4 + Util.dip2px(context, 8), paintHW);

        canvas.drawLine(ScrWidth / 2, ScrHeight - height4, ScrWidth / 2 + ScrWidth / 8, ScrHeight -
                height4, paintUT);
        canvas.drawText("UT", ScrWidth / 2 + ScrWidth / 8 + Util.dip2px(context, 10), ScrHeight -
                height4 + Util.dip2px(context, 8), paintUT);
    }
}
