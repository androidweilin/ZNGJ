package com.wkbp.zngj.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

import com.wkbp.zngj.util.Util;
import com.wkbp.zngj.util.XChartCalc;

import java.math.BigDecimal;

/**
 * 饼状图
 * Created by weilin on 2016/11/9 18:51
 */
public class CustomFanChartView extends View {
    private int ScrWidth, ScrHeight;
    Paint paintYG, paintDH, paintHW, paintUT;
    float ygCount;
    float dhCount;
    float hwCount;
    float utCount;

    float ygCountP;
    float dhCountP;
    float hwCountP;
    float utCountP;

    float cirX;
    float cirY;
    private Context context;

    public CustomFanChartView(Context context, float a, float b, float c, float d) {
        super(context);
        this.context = context;
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;

        //初始角度
        ygCount = a / (a + b + c + d) * 360f;
        dhCount = b / (a + b + c + d) * 360f;
        hwCount = c / (a + b + c + d) * 360f;
        utCount = 360f - ygCount - dhCount - hwCount;
        //保留两位小数
        BigDecimal bd = BigDecimal.valueOf(ygCount);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);//返回一个新的bd对象
        ygCount = (float) bd.doubleValue();//把值从对象中取出来

        BigDecimal bd1 = BigDecimal.valueOf(dhCount);
        bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
        dhCount = (float) bd1.doubleValue();

        BigDecimal bd2 = BigDecimal.valueOf(hwCount);
        bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
        hwCount = (float) bd2.doubleValue();

        BigDecimal bd3 = BigDecimal.valueOf(utCount);
        bd3 = bd3.setScale(2, BigDecimal.ROUND_HALF_UP);
        utCount = (float) bd3.doubleValue();

        //初始百分比
        ygCountP = a / (a + b + c + d) * 100f;
        dhCountP = b / (a + b + c + d) * 100f;
        hwCountP = c / (a + b + c + d) * 100f;
        utCountP = 100f - ygCountP - dhCountP - hwCountP;
        //保留两位小数
        BigDecimal bd00 = BigDecimal.valueOf(ygCountP);
        bd00 = bd00.setScale(2, BigDecimal.ROUND_HALF_UP);//返回一个新的bd对象
        ygCountP = (float) bd00.doubleValue();//把值从对象中取出来

        BigDecimal bd01 = BigDecimal.valueOf(dhCountP);
        bd01 = bd01.setScale(2, BigDecimal.ROUND_HALF_UP);
        dhCountP = (float) bd01.doubleValue();

        BigDecimal bd02 = BigDecimal.valueOf(hwCountP);
        bd02 = bd02.setScale(2, BigDecimal.ROUND_HALF_UP);
        hwCountP = (float) bd02.doubleValue();

        BigDecimal bd03 = BigDecimal.valueOf(utCountP);
        bd03 = bd03.setScale(2, BigDecimal.ROUND_HALF_UP);
        utCountP = (float) bd03.doubleValue();

    }

    public void onDraw(Canvas canvas) {
        //画布背景
        canvas.drawColor(Color.WHITE);
        //标注画笔初始化
        Paint PaintBlue = new Paint();
        PaintBlue.setColor(Color.BLUE);
        PaintBlue.setStyle(Paint.Style.FILL);
        PaintBlue.setTextSize(Util.sp2px(context, 12));

        //画笔初始化
        paintYG = new Paint();
        paintYG.setColor(Color.parseColor("#FF7E50"));
        paintYG.setStyle(Paint.Style.FILL);

        paintDH = new Paint();
        paintDH.setColor(Color.parseColor("#DB70D7"));
        paintDH.setStyle(Paint.Style.FILL);

        paintHW = new Paint();
        paintHW.setColor(Color.parseColor("#87CFFB"));
        paintHW.setStyle(Paint.Style.FILL);

        paintUT = new Paint();
        paintUT.setColor(Color.parseColor("#32CD32"));
        paintUT.setStyle(Paint.Style.FILL);

        //抗锯齿
        paintYG.setAntiAlias(true);
        paintDH.setAntiAlias(true);
        paintHW.setAntiAlias(true);
        paintUT.setAntiAlias(true);

        cirX = ScrWidth / 2;
        cirY = ScrHeight / 3;
        float radius = ScrHeight / 5;

        float arcLeft = cirX - radius;
        float arcTop = cirY - radius;
        float arcRight = cirX + radius;
        float arcBottom = cirY + radius;
        RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);

        //位置计算类
        XChartCalc xcalc = new XChartCalc();
        //实际用于计算的半径
        float calcRadius = radius / 2;

        //先画个圆确定下显示位置
        canvas.drawCircle(cirX, cirY, radius, paintUT);

        //填充扇形
        canvas.drawArc(arcRF0, 0, ygCount, true, paintYG);
        //计算并在扇形中心标注上百分比
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, ygCount / 2);
        canvas.drawText(Float.toString(ygCountP) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);

        //填充扇形
        canvas.drawArc(arcRF0, ygCount, dhCount, true, paintDH);
        //计算并在扇形中心标注上百分比
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, ygCount + dhCount / 2);
        canvas.drawText(Float.toString(dhCountP) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);

        //填充扇形
        canvas.drawArc(arcRF0, ygCount + dhCount, hwCount, true, paintHW);
        //计算并在扇形中心标注上百分比
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, ygCount + dhCount + hwCount / 2);
        canvas.drawText(Float.toString(hwCountP) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);

        //计算并在扇形中心标注上百分比
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, ygCount + dhCount + hwCount + utCount / 2);
        canvas.drawText(Float.toString(utCountP) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);

        drawLineOrText(canvas);
    }

    /**
     * 设置底部标注
     *
     * @param canvas
     */
    private void drawLineOrText(Canvas canvas) {
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

        float height4 = ScrHeight / 4;
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
