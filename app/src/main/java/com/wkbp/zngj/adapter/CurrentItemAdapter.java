package com.wkbp.zngj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wkbp.zngj.R;
import com.wkbp.zngj.bean.CurrentInfoBean;

import java.util.List;

/**
 * Created by weilin on 2016/11/4 20:07
 */
public class CurrentItemAdapter extends BaseAdapter {
    private Context context;
    private List<CurrentInfoBean.ListBean> list;

    public CurrentItemAdapter(Context context, List<CurrentInfoBean.ListBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.current_list_item, null);
            holder = new ViewHolder();
           // holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
            holder.item_level = (TextView) convertView.findViewById(R.id.item_level);
            holder.item_state = (TextView) convertView.findViewById(R.id.item_state);
            holder.item_info = (TextView) convertView.findViewById(R.id.item_info);
            holder.item_checkBox = (CheckBox) convertView.findViewById(R.id.item_checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CurrentInfoBean.ListBean bean = list.get(position);
        String str = bean.getAlarmtime();
        String date = str.substring(0, 10);
        String time = str.substring(11, str.length());
        holder.item_checkBox.setChecked(bean.isCheck());
       /* holder.item_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.setCheck(isChecked);
            }
        });*/
        final ViewHolder finalHolder = holder;
        holder.item_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.item_checkBox.isChecked()){
                    bean.setCheck(true);
                }else {
                    bean.setCheck(false);
                }
            }
        });

        //holder.item_number.setText((position + 1) + "");
        holder.item_date.setText(date);
        holder.item_time.setText(time);
        holder.item_level.setText(bean.getLevelname());
        holder.item_state.setText(bean.getStatusname());
        holder.item_info.setText(bean.getAlarminfo());
        return convertView;
    }

    class ViewHolder {
        CheckBox item_checkBox;
        TextView item_number, item_date, item_time, item_level, item_state, item_info;
    }
}
