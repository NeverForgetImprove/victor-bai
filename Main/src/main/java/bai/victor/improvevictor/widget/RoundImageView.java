package bai.victor.improvevictor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import bai.victor.improvevictor.R;
import bai.victor.utils.LogUtils;

/**
 * Created by Victor-Bai on 2017/7/13.
 */

public class RoundImageView extends View {

    private int type;// 图片类型
    private static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    // 图片
    private Bitmap mSrc;
    // 圆角的大小
    private int mRadius;
    // 控件的宽高
    private int mWidth, mHeight;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0);
        // 默认为圆角
        type = a.getInteger(R.styleable.RoundImageView_type, 1);
        // 圆角半径默认为 10dp
        mRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics()));
//        BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(R.styleable.RoundImageView_src);
//        if (drawable != null){
//            mSrc = drawable.getBitmap();
//        }else {
//            mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bg);
//        }
        mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.RoundImageView_src,0));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){// match_parent ,  accurate
            mWidth = specSize;
        }else {
            int desireWidByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST){
                mWidth = Math.min(desireWidByImg, specSize);
            }else {
                mWidth = desireWidByImg;
            }
        }
        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else {
            int desireHeiByImg = getPaddingLeft() + getPaddingRight() + mSrc.getHeight();
            if (specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(desireHeiByImg, specSize);
            }else {
                mHeight = desireHeiByImg;
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (type){
            case TYPE_CIRCLE:// 圆形
                int min = Math.min(mWidth, mHeight);
                // 长度如果不一致，按小的值进行压缩
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);

                canvas.drawBitmap(createCircleImage(mSrc, min), (mWidth - min) / 2, (mHeight - min) / 2, null);
                break;
            case TYPE_ROUND:
                canvas.drawBitmap(createRoundCornerImage(mSrc), 0, 0, null);
                break;
        }
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        // 产生一个同样大小的画布
        Canvas canvas = new Canvas(target);
        // 首先绘制圆形
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        // 使用 SRC_IN ， 绘制重叠区域
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundCornerImage(Bitmap source){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
