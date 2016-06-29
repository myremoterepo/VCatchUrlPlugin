package com.a2345.mimeplayer.type;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.a2345.mimeplayer.R;

/**
 * Created by fanzf on 2016/5/5.
 */
public class TypeListActivity extends FragmentActivity {
    private static final String videoListFragmentTag = "videolistfragment";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        initView();
    }

    private void initView(){
        TypeListFragment videoListFragment = (TypeListFragment) getSupportFragmentManager().findFragmentByTag(videoListFragmentTag);
        if (videoListFragment == null){
            videoListFragment = TypeListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fra_videolist_container, videoListFragment, videoListFragmentTag).commit();
        }
        new TypePresenter(videoListFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
