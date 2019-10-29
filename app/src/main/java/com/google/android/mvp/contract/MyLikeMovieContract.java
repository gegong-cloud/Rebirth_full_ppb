package com.google.android.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.model.upload.UserViewUpload;
import com.google.android.mvp.ui.adapter.HistoryAdapter;
import io.reactivex.Observable;


public interface MyLikeMovieContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void editShow(boolean isShow);
        void showAdapter(HistoryAdapter historyAdapter);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<YunboList>> userFavo(UserViewUpload userViewUpload);
        Observable<CommonEntity> userFavoDel(UserDeleteUpload userDeleteUpload);
    }
}
