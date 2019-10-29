package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.ui.adapter.HistoryAdapter;
import com.google.android.mvp.ui.adapter.PlayAdvAdapter;

import java.util.List;

import io.reactivex.Observable;


public interface MinePlayContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showAdapter(PlayAdvAdapter playAdvAdapter);

        void showAdapter(HistoryAdapter historyAdapter);

        void initVedio(YunboMov yunboMov);

        void showJJ();

        void updateFavoUI(boolean isFavo);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload);

        Observable<CommonEntity<YunboMov>> yunboView(HomeAdvUpload homeAdvUpload);

        Observable<CommonEntity<YunboList>> movSimilar(UserDeleteUpload userDeleteUpload);

        Observable<CommonEntity> userfavor(UserDeleteUpload userDeleteUpload);

        Observable<CommonEntity> userFavoDel(UserDeleteUpload userDeleteUpload);
    }
}
