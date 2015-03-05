package com.child.manage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.entity.NoticeNews;
import com.child.manage.ui.Constants;
import com.child.manage.util.InternetURL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * author: liuzwei
 * Date: 2014/7/31
 * Time: 14:19
 * 类的功能、说明写在此处.
 */
public class KechengAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<NoticeNews> list;
    private Context context;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public KechengAdapter(List<NoticeNews> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.gonggao_item, null);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.cont = (TextView) convertView.findViewById(R.id.cont);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NoticeNews cell = list.get(position);
        holder.datetime.setText(cell.getTime());
        holder.title.setText(cell.getTitle());
        holder.cont.setText(cell.getSummary());
        imageLoader.displayImage(String.format(Constants.API_HEAD + "%s",cell.getPic()),
                holder.pic, ChildApplication.tpOptions, animateFirstListener);
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        TextView cont;
        TextView title;
        TextView datetime;
    }

}
