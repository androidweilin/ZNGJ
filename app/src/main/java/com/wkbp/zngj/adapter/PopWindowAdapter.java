package com.wkbp.zngj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wkbp.zngj.R;

import java.util.List;

/**
 * Created by weilin on 2016/11/5 18:31
 */
public class PopWindowAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    public PopWindowAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.popwindow_item,null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_pop);
        tv.setText(list.get(position));
        return convertView;
    }
}
