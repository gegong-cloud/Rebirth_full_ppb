package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.PayOptionContract;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.upload.TokenEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class PayOptionModel extends BaseModel implements PayOptionContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PayOptionModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<MoneyShow>>> chargeIndex(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).chargeIndex(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<PayMoneyResult>> recharge(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).recharge(tokenEntity);
    }


}