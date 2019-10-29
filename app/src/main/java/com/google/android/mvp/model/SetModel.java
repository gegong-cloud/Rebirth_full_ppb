package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.SetContract;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.upload.AccountUpload;

import io.reactivex.Observable;


@ActivityScope
public class SetModel extends BaseModel implements SetContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SetModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity> updateUserBank(AccountUpload accountUpload) {
        return mRepositoryManager.obtainRetrofitService(MineService.class).updateUserBank(accountUpload);
    }
}