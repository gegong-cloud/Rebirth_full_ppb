package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.upload.TokenEntity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public interface VipQrcodeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void complete();
        void showQrImg(String qrPath);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<PaySuccessResult>> rechargeQuery(TokenEntity tokenEntity);
        Observable<CommonEntity> unlockMoney(TokenEntity tokenEntity);
        Observable<ResponseBody> getImgFile(String fileName);//下载文件
    }
}
