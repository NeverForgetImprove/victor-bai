package bai.victor.utils;

import android.graphics.PointF;

import static android.R.attr.x;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class MathUtils {

    /**
     * 根据传入的两点得到斜率
     *
     * @param p1 PointF
     * @param p2 PointF
     * @return 返回斜率
     */
    public static Float getLineSlope(PointF p1, PointF p2){
        if (p2.x - p1.x == 0)return null;
        return (p2.y - p1.y) / (p2.x - p1.x);
    }

    /**
     * 获取斜率为linek的直线与指定圆心、半径的圆的切点
     *
     * @param pMiddle 圆心
     * @param radius 半径
     * @param lineK 斜率
     * @return 切点
     */
    public static PointF[] getIntersectionPoints(PointF pMiddle, float radius, Float lineK){
        PointF[] points = new PointF[2];

        float radian, xOffset = 0, yOffset = 0;
        if (lineK != null){
            radian = (float) Math.atan(lineK);
            xOffset = (float) (Math.sin(radian) * radius);
            yOffset = (float) (Math.cos(radian) * radius);
        }else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
        points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);
        return points;
    }

    /**
     * 获得两点连线的中点
     *
     * @param p1 PointF
     * @param p2 PointF
     * @return 中点
     */
    public static PointF getMiddlePoint(PointF p1, PointF p2){
        return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
    }

    /**
     * 获得两点之间的直线距离
     *
     * @param p1 PointF
     * @param p2 PointF
     * @return 两点之间的直线距离
     */
    public static float getTwoPointDistance(PointF p1, PointF p2){
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}
