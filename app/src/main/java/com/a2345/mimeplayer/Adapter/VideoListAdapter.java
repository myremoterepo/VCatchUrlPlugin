package com.a2345.mimeplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a2345.mimeplayer.R;
import com.a2345.mimeplayer.type.TypePresenter;

import java.util.List;


/**
 * Created by Administrator on 2015/11/25.
 */
public class VideoListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private TypePresenter.VideoItemListener mItemListener;
    public VideoListAdapter(Context context, List list, TypePresenter.VideoItemListener itemListener){
        this.list = list;
        this.context = context;
        this.mItemListener = itemListener;
    }
    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        TextView textView = (TextView)convertView.findViewById(R.id.title);
        textView.setText(list.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onItemClick(list.get(position));
            }
        });
        return convertView;
    }
}
