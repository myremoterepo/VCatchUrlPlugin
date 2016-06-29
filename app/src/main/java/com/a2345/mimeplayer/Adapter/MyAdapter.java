package com.a2345.mimeplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a2345.mimeplayer.R;
import com.a2345.mimeplayer.ValuePool.SourceInfo;

import java.util.List;


/**
 * Created by Administrator on 2015/11/25.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<SourceInfo> infos;
    public MyAdapter(Context context, List<SourceInfo> infos){
        this.infos = infos;
        this.context = context;
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        TextView textView = (TextView)convertView.findViewById(R.id.title);
        textView.setText(infos.get(position).getSourceName());
        return convertView;
    }
}
