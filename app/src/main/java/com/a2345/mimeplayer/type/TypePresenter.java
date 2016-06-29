package com.a2345.mimeplayer.type;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fanzf on 2016/5/5.
 */
public class TypePresenter implements TypeContract.Presenter {
    TypeContract.View mView;
    public TypePresenter(TypeContract.View view){
        mView = view;
        view.setPresenter(this);
    }
    @Override
    public void start() {
        mView.showVideoList(createVideos());
    }
    private List<String> createVideos(){
        return Arrays.asList(
                "long",
                "short",
                "live",
                "straight"
        );
    }

    public interface VideoItemListener{
        void onItemClick(String type);
    }
}
