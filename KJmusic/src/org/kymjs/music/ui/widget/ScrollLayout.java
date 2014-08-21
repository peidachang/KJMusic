package org.kymjs.music.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {

    private int defaultScreen = 0;
    private int currentScreen;

    private Scroller scroller;// 滑动控制器
    private VelocityTracker velocityTracker;// 用于得到手势在屏幕上的滑动速度
    private float lastMotionX;

    private static final int VELOCITY = 600;

    private OnViewChangeListener onViewChangeListener;

    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScrollLayout(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        // 当前屏幕
        currentScreen = defaultScreen;
        // 滑动控制器
        scroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {

            int childLeft = 0;
            int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    int childWidth = childView.getMeasuredWidth();
                    // 设置View的大小、位置
                    childView.layout(childLeft, 0, childLeft + childWidth,
                            childView.getMeasuredHeight());

                    childLeft += childWidth;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain();// 取得手势在屏幕上的滑动速度
                velocityTracker.addMovement(event);
            }
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }

            lastMotionX = x;

            break;

        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) (lastMotionX - x);
            if (isCanMove(deltaX)) {
                if (velocityTracker != null) {
                    velocityTracker.addMovement(event);
                }

                lastMotionX = x;

                scrollBy(deltaX, 0);
            }

            break;

        case MotionEvent.ACTION_UP:

            int velocityX = 0;
            if (velocityTracker != null) {
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);// 计算每秒滑动多少个像素
                velocityX = (int) velocityTracker.getXVelocity();// 最后计算检索X速度
            }

            if (velocityX > VELOCITY && currentScreen > 0) {
                toScreen(currentScreen - 1);
            } else if (velocityX < -VELOCITY
                    && currentScreen < getChildCount() - 1) {
                toScreen(currentScreen + 1);
            } else {
                toDestination();
            }

            if (velocityTracker != null) {
                velocityTracker.recycle();// 回收
                velocityTracker = null;
            }

            break;
        }

        return true;
    }

    // 滑动到目标页面
    private void toDestination() {
        int screenWidth = getWidth();
        int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;

        toScreen(destScreen);
    }

    // 滑动到指定页面
    private void toScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

        if (getScrollX() != whichScreen * getWidth()) {
            int deltaX = whichScreen * getWidth() - getScrollX();

            scroller.startScroll(getScrollX(), 0, deltaX, 0,
                    Math.abs(deltaX * 2));
            currentScreen = whichScreen;
            invalidate();

            if (onViewChangeListener != null) {
                onViewChangeListener.onViewChange(currentScreen);
            }
        }
    }

    public void setOnViewChangeLintener(OnViewChangeListener lintener) {
        onViewChangeListener = lintener;
    }

    // 能否滑动
    private boolean isCanMove(int deltaX) {
        if (getScrollX() <= 0 && deltaX < 0) {
            return false;
        }
        if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        scrollTo(currentScreen * width, 0);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
    
    public interface OnViewChangeListener {
        public void onViewChange(int view);
    }
}
