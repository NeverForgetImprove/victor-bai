package bai.victor.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class SwipeRecyclerView extends RecyclerView {
    private SwipeMenuLayout mLastMenuLayout;
    private int mLastTouchPosition;
    private int mScaleToucheSlop;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleToucheSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private int mLastX, mLastY;
    private int mDownX, mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercepted = super.onInterceptTouchEvent(e);
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) e.getX();
                mLastY = (int) e.getY();
                mDownX = (int) e.getX();
                mDownY = (int) e.getY();
                isIntercepted = false;
                // get the child view according to the X & Y value
                View view = findChildViewUnder(mLastX, mLastY);
                if (view == null) return false;
                // get the clicked child view's position
                final int touchPos = getChildAdapterPosition(view);
                if (touchPos != mLastTouchPosition && mLastMenuLayout != null &&
                        mLastMenuLayout.mCurrentState != SwipeMenuLayout.STATE_CLOSED){
                    if (mLastMenuLayout.isMenuOpen()){
                        // if the menu is opened, close it
                        mLastMenuLayout.smoothToCloseMenu();
                    }
                    isIntercepted = true;
                }else {
                    // get the child view according to the click position
                    ViewHolder holder = findViewHolderForAdapterPosition(touchPos);
                    if (holder != null){
                        View childView = holder.itemView;
                        if (childView != null && childView instanceof SwipeMenuLayout){
                            mLastMenuLayout = (SwipeMenuLayout) childView;
                            mLastTouchPosition = touchPos;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int dx = (int) (mDownX - e.getX());
                int dy = (int) (mDownY - e.getY());
                if (Math.abs(dx) > mScaleToucheSlop && Math.abs(dx) > Math.abs(dy) ||
                        ((mLastMenuLayout != null) && mLastMenuLayout.mCurrentState != SwipeMenuLayout.STATE_CLOSED) ){
                    // if offset-X more than offset-Y or the last menu is opened,forbid the RecyclerView's slid
                    return false;
                }
                break;
        }
        return isIntercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                // if have a item that menu is opened, the RecyclerView can not slid
                if (!mLastMenuLayout.isMenuClosed()){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (mLastMenuLayout != null && mLastMenuLayout.isMenuOpen()){
                    mLastMenuLayout.smoothToCloseMenu();
                }
                break;
        }
        return super.onTouchEvent(e);
    }
}
