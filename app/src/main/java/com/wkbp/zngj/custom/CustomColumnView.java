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
 * 柱状图
 * Created by weilin on 2016/11/10 11:51
 */
public class CustomColumnView extends View {
    private int ScrWidth, ScrHeight;
    private Paint paintG, paintB;
    private String[] colors = new String[]{"#FF7E50", "#DB70D7", "#87CFFB"};
    private Paint[] aryPaint;
    private TextPaint mPaint;
    private int mHeight;// 每条横线之间的高度
    private int mWidth;// 横向每个柱形宽度/2
    private String[] valuesY;
    private String[] level;
    private int[] values;
    private Context context;

    public CustomColumnView(Context context, String[] valuesY, int[] values, String[] level) {
        super(context);
        this.valuesY = valuesY;
        this.values = values;
        this.level = level;
        this.context = context;
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;

        mHeight = ScrHeight * 3 / 5 / (valuesY.length + 2);
        mWidth = ScrWidth / (level.length * 2 + 2);
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

        //抗锯齿
        paintG.setAntiAlias(true);
        paintB.setAntiAlias(true);
        mPaint.setAntiAlias(true);

        //画笔初始化
        aryPaint = new Paint[level.length];
        for (int i = 0; i < level.length; i++) {
            aryPaint[i] = new Paint();
            aryPaint[i].setColor(Color.parseColor(colors[i]));
            aryPaint[i].setStyle(Paint.Style.FILL);
            aryPaint[i].setAntiAlias(true);
        }

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
                canvas.drawLine(mWidth, (i + 2) * mHeight, mWidth * (level.length * 2 + 1), (i +
                        2) * mHeight, paintB);
            } else {
                canvas.drawLine(mWidth, (i + 2) * mHeight, mWidth * (level.length * 2 + 1), (i +
                        2) * mHeight, paintG);
            }
            canvas.save();//保存画布状态
            StaticLayout layout = new StaticLayout(valuesY[i], mPaint, mWidth, Layout
                    .Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
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
        for (int i = 0; i < (level.length + 1) * 2; i += 2) {
            if (i == 0) {
                canvas.drawLine(mWidth * (i + 1), 2 * mHeight, mWidth * (i + 1), (valuesY.length
                        + 1) * mHeight, paintB);
            } else {
                canvas.drawLine(mWidth * (i + 1), 2 * mHeight, mWidth * (i + 1), (valuesY.length
                        + 1) * mHeight, paintG);
            }
        }
    }

    /**
     * 画底下等级说明
     *
     * @param canvas
     */
    private void drawXText(Canvas canvas) {
        for (int j = 0; j < level.length * 2; j += 2) {
            canvas.save();
            StaticLayout layout = new StaticLayout(level[j / 2], mPaint, mWidth * 2, Layout
                    .Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            canvas.translate((j + 1) * mWidth, (valuesY.length + 1) * mHeight + Util.dip2px
                    (context, 15));
            layout.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 画柱形
     *
     * @param canvas
     */
    private void drawXExplain(Canvas canvas) {
        for (int i = 0; i < level.length * 2; i += 2) {
            canvas.drawRect((float) (mWidth * (i + 1.5)), drawValues(i / 2), (float) (mWidth * (i +
                    2.5)), (valuesY.length + 1) * mHeight, aryPaint[i / 2]);
        }
    }

    /**
     * 柱形的值
     *
     * @param y
     * @return
     */
    private float drawValues(int y) {
        float y0 = 0;
        float y1 = 0;
        y1 = Integer.parseInt(valuesY[0]);
        y0 = values[y];
        float y2 = (1 - y0 / y1) * mHeight * (valuesY.length - 1) + mHeight * 2;
        return y2;
    }

    /**
     * 设置底部标注
     *
     * @param canvas
     */
    private void drawLineOrText(Canvas canvas) {
        //设置线宽
        for (int i = 0; i < aryPaint.length; i++) {
            aryPaint[i].setStrokeWidth((float) Util.sp2px(context, 18));
            aryPaint[i].setTextSize(Util.dip2px(context, 18));
        }
        int height3 = ScrHeight / 3;

        canvas.drawLine(Util.dip2px(context, 10), ScrHeight - height3, Util.dip2px(context, 10) +
                ScrWidth / 8, ScrHeight - height3, aryPaint[0]);
        canvas.drawText(level[0], Util.dip2px(context, 10) + ScrWidth / 8 + Util.dip2px(context,
                10), ScrHeight - height3 + Util.dip2px(context, 8), aryPaint[0]);

        canvas.drawLine(ScrWidth / 3 + Util.dip2px(context, 10), ScrHeight - height3, ScrWidth /
                3 + ScrWidth / 8 + Util.dip2px(context, 10), ScrHeight - height3, aryPaint[1]);
        canvas.drawText(level[1], Util.dip2px(context, 10) + ScrWidth / 3 + ScrWidth / 8 + Util
                .dip2px(context, 10), ScrHeight - height3 + Util.dip2px(context, 8), aryPaint[1]);

        canvas.drawLine(ScrWidth * 2 / 3 + Util.dip2px(context, 10), ScrHeight - height3,
                ScrWidth * 2 / 3 + ScrWidth / 8 + Util.dip2px(context, 10), ScrHeight - height3,
                aryPaint[2]);
        canvas.drawText(level[2], Util.dip2px(context, 10) + ScrWidth * 2 / 3 + ScrWidth / 8 +
                Util.dip2px(context, 10), ScrHeight - height3 + Util.dip2px(context, 8),
                aryPaint[2]);

    }
}