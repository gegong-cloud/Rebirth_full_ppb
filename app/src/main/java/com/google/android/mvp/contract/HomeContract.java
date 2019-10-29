package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import java.util.List;

import io.reactivex.Observable;


public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setBannerInfo(List<HomeAdv> advList);
        void showDeng(List<ScrollMsg> listDeng);
        void showFloat(HomeAdv homeAdv);
        void showAdapter(List<HomeVipMov> homeVipMovList);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<List<HomeVipMov>>> vipCate();//vip影视区
        Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload);//首页广告
        Observable<CommonEntity<List<ScrollMsg>>> getConfig(HomeAdvUpload homeAdvUpload);//公告
        Observable<CommonEntity<JionLiveEntity>> checkUser();//检查是否是vip
        Observable<CommonEntity<LiveEntity>> liveIndex();//
    }
}
