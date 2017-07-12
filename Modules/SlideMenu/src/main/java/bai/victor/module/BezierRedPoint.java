package bai.victor.module;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import bai.victor.utils.MathUtils;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class BezierRedPoint extends AppCompatTextView {
    public DragView dragView;
    private float mWidth, mHeight;// View的宽和高
    private OnDragStatusListener onDragListener;

    public BezierRedPoint(Context context) {
        this(context,null);
    }

    public BezierRedPoint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierRedPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获得根View
        View rootView = getRootView();
        // 获得触摸位置在全屏所在位置
        float mRawX = event.getRawX();
        float mRawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 请求父View不拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                // 获得当前View在屏幕上的位置
                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);
                if (rootView instanceof ViewGroup){
                    // 初始化拖拽时的View
                    dragView = new DragView(getContext());
                    // 设置固定圆的圆心坐标
                    dragView.setStickyPoint(cLocation[0] + mWidth / 2, cLocation[1] + mHeight / 2, mRawX, mRawY);
                    // 获得缓存的Bitmap，滑动时直接通过drawBitmap绘制出来
                    setDrawingCacheEnabled(true);
                    Bitmap bitmap =getDrawingCache();
                    if (bitmap != null){
                        dragView.setCacheBitmap(bitmap);
                        // 将DragView添加到RootView中，这样就可以全屏滑动了
                        ((ViewGroup) rootView).addView(dragView);
                        setVisibility(INVISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 请求父View不拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                if (dragView != null){
                    // 更新dragView的位置
                    dragView.setDragViewLocation(mRawX, mRawY);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (dragView != null){
                    // 手抬起时判断各种情况
                    dragView.setDragUp();
                }
                break;
        }
        return true;
    }

    public interface OnDragStatusListener{
        void onDrag();

        void onMove();

        void onRestore();

        void onDismiss();
    }

    public void setOnDragListener(OnDragStatusListener onDragListener){
        this.onDragListener = onDragListener;
    }

    /**
     * 拖拽时的椭圆
     */
    public class DragView extends View  {
        private Path mPath;
        private Paint mPaint;
        private Bitmap mCacheBitmap;
        private PointF dragPointF, stickyPointF;// 拖拽圆的圆点和固定圆的圆点
        private PointF controlPoint;// 二阶贝塞尔曲线控制点
        private float dragDistance;// 拖拽的距离
        private float maxDistance = 300;
        private float mWidth, mHeight;// View的宽和高
        private int mState;// 当前红点的状态
        private static final int STATE_INIT = 0;// 默认静止状态
        private static final int STATE_DRAG = 1;// 拖拽状态
        private static final int STATE_MOVE = 2;// 移动状态
        public static final int STATE_DISMISS = 3;// 消失状态
        private int stickyRadius;// 固定圆的半径
        private int defaultRadius = 30;// 固定小圆的默认半径
        private int dragRadius = 40;// 拖拽圆的半径

        private int[] explode_res = {R.mipmap.explode1, R.mipmap.explode2, R.mipmap.explode3, R.mipmap.explode4, R.mipmap.explode5};
        private Bitmap[] bitmaps;

        public DragView(Context context) {
            this(context, null);
        }

        public DragView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }
        private void init(){
            // 初始化path
            mPath = new Path();
            // 初始化paint
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);
            // 初始化拖拽的PointF
            dragPointF = new PointF();
            stickyPointF = new PointF();
            mState = STATE_INIT;
            // 初始化消失动画资源
            bitmaps = new Bitmap[explode_res.length];
            for (int i = 0; i < explode_res.length; i++) {
                bitmaps[i] = BitmapFactory.decodeResource(getResources(),explode_res[i]);
            }
        }

        private int explodeIndex;
        @Override
        protected void onDraw(Canvas canvas) {
            if (isInsideRange() && mState == STATE_DRAG){
                mPaint.setColor(Color.RED);
                // 绘制固定的小圆
                canvas.drawCircle(stickyPointF.x, stickyPointF.y, stickyRadius, mPaint);
                // 首先获得两圆心之间的斜率
                Float linK = MathUtils.getLineSlope(dragPointF, stickyPointF);
                // 然后通过两个圆心和半径、斜率来获得外切线的切点
                PointF[] stickyPoints = MathUtils.getIntersectionPoints(stickyPointF, stickyRadius, linK);
                dragRadius = (int) (Math.min(mWidth, mHeight) / 2);
                PointF[] dragPoints = MathUtils.getIntersectionPoints(dragPointF, dragRadius, linK);
                mPaint.setColor(Color.RED);
                // 二阶贝塞尔曲线的控制点取得两圆心的中点
                controlPoint = MathUtils.getMiddlePoint(dragPointF, stickyPointF);
                // 绘制贝塞尔曲线
                mPath.reset();
                mPath.moveTo(stickyPoints[0].x, stickyPoints[0].y);
                mPath.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(controlPoint.x, controlPoint.y, stickyPoints[1].x, stickyPoints[1].y);
                mPath.lineTo(stickyPoints[0].x, stickyPoints[0].y);
                canvas.drawPath(mPath, mPaint);
            }
            if (mCacheBitmap != null && mState != STATE_DISMISS){
                // 绘制缓存的Bitmap
                canvas.drawBitmap(mCacheBitmap, dragPointF.x - mWidth / 2, dragPointF.y - mHeight / 2, mPaint);
            }
            if (mState == STATE_DISMISS && explodeIndex < explode_res.length){
                // 绘制小红点消失时的爆炸动画
                canvas.drawBitmap(bitmaps[explodeIndex], dragPointF.x - mWidth / 2, dragPointF.y - mHeight / 2, mPaint);
            }
        }

        /**
         *  设置缓存Bitmap
         *
         * @param cacheBitmap 缓存Bitmap
         */
        public void setCacheBitmap(Bitmap cacheBitmap){
            mCacheBitmap = cacheBitmap;
            mWidth = cacheBitmap.getWidth();
            mHeight = cacheBitmap.getHeight();
        }

        /**
         * 设置固定圆的圆心和半径
         *
         * @param stickyX 固定圆的X坐标
         * @param stickyY 固定圆的Y坐标
         * @param touchX
         * @param touchY
         */
        public void setStickyPoint(float stickyX, float stickyY, float touchX, float touchY){
            // 分别设置固定圆和拖拽圆的坐标
            stickyPointF.set(stickyX, stickyY);
            dragPointF.set(touchX, touchY);
            // 通过两个圆点算出圆心距，也就是拖拽的距离
            dragDistance = MathUtils.getTwoPointDistance(dragPointF, stickyPointF);
            if (dragDistance <= maxDistance){
                // 如果拖拽距离小于规定的最大距离，则固定的圆应该越来越小，这样看着才符合实际
                stickyRadius = (int)(defaultRadius - dragDistance / 10) < 10 ? 10 : (int) (defaultRadius - dragDistance / 10);
                mState = STATE_DRAG;
            }else {
                mState = STATE_INIT;
            }
        }

        public void setDragViewLocation(float touchX, float touchY){
            dragPointF.set(touchX, touchY);
            // 随时更改圆心距
            dragDistance = MathUtils.getTwoPointDistance(dragPointF, stickyPointF);
            if (mState == STATE_DRAG){
                if (isInsideRange()){
                    stickyRadius = (int)(defaultRadius - dragDistance / 10) < 10 ? 10: (int)(defaultRadius - dragDistance / 10);
                }else {
                    mState = STATE_MOVE;
                    if (onDragListener != null){
                        onDragListener.onMove();
                    }
                }
            }
            invalidate();
        }

        public void setDragUp() {
            if (mState == STATE_DRAG && isInsideRange()){
                // 拖拽状态且在范围之内
                startResetAnimator();
            }else if (mState == STATE_MOVE){
                if (isInsideRange()){
                    // 在范围之内，需要RESET
                    startResetAnimator();
                }else {
                    // 范围之外，消失动画
                    mState = STATE_DISMISS;
                    startExplodeAnim();
                }
            }
        }

        private boolean isInsideRange(){
            return dragDistance <= maxDistance;
        }

        /**
         * 爆炸动画
         */
        private void startExplodeAnim(){
            ValueAnimator animator = ValueAnimator.ofInt(0, explode_res.length);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    explodeIndex = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onDragListener != null){
                        onDragListener.onDismiss();
                    }
                }
            });
            animator.start();
        }

        /**
         *拖拽红点reset动画
         */
        private void startResetAnimator(){
            if (mState == STATE_DRAG){
                ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
                        new PointF(dragPointF.x, dragPointF.y), new PointF(stickyPointF.x, stickyPointF.y));
                animator.setDuration(500);
                animator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        float f = 0.571429f;
                        return (float) (Math.pow(2, -4 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF curPoint = (PointF) animation.getAnimatedValue();
                        dragPointF.set(curPoint.x, curPoint.y);
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clearDragView();
                        if (onDragListener != null){
                            onDragListener.onDrag();
                        }
                    }
                });
                animator.start();
            }else if (mState == STATE_MOVE){
                // 先拖拽到范围之外，在拖拽回范围之内
                dragPointF.set(stickyPointF.x, stickyPointF.y);
                invalidate();
                clearDragView();
                if (onDragListener!=null){
                    onDragListener.onRestore();
                }
            }
        }

        /**
         * 清除拖拽时的红点，恢复固定的红点
         */
        private void clearDragView(){
            ViewGroup viewGroup = (ViewGroup) getParent();
            viewGroup.removeView(DragView.this);
            BezierRedPoint.this.setVisibility(VISIBLE);
        }
    }
}
