package com.child.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.entity.shipu;
import com.child.manage.entity.yuer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * author: liuzwei
 * Date: 2014/7/31
 * Time: 14:19
 * 类的功能、说明写在此处.
 */
public class YuerAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<yuer> list;
    private Context context;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public YuerAdapter(List<yuer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.yuer_item, null);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.rightbutton = (ImageView) convertView.findViewById(R.id.rightbutton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final yuer cell = list.get(position);
        holder.datetime.setText(cell.getDatetime());
        holder.title.setText(cell.getTitle());
        imageLoader.displayImage(cell.getPic(), holder.pic, ChildApplication.options, animateFirstListener);
        return convertView;
    }


    class ViewHolder {
        ImageView pic;
        TextView title;
        TextView datetime;
        ImageView rightbutton;

    }

}
