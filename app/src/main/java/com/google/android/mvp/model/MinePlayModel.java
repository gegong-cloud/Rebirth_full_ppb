package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import com.google.android.mvp.contract.MinePlayContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import io.reactivex.Observable;


@ActivityScope
public class MinePlayModel extends BaseModel implements MinePlayContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MinePlayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getAdvList(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<YunboMov>> yunboView(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).yunboView(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<YunboList>> movSimilar(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).movSimilar(userDeleteUpload);
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