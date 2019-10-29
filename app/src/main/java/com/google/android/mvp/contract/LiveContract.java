package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import java.util.List;

import io.reactivex.Observable;


public interface LiveContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setBannerInfo(List<HomeAdv> adHomeEntity);
        void showAdapter(List<LiveInfo> liveInfoList);
        void setCountTitle(String countTitle);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<LiveEntity>> liveIndex();
        Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload);
    }
}
