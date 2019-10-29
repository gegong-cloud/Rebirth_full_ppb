package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.MoneyDetailContract;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyTakeDetail;

import java.util.List;

import io.reactivex.Observable;


@ActivityScope
public class MoneyDetailModel extends BaseModel implements MoneyDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MoneyDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<MoneyTakeDetail>>> accountLog() {
        return mRepositoryManager.obtainRetrofitService(MineService.class).accountLog();
    }
}