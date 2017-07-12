package bai.victor.module;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by Victor-Bai on 2017/7/6.
 */

public class SwipeMenuLayout extends LinearLayout{
    public static final int STATE_CLOSED = 0;// 关闭状态
    public static final int STATE_OPEN = 1;// 打开状态
    public static final int STATE_MOVING_LEFT = 2;// 左滑将要打开状态
    public static final int STATE_MOVING_RIGHT = 3;// 右滑将要关闭状态

    public int mCurrentState = 0;
    private OverScroller mScroller;
    private int mScaledTouchSlop;// 滑动阀值
    private int rightId;// 右侧菜单栏控件id
    private View rightMenuView;// 右侧的菜单按钮
    private int menuWidth;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mScaledTouchSlop = configuration.getScaledTouchSlop();
        // 获取右侧菜单id
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.SwipeMenuLayout);
        rightId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_right_id, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (rightId != 0){
            rightMenuView = findViewById(rightId);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menuWidth = rightMenuView.getMeasuredWidth();
        super.onLayout(changed, l, t, r, b);
    }

    private int mDownX , mDownY;
    private int mLastX , mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (mDownX - event.getX());
                int dy = (int) (mDownY - event.getY());
                // if dy > dx no longer slide
                if (Math.abs(dy) > Math.abs(dx)) return false;

                int deltaX = (int) (mLastX - event.getX());
                if (deltaX > 0){
                    // slide to the left
                    mCurrentState = STATE_MOVING_LEFT;
                    if (deltaX>=menuWidth || getScrollX() + deltaX >= menuWidth){
                        scrollTo(menuWidth,0);
                        mCurrentState = STATE_OPEN;
                        break;
                    }
                }else if (deltaX < 0){
                    // slide to the right
                    mCurrentState = STATE_MOVING_RIGHT;
                    if (deltaX + getScrollX() <= 0){
                        scrollTo(0, 0);
                        mCurrentState = STATE_CLOSED;
                        break;
                    }
                }
                scrollBy(deltaX,0);
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurrentState == STATE_MOVING_LEFT){
                    //The left slide to open
                    mScroller.startScroll(getScrollX(), 0, menuWidth - getScrollX(), 0, 300);
                    invalidate();
                }else if (mCurrentState == STATE_MOVING_RIGHT || mCurrentState == STATE_OPEN){
                    // slide to close
                    smoothToCloseMenu();
                }
                // if the slide distance less than touch slop and the menu is closed,right now the item has click event
                int deltx = (int) (mDownX - event.getX());
                return !(Math.abs(deltx) < mScaledTouchSlop && isMenuClosed()) || super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            // 获取当前 x 和 y 的位置
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX, currY);
            postInvalidate();
        }
        if (isMenuOpen()){
            mCurrentState = STATE_OPEN;
        }else if (isMenuClosed()){
            mCurrentState = STATE_CLOSED;
        }
    }

    /**
     * Determine the menu state
     * @return true if the menu is opened
     */
    public boolean isMenuOpen(){
        return getScrollX() >= menuWidth;
    }

    /**
     * Determine the menu state
     * @return true if menu is closed
     */
    public boolean isMenuClosed(){
        return getScrollX() <= 0;
    }

    public void smoothToCloseMenu(){
        mScroller.startScroll(getScrollX(),0, -getScrollX(),0,300);
        invalidate();
    }
}
