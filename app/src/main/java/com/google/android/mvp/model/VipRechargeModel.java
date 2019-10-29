package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import com.google.android.mvp.contract.VipRechargeContract;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HornEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.TokenEntity;
import io.reactivex.Observable;


@ActivityScope
public class VipRechargeModel extends BaseModel implements VipRechargeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VipRechargeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<MoneyShow>>> chargeInterfaceList() {
        return mRepositoryManager.obtainRetrofitService(PayService.class).chargeInterfaceList();
    }

    @Override
    public Observable<CommonEntity<List<MoneyShow>>> chargeIndex(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).chargeIndex(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<PayMoneyResult>> recharge(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).recharge(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<PaySuccessResult>> rechargeQuery(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).rechargeQuery(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<UserEntity>> cardRecharge(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).cardRecharge(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<List<HornEntity>>> broadcast() {
        return mRepositoryManager.obtainRetrofitService(PayService.class).broadcast();
    }
}