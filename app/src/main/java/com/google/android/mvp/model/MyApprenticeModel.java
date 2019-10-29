package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.MyApprenticeContract;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MyTeam;
import com.google.android.mvp.model.upload.AccountUpload;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class MyApprenticeModel extends BaseModel implements MyApprenticeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MyApprenticeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<MyTeam>>> myTeam(AccountUpload accountUpload) {
        return mRepositoryManager.obtainRetrofitService(MineService.class).myTeam(accountUpload);
    }
}