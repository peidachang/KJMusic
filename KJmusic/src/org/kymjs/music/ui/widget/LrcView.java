package org.kymjs.music.ui.widget;

import java.util.List;

import org.kymjs.music.Config;
import org.kymjs.music.utils.DensityUtils;
import org.kymjs.music.utils.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LrcView extends View implements ILrcView {

    /** normal display mode */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /** seek display mode */
    public final static int DISPLAY_MODE_SEEK = 1;
    /** scale display mode ,scale font size */
    public final static int DISPLAY_MODE_SCALE = 2;

    private List<LrcRow> mLrcRows;
    private int mMinSeekFiredOffset = 10; // min offset for fire seek action,
                                          // px;
    private int mHignlightRow = 0; // current singing row , should be
                                   // highlighted.
    private int mHignlightRowColor = Color.YELLOW;
    private int mNormalRowColor = Color.WHITE;
    private int mSeekLineColor = Color.CYAN;
    private int mSeekLineTextColor = Color.CYAN;
    private int mSeekLineTextSize = DensityUtils.sp2px(getContext(), 10);
    private int mMinSeekLineTextSize = DensityUtils.sp2px(getContext(), 8);
    private int mMaxSeekLineTextSize = DensityUtils.sp2px(getContext(), 13);
    // font size of lrc
    private int mLrcFontSize = DensityUtils.sp2px(getContext(), 17);
    private int mMinLrcFontSize = DensityUtils.sp2px(getContext(), 10);
    private int mMaxLrcFontSize = DensityUtils.sp2px(getContext(), 30);
    private int mPaddingY = 10; // padding of each row
    private int mSeekLinePaddingX = 0; // Seek line padding x
    private int mDisplayMode = DISPLAY_MODE_NORMAL;
    private LrcViewListener mLrcViewListener;

    private String mLoadingLrcTip = Config.LRC_TEXT;

    private float mLastMotionY;
    private PointF mPointerOneLastMotion = new PointF();
    private PointF mPointerTwoLastMotion = new PointF();
    private boolean mIsFirstMove = false;

    private String str;

    private Paint mPaint;

    public LrcView(Context context) {
        super(context);
    }

    public LrcView(Context context, AttributeSet attr) {
        super(context, attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final int height = getHeight(); // height of this view
        final int width = getWidth(); // width of this view

        // default display
        if (mLrcRows == null || mLrcRows.size() == 0) {
            if (mLoadingLrcTip != null) {
                // draw tip when no lrc.
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2
                        - mLrcFontSize, mPaint);
            }
            return;
        }

        int rowY = 0;
        final int rowX = width / 2;
        int rowNum = 0;

        // 高亮度
        String highlightText = mLrcRows.get(mHignlightRow).content;
        str = mLrcRows.get(mHignlightRow).strTime;
        int highlightRowY = height / 2 - mLrcFontSize;
        mPaint.setColor(mHignlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        canvas.drawText(highlightText, rowX, highlightRowY, mPaint);

        // 显示时间还有线
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // 当前时间移动时，显示歌词线
            mPaint.setColor(mSeekLineColor);
            canvas.drawLine(mSeekLinePaddingX, highlightRowY, width
                    - mSeekLinePaddingX, highlightRowY, mPaint);

            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Align.LEFT);
            canvas.drawText(mLrcRows.get(mHignlightRow).strTime, 0,
                    highlightRowY, mPaint);
        }

        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        rowNum = mHignlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        while (rowY > -mLrcFontSize && rowNum >= 0) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum--;
        }

        rowNum = mHignlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;
        while (rowY < height && rowNum < mLrcRows.size()) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum++;
        }
    }

    public void seekLrc(int position) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHignlightRow = position;
        invalidate();
        if (mLrcViewListener != null) {
            mLrcViewListener.onLrcSeeked(position, lrcRow);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mLrcRows == null || mLrcRows.size() == 0) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {

        case MotionEvent.ACTION_DOWN:
            mLastMotionY = event.getY();
            mIsFirstMove = true;
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:

            if (event.getPointerCount() == 2) {
                doScale(event);
                return true;
            }
            // single pointer mode ,seek
            if (mDisplayMode == DISPLAY_MODE_SCALE) {
                // if scaling but pointer become not two ,do nothing.
                return true;
            }

            doSeek(event);
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (mDisplayMode == DISPLAY_MODE_SEEK) {
                seekLrc(mHignlightRow);

                // 指定播放的位置（以毫秒为单位）
                Player.getPlayer().seekTo(timeConvert(str));
            }
            mDisplayMode = DISPLAY_MODE_NORMAL;
            invalidate();
            break;
        }
        return true;
    }

    private static int timeConvert(String timeString) {
        timeString = timeString.replace('.', ':');
        String[] times = timeString.split(":");
        // mm:ss:SS
        return Integer.valueOf(times[0]) * 60 * 1000
                + Integer.valueOf(times[1]) * 1000 + Integer.valueOf(times[2]);
    }

    public void setListener(LrcViewListener l) {
        mLrcViewListener = l;
    }

    // 对外暴漏默认显示的文本
    public void setLoadingTipText(String text) {
        mLoadingLrcTip = text;
    }

    public String getLoadingTipText(){
        return mLoadingLrcTip;
    }
    
    private void doScale(MotionEvent event) {

        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // if Seeking but pointer become two, become to scale mode
            mDisplayMode = DISPLAY_MODE_SCALE;
            return;
        }
        // two pointer mode , scale font
        if (mIsFirstMove) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            mIsFirstMove = false;
            setTwoPointerLocation(event);
        }
        int scaleSize = getScale(event);
        if (scaleSize != 0) {
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);
    }

    private void doSeek(MotionEvent event) {
        float y = event.getY();
        float offsetY = y - mLastMotionY; // touch offset.
        if (Math.abs(offsetY) < mMinSeekFiredOffset) {
            // move to short ,do not fire seek action
            return;
        }
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY / mLrcFontSize); // highlight row
                                                                // offset.
        if (offsetY < 0) {
            // finger move up
            mHignlightRow += rowOffset;
        } else if (offsetY > 0) {
            // finger move down
            mHignlightRow -= rowOffset;
        }
        mHignlightRow = Math.max(0, mHignlightRow);
        mHignlightRow = Math.min(mHignlightRow, mLrcRows.size() - 1);

        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    private void setTwoPointerLocation(MotionEvent event) {
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }

    private void setNewFontSize(int scaleSize) {
        mLrcFontSize += scaleSize;
        mSeekLineTextSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);
    }

    // get font scale offset
    private int getScale(MotionEvent event) {
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);
        float maxOffset = 0; // max offset between x or y axis,used to decide
                             // scale size

        boolean zoomin = false;

        float oldXOffset = Math.abs(mPointerOneLastMotion.x
                - mPointerTwoLastMotion.x);
        float newXoffset = Math.abs(x1 - x0);

        float oldYOffset = Math.abs(mPointerOneLastMotion.y
                - mPointerTwoLastMotion.y);
        float newYoffset = Math.abs(y1 - y0);

        maxOffset = Math.max(Math.abs(newXoffset - oldXOffset),
                Math.abs(newYoffset - oldYOffset));
        if (maxOffset == Math.abs(newXoffset - oldXOffset)) {
            zoomin = newXoffset > oldXOffset ? true : false;
        } else {
            zoomin = newYoffset > oldYOffset ? true : false;
        }

        if (zoomin)
            return (int) (maxOffset / 10);
        else
            return -(int) (maxOffset / 10);
    }

    // 设置歌词行数
    public void setLrc(List<LrcRow> lrcRows) {
        mLrcRows = lrcRows;
        invalidate();
    }

    // 设置正在播放的时间段
    public void seekLrcToTime(long time) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }

        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            // touching
            return;
        }

        // find row
        for (int i = 0; i < mLrcRows.size(); i++) {
            LrcRow current = mLrcRows.get(i);
            LrcRow next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            if ((time >= current.time && next != null && time < next.time)
                    || (time > current.time && next == null)) {
                seekLrc(i);
                return;
            }
        }
    }
}
