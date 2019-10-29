package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HornEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.TokenEntity;
import com.google.android.mvp.ui.adapter.RechargeMoneyAdapter;
import com.google.android.mvp.ui.adapter.VipRechargeTypeAdapter;

import java.util.List;

import io.reactivex.Observable;


public interface VipRechargeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showAdapter(RechargeMoneyAdapter rechargeMoneyAdapter);
//        TagFlowLayout getFlowLayout();
        void showAdapter(VipRechargeTypeAdapter vipRechargeTypeAdapter);
//        void showAdapter1(VipRechargeType1Adapter vipRechargeType1Adapter);
        void regWx(String wxAppID);
        void showTip(boolean isShow);
        void showHorn(List<HornEntity> listData);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<List<MoneyShow>>> chargeInterfaceList();
        Observable<CommonEntity<List<MoneyShow>>> chargeIndex(TokenEntity tokenEntity);
        Observable<CommonEntity<PayMoneyResult>> recharge(TokenEntity tokenEntity);
        Observable<CommonEntity<PaySuccessResult>> rechargeQuery(TokenEntity tokenEntity);
        Observable<CommonEntity<UserEntity>> cardRecharge(TokenEntity tokenEntity);
        Observable<CommonEntity<List<HornEntity>>> broadcast();
    }
}
