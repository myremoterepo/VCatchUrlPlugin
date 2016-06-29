package com.a2345.mimeplayer.type;

import com.a2345.mimeplayer.BasePresenter;
import com.a2345.mimeplayer.BaseView;

import java.util.List;

/**
 * Created by fanzf on 2016/5/5.
 */
public interface TypeContract {

    interface View<Presenter> extends BaseView{
        void showVideoList(List<String> list);
    }

    interface Presenter extends BasePresenter{

    }
}
