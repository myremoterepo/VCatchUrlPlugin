package com.a2345.mimeplayer.type;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.a2345.mimeplayer.Adapter.VideoListAdapter;
import com.a2345.mimeplayer.R;

import java.util.List;

/**
 * Created by fanzf on 2016/5/5.
 */
public class TypeListFragment extends Fragment implements TypeContract.View{

    private TypeContract.Presenter mPresenter;

    private ListView mListView;

    private static TypeListFragment sVideoListFragment = new TypeListFragment();

    public static TypeListFragment newInstance(){
        return sVideoListFragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("info", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_videolist, container, false);
        mListView = (ListView)root.findViewById(R.id.fragment_videolist_list);
        Log.e("info", "onCreateView");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("info", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Log.e("info", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(TypeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showVideoList(List list) {
        Log.i("info", "list.." + list);
        mListView.setAdapter(new VideoListAdapter(getActivity(), list, mItemListener));
    }

    private TypePresenter.VideoItemListener mItemListener = new TypePresenter.VideoItemListener() {
        @Override
        public void onItemClick(String type) {
            Toast.makeText(getActivity(), type, Toast.LENGTH_SHORT).show();
        }
    };
}
