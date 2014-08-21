package org.kymjs.music.ui.fragment;

import org.kymjs.music.AppLog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 应用程序Fragment的基类
 * 
 * @author kymjs
 * @version 1.0
 * @created 2013-12-27
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

    public abstract View setView(LayoutInflater inflater, ViewGroup container,
            Bundle bundle);

    public abstract void initWidget(View parentView);

    public abstract void widgetClick(View parentView);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /***************************************************************************
     * 
     * 打印Fragment生命周期
     * 
     ***************************************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        AppLog.state(this.getClass(), "---------onCreateView ");
        View mFragment = setView(inflater, container, savedInstanceState);
        initWidget(mFragment);
        return mFragment;
    }

    @Override
    public void onResume() {
        AppLog.state(this.getClass(), "---------onResume ");
        super.onResume();
    }

    @Override
    public void onPause() {
        AppLog.state(this.getClass(), "---------onPause ");
        super.onPause();
    }

    @Override
    public void onStop() {
        AppLog.state(this.getClass(), "---------onStop ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        AppLog.state(this.getClass(), "---------onCreat ");
        super.onDestroyView();
    }

}
