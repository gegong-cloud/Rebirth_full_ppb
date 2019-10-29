package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.PayCodeShowContract;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.upload.TokenEntity;

import io.reactivex.Observable;


@ActivityScope
public class PayCodeShowModel extends BaseModel implements PayCodeShowContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PayCodeShowModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<PaySuccessResult>> rechargeQuery(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).rechargeQuery(tokenEntity);
    }
}