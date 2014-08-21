package org.kymjs.music.ui;

import org.kymjs.music.AppManager;
import org.kymjs.music.Config;
import org.kymjs.music.R;
import org.kymjs.music.service.PlayerService;
import org.kymjs.music.ui.fragment.LyricFragment;
import org.kymjs.music.ui.fragment.MainFragment;
import org.kymjs.music.ui.widget.ResideMenu;
import org.kymjs.music.ui.widget.ResideMenuItem;
import org.kymjs.music.utils.ImageUtils;
import org.kymjs.music.utils.Player;
import org.kymjs.music.utils.UIHelper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 应用程序主Activity
 * 
 * @author kymjs
 */
public class Main extends BaseActivity {

    private ResideMenu resideMenu;
    private ResideMenuItem itemDown;
    private ResideMenuItem itemScan;
    private ResideMenuItem itemTimer;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemQuit;

    /** 音乐播放器服务 */
    public PlayerService mPlayersService;
    private Connection conn = new Connection();
    private MusicChangeReceiver changeReceiver = new MusicChangeReceiver();

    private Button mBtnNext, mBtnPrevious, mBtnPlay;
    private ImageView mImg;
    private TextView mTvTitle, mTvArtist;

    // 歌词界面需要的变量
    public boolean isOpen = false;// content当前是否为显示
    public int screenHeight = 0;// lyric显示的高度
    public FrameLayout.LayoutParams contentParams;// 通过此参数来更改lyric界面的位置。
    public View lyricView;
    public LyricFragment lyricFragment;

    @Override
    public void initWidget() {
        setContentView(R.layout.main_activity);
        setUpMenu();
        handleLrcView(); // 要放在lyricFragment之前调用
        lyricFragment = new LyricFragment();
        changeFragment(new MainFragment(), false);
        changeFragment(R.id.main_layout_lyric, lyricFragment, false);
        initBottonBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceIntent = new Intent(this, PlayerService.class);
        this.bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.RECEIVER_MUSIC_CHANGE);
        filter.addAction(Config.RECEIVER_ERROR);
        registerReceiver(changeReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBottomBar();
    }

    /**
     * 处理歌词界面显示方式
     */
    private void handleLrcView() {
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenHeight = window.getDefaultDisplay().getHeight();
        int width = window.getDefaultDisplay().getWidth();
        contentParams = new FrameLayout.LayoutParams(width,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lyricView = findViewById(R.id.main_aty_lyric);
        contentParams.topMargin = screenHeight;
        lyricView.setLayoutParams(contentParams);
        resideMenu.addIgnoredView(lyricView);
    }

    /**
     * 初始化侧滑菜单界面控件
     */
    private void setUpMenu() {
        // 附加到当前activity
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background_cool);
        resideMenu.attachToActivity(this);
        // 创建菜单项
        itemDown = new ResideMenuItem(this, R.drawable.icon_down, "下载管理");
        itemScan = new ResideMenuItem(this, R.drawable.icon_scan, "扫描音乐");
        itemTimer = new ResideMenuItem(this, R.drawable.icon_timer, "定时音乐");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_setup, "系统设置");
        itemQuit = new ResideMenuItem(this, R.drawable.icon_quit, "下次再来");
        MenuClickListener listener = new MenuClickListener();
        itemDown.setOnClickListener(listener);
        itemScan.setOnClickListener(listener);
        itemTimer.setOnClickListener(listener);
        itemSettings.setOnClickListener(listener);
        itemQuit.setOnClickListener(listener);
        resideMenu.addMenuItem(itemScan);
        resideMenu.addMenuItem(itemDown);
        resideMenu.addMenuItem(itemTimer);
        resideMenu.addMenuItem(itemSettings);
        resideMenu.addMenuItem(itemQuit);
    }

    /**
     * 初始化底部栏
     */
    private void initBottonBar() {
        findViewById(R.id.bottom_bar).setOnClickListener(this);
        mImg = (ImageView) findViewById(R.id.bottom_img_image);
        mImg.setBackgroundResource(R.drawable.img_noplaying);

        mTvTitle = (TextView) findViewById(R.id.bottom_tv_title);
        mTvArtist = (TextView) findViewById(R.id.bottom_tv_artist);
        mTvTitle.setText(Config.TITLE);
        mTvArtist.setText(Config.ARTIST);

        mBtnNext = (Button) findViewById(R.id.bottom_btn_next);
        mBtnPrevious = (Button) findViewById(R.id.bottom_btn_previous);
        mBtnPlay = (Button) findViewById(R.id.bottom_btn_play);
        mBtnNext.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
    }

    /**
     * 刷新底部栏
     */
    private void refreshBottomBar() {
        Player player = Player.getPlayer();
        switch (player.getPlaying()) {
        case Config.PLAYING_PAUSE:
            mImg.setImageResource(R.drawable.img_playing);
            mBtnPlay.setBackgroundResource(R.drawable.selector_btn_play);
            mTvTitle.setText(player.getMusic().getTitle());
            mTvArtist.setText(player.getMusic().getArtist());
            break;
        case Config.PLAYING_PLAY:
            mImg.setImageResource(R.drawable.img_playing);
            mBtnPlay.setBackgroundResource(R.drawable.selector_btn_pause);
            mTvTitle.setText(player.getMusic().getTitle());
            mTvArtist.setText(player.getMusic().getArtist());
            break;
        case Config.PLAYING_STOP:
            mImg.setImageResource(R.drawable.img_noplaying);
            mBtnPlay.setBackgroundResource(R.drawable.selector_btn_play);
            mTvTitle.setText(Config.TITLE);
            mTvArtist.setText(Config.ARTIST);
            break;
        }
    }

    @Override
    public void widgetClick(View v) {
        Player player = Player.getPlayer();
        switch (v.getId()) {
        case R.id.bottom_bar:
            wantScroll();
            break;
        case R.id.bottom_btn_next:
            mPlayersService.next();
            break;
        case R.id.bottom_btn_previous:
            mPlayersService.previous();
            break;
        case R.id.bottom_btn_play:
            if (player.getPlaying() == Config.PLAYING_PLAY) {
                mPlayersService.pause();
            } else if (player.getPlaying() == Config.PLAYING_PAUSE) {
                mPlayersService.replay();
            } else {
                mPlayersService.play();
            }
            v.setBackgroundResource(ImageUtils.getBtnMusicPlayBg());
            break;
        }
    }

    /**
     * 菜单页点击事件监听器
     */
    class MenuClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == itemDown) {
            } else if (v == itemScan) {
                // startService(new Intent(Main.this, ScanMusic.class));
                startActivity(new Intent(Main.this, FirstInstallActivity.class));
            } else if (v == itemTimer) {
            } else if (v == itemSettings) {
            } else if (v == itemQuit) {
                AppManager.getAppManager().AppExit(Main.this);
            }
            resideMenu.closeMenu();
        }
    }

    /**
     * 改变界面的fragment
     */
    private void changeFragment(Fragment targetFragment, boolean pushStack) {
        changeFragment(R.id.main_fragment, targetFragment, pushStack);
    }

    /**
     * 改变界面的fragment
     */
    private void changeFragment(int resView, Fragment targetFragment,
            boolean pushStack) {
        resideMenu.clearIgnoredViewList();// 清空不拦截触摸事件的控件（界面已经被替换）
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        // 使用传入的fragment替换主界面的fragment
        transaction.replace(resView, targetFragment, "fragment");
        // 设置动画样式
        transaction
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (pushStack) {
            // 添加到返回栈（使用户按下返回键时可以返回上一个界面）
            transaction.addToBackStack(null);
        }
        // 提交
        transaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.onInterceptTouchEvent(ev)
                || super.dispatchTouchEvent(ev);
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(changeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
    }

    /**
     * ServiceConnection实现类
     */
    class Connection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            UIHelper.toast("呀，音乐播放失败，退出再进试试");
            mPlayersService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayersService = ((PlayerService.LocalPlayer) service)
                    .getService();
        }
    }

    /**
     * BroadcastReceiver类
     */
    public class MusicChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Config.RECEIVER_MUSIC_CHANGE.equals(intent.getAction())) {
                if (Player.getPlayer().getPlaying() != Config.PLAYING_STOP) {
                    refreshBottomBar();
                }
                lyricFragment.refreshLrcView();
            } else if (Config.RECEIVER_ERROR.endsWith(intent.getAction())) {
                UIHelper.toast(intent.getStringExtra("error"));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOpen) {
                wantScroll();
            } else {
                resideMenu.openMenu();
                // moveTaskToBack(false);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            resideMenu.openMenu();
            return true;
        } else {
            return false;
        }
    }

    /************************************************************************************
     * 
     * 抽屉效果策略
     * 
     ************************************************************************************/
    public void wantScroll(Main aty) {
        if (isOpen) {
            aty.scrollToLrc();
        } else {
            aty.scrollToContent();
        }
    }

    private void wantScroll() {
        if (isOpen) {
            scrollToLrc();
        } else {
            scrollToContent();
        }
    }

    private void scrollToLrc() {
        new ScrollTask().execute(15);
        isOpen = false;
    }

    private void scrollToContent() {
        new ScrollTask().execute(-15);
        isOpen = true;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... speed) {
            int topMargin = contentParams.topMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                topMargin += speed[0];
                if (topMargin > screenHeight) {
                    topMargin = screenHeight;
                    break;
                }
                if (topMargin < 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                // 每次循环使线程睡眠，这样肉眼才能够看到滚动动画。
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... left) {
            contentParams.topMargin = left[0];
            lyricView.setLayoutParams(contentParams);
            lyricView.invalidate();
        }

        @Override
        protected void onPostExecute(Integer left) {
            contentParams.topMargin = left;
            lyricView.setLayoutParams(contentParams);
        }
    }
}
