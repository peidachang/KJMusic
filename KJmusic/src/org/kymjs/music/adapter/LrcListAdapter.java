package org.kymjs.music.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.kymjs.music.R;
import org.kymjs.music.bean.Music;
import org.kymjs.music.utils.ListData;
import org.kymjs.music.utils.Player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 歌词界面播放列表适配器
 * 
 * @author kymjs
 */
public class LrcListAdapter extends BaseAdapter {

    private Context context;
    private List<Music> datas;
    private String imgUrl;

    public LrcListAdapter(Context context) {
        super();
        this.context = context;
        this.datas = Player.getPlayer().getList();
        if (datas == null) {
            datas = ListData.getCollectList(context);
            if (datas == null) {
                datas = new ArrayList<Music>();
            }
        }
    }

    public void refreshLrcAdapter() {
        this.datas = Player.getPlayer().getList();
        if (datas == null) {
            datas = new ArrayList<Music>();
        }
        notifyDataSetChanged();
    }

    public void refreshLrcAdapter(String url) {
        this.imgUrl = url;
        this.refreshLrcAdapter();
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
        ImageView img;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(context, R.layout.list_item_lrclist, null);
            holder = new ViewHolder();
            holder.img = (ImageView) v.findViewById(R.id.list_item_img);
            holder.tv_title = (TextView) v.findViewById(R.id.list_item_title);
            holder.tv_artist = (TextView) v.findViewById(R.id.list_item_artist);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.tv_title.setText(datas.get(position).getTitle());
        holder.tv_artist.setText(datas.get(position).getArtist());
        if (Player.getPlayer().getMusic().getId() == datas.get(position)
                .getId()) {
            if (imgUrl != null) {
                holder.img.setVisibility(View.VISIBLE);
                FinalBitmap fb = FinalBitmap.create(context);
                fb.display(holder.img, imgUrl);
            } else {
                holder.img.setImageResource(R.drawable.img_playing);
                holder.img.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img.setVisibility(View.GONE);
        }
        return v;
    }

}
