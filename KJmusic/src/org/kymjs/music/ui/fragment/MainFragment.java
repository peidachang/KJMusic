package org.kymjs.music.ui.fragment;

import org.kymjs.music.Config;
import org.kymjs.music.R;
import org.kymjs.music.adapter.AbsPlayListAdapter;
import org.kymjs.music.adapter.CollectListAdapter;
import org.kymjs.music.adapter.FMPagerAdapter;
import org.kymjs.music.adapter.MyMusicAdapter;
import org.kymjs.music.ui.FMActivity;
import org.kymjs.music.ui.Main;
import org.kymjs.music.ui.widget.JSViewPager;
import org.kymjs.music.ui.widget.ResideMenu;
import org.kymjs.music.utils.ListData;
import org.kymjs.music.utils.UIHelper;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 播放列表界面 fragment
 * 
 * @author kymjs
 */
public class MainFragment extends BaseFragment {
    // 界面控件
    private JSViewPager jsViewPager;
    private TextView mTextTab1, mTextTab2, mTextTab3;
    // 顶部图片
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView mImgLine;
    // 侧滑界面控件
    private ResideMenu resideMenu;
    // listView刷新广播接收器
    private RefreshAdapterReceiver receiver = new RefreshAdapterReceiver();

    // ViewPager中的控件
    private AbsPlayListAdapter myMusicAdp, collectAdp; // 主界面中ListView适配器
    private ListView mMyMusicList, mCollectList; // Pager中的List

    @Override
    public View setView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle) {
        View view = inflater.inflate(R.layout.frag_main, container, false);
        return view;
    }

    @Override
    public void initWidget(View parentView) {
        mTextTab1 = (TextView) parentView.findViewById(R.id.collect_title1);
        mTextTab2 = (TextView) parentView.findViewById(R.id.collect_title2);
        mTextTab3 = (TextView) parentView.findViewById(R.id.collect_title3);
        mTextTab1.setText("本地音乐");
        mTextTab2.setText("我的收藏");
        mTextTab3.setText("音乐电台");
        mTextTab1.setOnClickListener(this);
        mTextTab2.setOnClickListener(this);
        mTextTab3.setOnClickListener(this);

        initViewPager(parentView);
        initImageLine(parentView, 3);
        resideMenu = ((Main) getActivity()).getResideMenu();
    }

    @Override
    public void widgetClick(View v) {
        if (v == mTextTab1) {
            jsViewPager.setCurrentItem(0);
        } else if (v == mTextTab2) {
            jsViewPager.setCurrentItem(1);
        } else if (v == mTextTab3) {
            jsViewPager.setCurrentItem(2);
        }
    }

    /**
     * 初始化主界面ViewPager
     */
    private void initViewPager(View parentView) {
        jsViewPager = (JSViewPager) parentView.findViewById(R.id.main_pager);
        jsViewPager.setAdapter(new MainPagerAdapter(3));
        jsViewPager.setCurrentItem(0);
        jsViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
                Animation animation = new TranslateAnimation(one * currIndex,
                        one * arg0, 0, 0);
                currIndex = arg0;
                animation.setFillAfter(true);// 图片停在动画结束位置
                animation.setDuration(300);
                mImgLine.startAnimation(animation);
                resideLogic(arg0);
                refCollectLogic(arg0);
            }
        });
    }

    /**
     * 初始化滚动线并设置滚动线的宽度
     * 
     * @param pagers
     *            viewpager的页面数
     */
    private void initImageLine(View parentView, int pagers) {
        mImgLine = (ImageView) parentView.findViewById(R.id.collect_cursor);
        // 从资源获取一个bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_collect_line);
        // 获取设备屏幕宽度
        WindowManager window = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);
        int screenW = window.getDefaultDisplay().getWidth();

        Matrix matrix = new Matrix();
        // 将宽度按(目标宽度/原宽度)放大，高度没有改变，则比例为1
        matrix.postScale((screenW / pagers) / bitmap.getWidth(), 1);
        // 得到放大的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        // 得到放大后的图片宽度
        bmpW = bitmap.getWidth();
        // 计算偏移量
        offset = (screenW / pagers - bmpW);
        // matrix.postTranslate(offset, 0);//默认显示位置
        // mImgLine.setImageMatrix(matrix);// 设置动画初始位置
        // 设置ImageView的图片
        mImgLine.setImageBitmap(bitmap);
        bitmap = null;
    }

    /**
     * 侧滑策略
     */
    private void resideLogic(int arg0) {
        if (arg0 == 0) {
            resideMenu.removeIgnoredView(jsViewPager);
        } else {
            resideMenu.addIgnoredView(jsViewPager);
        }
    }

    /**
     * 刷新收藏策略
     */
    private void refCollectLogic(int arg0) {
        if (arg0 == 1 && collectAdp != null && Config.changeCollectInfo) {
            collectAdp.refresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.RECEIVER_UPDATE_MUSIC_LIST);
        filter.addAction(Config.RECEIVER_MUSIC_SCAN_FAIL);
        getActivity().registerReceiver(receiver, filter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 刷新Adapter的广播
     * 
     * @author kymjs
     */
    public class RefreshAdapterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Config.RECEIVER_UPDATE_MUSIC_LIST.equalsIgnoreCase(intent
                    .getAction())) {
                // 我不知道为什么，调用刷新方法却没有刷新控件，好像必须销毁ListView再重新创建才能有刷新作用
                // if (collectAdp != null) {
                // collectAdp.refresh();
                // mCollectList.invalidate();
                // }
                // if (myMusicAdp != null) {
                // myMusicAdp.refresh();
                // mMyMusicList.invalidate();
                // }
                myMusicAdp = new MyMusicAdapter(getActivity(), 0);
                mMyMusicList.setAdapter(myMusicAdp);
                collectAdp = new CollectListAdapter(getActivity(), 1);
                mCollectList.setAdapter(collectAdp);
                ((Main) getActivity()).lyricFragment.refreshLrcView();
            } else {
                UIHelper.toast("呀，扫描失败了，退出再进试试？");
            }
        }
    }

    /***************************************************************************************
     * 
     * 主界面ViewPager控件的适配器
     * 
     ***************************************************************************************/
    class MainPagerAdapter extends PagerAdapter implements OnItemClickListener {
        private int pagers;

        public MainPagerAdapter(int pagers) {
            super();
            this.pagers = pagers;
        }

        @Override
        public int getCount() {
            return pagers;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View mainPagerView = getPagerView(position);
            if (mainPagerView == null) {
                mainPagerView = (ListView) View.inflate(getActivity(),
                        R.layout.pager_item_main, null);
            }
            (container).addView(mainPagerView);
            return mainPagerView;
        }

        // 对不同的界面设置不同的适配器
        private View getPagerView(int pager) {
            View pagerView = null;
            switch (pager) {
            case 0:
                mMyMusicList = (ListView) View.inflate(getActivity(),
                        R.layout.pager_item_main, null);
                myMusicAdp = new MyMusicAdapter(getActivity(), pager);
                mMyMusicList.setAdapter(myMusicAdp);
                mMyMusicList.setOnItemClickListener(this);
                pagerView = mMyMusicList;
                break;
            case 1:
                mCollectList = (ListView) View.inflate(getActivity(),
                        R.layout.pager_item_main, null);
                collectAdp = new CollectListAdapter(getActivity(), pager);
                mCollectList.setAdapter(collectAdp);
                mCollectList.setOnItemClickListener(this);
                pagerView = mCollectList;
                break;
            case 2:
                View fmView = View.inflate(getActivity(),
                        R.layout.pager_item_main_fm, null);
                ImageView fmImg = (ImageView) fmView
                        .findViewById(R.id.pager_img_fm);
                fmImg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // startActivity(new Intent(getActivity(),
                        // FMActivity.class));
                        Intent intent = new Intent();
                        try {
                            getActivity().getPackageManager().getPackageInfo(
                                    "com.douban.radio", 0);
                            intent.setComponent(new ComponentName(
                                    "com.douban.radio",
                                    "com.douban.radio.app.WelcomeActivity"));
                        } catch (NameNotFoundException e) {
                            intent.setClass(getActivity(), FMActivity.class);
                            UIHelper.toast("您还未安装豆瓣FM");
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                GridView grid = (GridView) fmView
                        .findViewById(R.id.pager_grid_fm);
                grid.setAdapter(new FMPagerAdapter());
                pagerView = fmView;
                break;
            }
            return pagerView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            switch (((AbsPlayListAdapter) parent.getAdapter())
                    .getCurrentPager()) {
            case 0:
                ((Main) getActivity()).mPlayersService.play(
                        ListData.getLocalList(getActivity()), position);
                break;
            case 1:
                ((Main) getActivity()).mPlayersService.play(
                        ListData.getCollectList(getActivity()), position);
                break;
            }
            ((Main) getActivity()).wantScroll((Main) getActivity());
        }
    }
}
