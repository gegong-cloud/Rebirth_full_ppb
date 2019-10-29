package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.CloudListContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import javax.inject.Inject;

import com.google.android.mvp.model.upload.UserDeleteUpload;
import io.reactivex.Observable;


@ActivityScope
public class CloudListModel extends BaseModel implements CloudListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CloudListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<YunboList>> yunboIndex(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).yunboIndex(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<JionLiveEntity>> checkUser() {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).checkUser();
    }

    @Override
    public Observable<CommonEntity> userfavor(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userfavor(userDeleteUpload);
    }

    @Override
    public Observable<CommonEntity> userFavoDel(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userFavoDel(userDeleteUpload);
    }
}