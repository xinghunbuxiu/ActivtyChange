package com.demon.activitychange.adapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.demon.activitychange.adapter.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;
    public CommonAdapter(Context context, int layoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
    }
    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }
    public void setmDatas( List<T> datasatas) {
        mDatas = datasatas;
    }
    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position),position);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
    public void convert(ViewHolder holder,T t,int position){
        convert(holder,t);
    }

}
