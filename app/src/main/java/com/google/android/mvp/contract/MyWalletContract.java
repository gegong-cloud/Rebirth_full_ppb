package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.google.android.mvp.model.back_entity.AccountDetail;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.WithDrowAppUpload;

import java.util.List;

import io.reactivex.Observable;


public interface MyWalletContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setScrollText(ScrollMsg scrollText);
        void wihtdarw_all(String allMoney);
        void loadAccountInfo(AccountDetail accountDetail);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<AccountDetail>> userIndex();//用户账户明细
        Observable<CommonEntity<List<ScrollMsg>>> getConfig(HomeAdvUpload homeAdvUpload);//公告
        Observable<CommonEntity> withDrowApply( WithDrowAppUpload withDrowAppUpload);

    }
}
