package org.kymjs.music.ui;

import org.kymjs.music.AppLog;
import org.kymjs.music.AppManager;

import net.tsz.afinal.FinalActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 应用程序Activity的基类
 * 
 * @author kymjs
 * @version 1.0
 * @created 2013-11-24
 */
public abstract class BaseActivity extends FinalActivity implements
        OnClickListener {
    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;

    public int activityState;

    // 是否允许全屏
    private boolean mAllowFullScreen = true;

    public abstract void initWidget();

    public abstract void widgetClick(View v);

    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /***************************************************************************
     * 
     * 打印Activity生命周期
     * 
     ***************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.debug(this.getClass() + "---------onCreat ");
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        }
        AppManager.getAppManager().addActivity(this);
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.state(this.getClass(), "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
        AppLog.state(this.getClass(), "---------onResume ");
    }

    @Override
    protected void onStop() {
        super.onResume();
        activityState = ACTIVITY_STOP;
        AppLog.state(this.getClass(), "---------onStop ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
        AppLog.state(this.getClass(), "---------onPause ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLog.state(this.getClass(), "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = ACTIVITY_DESTROY;
        AppLog.state(this.getClass(), "---------onDestroy ");
        AppManager.getAppManager().finishActivity(this);
    }
}
