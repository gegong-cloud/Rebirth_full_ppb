package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.ChooseAccoutContract;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.model.back_entity.CommonEntity;

import io.reactivex.Observable;


@ActivityScope
public class ChooseAccoutModel extends BaseModel implements ChooseAccoutContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChooseAccoutModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<AccountReceipt>> getUserBank() {
        return mRepositoryManager.obtainRetrofitService(MineService.class).getUserBank();
    }
}