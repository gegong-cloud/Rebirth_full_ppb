package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.CardPayContract;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.CardPayUpload;
import io.reactivex.Observable;


@ActivityScope
public class CardPayModel extends BaseModel implements CardPayContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CardPayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity> cardPay(CardPayUpload cardPayUpload) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).cardPay(cardPayUpload);
    }
}