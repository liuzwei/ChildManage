package com.child.manage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.child.manage.R;
import com.child.manage.entity.MyNodeinfo;

import java.util.LinkedList;


@SuppressWarnings("rawtypes")
public class TreeViewAdapter extends ArrayAdapter {

	private LayoutInflater mInflater;
	private LinkedList<MyNodeinfo> mfilelist;
	private Bitmap mIconCollapse;
	private Bitmap mIconExpand;
	private Bitmap mIconDevice;
	private Bitmap mIconCloud;
	private Bitmap mIconClgUnable;
	private Bitmap mIconDevUnable;
	private Bitmap mIconBag;
	
	@SuppressWarnings("unchecked")
	public TreeViewAdapter(Context context, int textViewResourceId,
						   LinkedList objects) {
		super(context, textViewResourceId, objects);
		mInflater = LayoutInflater.from(context);
		mfilelist = objects;
		initIcon(context);
	}

	public void initIcon(Context context) {
		//"+"未展开
		mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_add);
		//"-"合拢
		mIconExpand = BitmapFactory.decodeResource(context.getResources(),R.drawable.btn_lower);
		//摄像头可用
		mIconDevice = BitmapFactory.decodeResource(context.getResources(),R.drawable.listicon_normal);
		//摄像头不可用
		mIconDevUnable = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.listicon_disable);
		//钱包
		mIconBag = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.list_bag);
		mIconCloud = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.cloud);
		mIconClgUnable = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.btn_ban);
	}

	public int getCount() {
		return mfilelist.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		holder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.tree_node, null);
		try {
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.iconDev = (ImageView) convertView.findViewById(R.id.iconDev);
			holder.iconCloud = (ImageView) convertView.findViewById(R.id.iconCloud);
			convertView.setTag(holder);
				
			int level = mfilelist.get(position).getnLevel();
			holder.iconDev.setPadding(25*(level), holder.iconDev
					.getPaddingTop(), 0, holder.iconDev.getPaddingBottom());
			
			holder.text.setText(mfilelist.get(position).getsNodeName());
			
			//是节点
			if(!mfilelist.get(position).isbSxtNode()){
				//展开
				if(mfilelist.get(position).isbExpanded()){
					if (mfilelist.get(position).isbAlive()) {
						holder.iconDev.setImageBitmap(mIconExpand);
					} else {
						holder.iconDev.setImageBitmap(mIconClgUnable);
					}
				}else{
				//合拢	
					if (mfilelist.get(position).isbAlive()) {
						holder.iconDev.setImageBitmap(mIconCollapse);
					} else {
						holder.iconDev.setImageBitmap(mIconClgUnable);
					}
				}
			}else{
			//是摄像头
				if (mfilelist.get(position).isbAlive()) {
					holder.iconDev.setImageBitmap(mIconDevice);
				} else {
					holder.iconDev.setImageBitmap(mIconDevUnable);
				}
				if(mfilelist.get(position).isbAtTerm()){
					holder.iconDev.setImageBitmap(mIconBag);
				}
				if (mfilelist.get(position).isbCloudDev()) {
					holder.iconCloud.setImageBitmap(mIconCloud);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		TextView text;
		ImageView iconDev;
		ImageView iconCloud;
	}
}
