package org.kymjs.music.adapter;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.music.R;
import org.kymjs.music.bean.Music;
import org.kymjs.music.utils.DensityUtils;
import org.kymjs.music.utils.ListData;
import org.kymjs.music.utils.UIHelper;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 收藏列表适配器
 * 
 * @author kymjs
 */
public class CollectListAdapter extends AbsPlayListAdapter {

    private Context context;
    private int currentPager;
    private List<Music> datas = null;
    private PopupWindow mPopup;

    public CollectListAdapter(Context context, int current) {
        super(current);
        this.context = context;
        this.currentPager = current;
        datas = ListData.getCollectList(context);
        if (datas == null) {
            datas = new ArrayList<Music>();
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return datas.get(position).getId();
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_artist;
        ImageView img_menu;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int px = DensityUtils.dip2px(context, 14);
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_music, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.list_item_title);
            holder.tv_artist = (TextView) convertView
                    .findViewById(R.id.list_item_artist);
            holder.img_menu = (ImageView) convertView
                    .findViewById(R.id.list_img_button);
            holder.img_menu.setPadding(px, px, px, px);
            // holder.img_menu.setOnClickListener();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(datas.get(position).getTitle());
        holder.tv_artist.setText(datas.get(position).getArtist());
        holder.img_menu.setImageResource(R.drawable.selector_adp_menu);
        holder.img_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup = UIHelper.getUIHelper().getPopupWindow(context,
                        datas.get(position));
                mPopup.showAsDropDown(v);
            }
        });
        return convertView;
    }

    @Override
    public int getCurrentPager() {
        return currentPager;
    }

    @Override
    public void refresh() {
        datas = ListData.getCollectList(context);
        notifyDataSetChanged();
    }

}
