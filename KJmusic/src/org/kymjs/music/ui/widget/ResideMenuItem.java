package org.kymjs.music.ui.widget;

import org.kymjs.music.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @User: special Date: 13-12-10 
 * @Time: 下午11:05 
 * @Mail: specialcyci@gmail.com
 */

/**
 * @kymjs: 本类是对菜单的每一个item设置
 */
public class ResideMenuItem extends LinearLayout {

    /** 菜单项图标 */
    private ImageView iv_icon;
    /** 菜单项标题 */
    private TextView tv_title;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    /**
     * 初始化菜单项
     * @param context 上下文对象
     * @param icon 菜单图标
     * @param title 菜单内容
     */
    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * 设置菜单项图标
     */
    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    /**
     * 资源设置菜单项标题
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * 用字符串设置菜单项标题
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
