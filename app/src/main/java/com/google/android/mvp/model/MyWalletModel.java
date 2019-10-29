package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.MyWalletContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.AccountDetail;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.WithDrowAppUpload;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class MyWalletModel extends BaseModel implements MyWalletContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MyWalletModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<AccountDetail>> userIndex() {
        return mRepositoryManager.obtainRetrofitService(MineService.class).userIndex();
    }

    @Override
    public Observable<CommonEntity<List<ScrollMsg>>> getConfig(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getConfig(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity> withDrowApply(WithDrowAppUpload withDrowAppUpload) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).withDrowApply(withDrowAppUpload);
    }
}