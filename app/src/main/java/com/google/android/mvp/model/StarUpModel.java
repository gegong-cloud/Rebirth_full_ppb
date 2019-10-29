package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.StarUpContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.api.service.LoginService;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.LoginUpload;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


@ActivityScope
public class StarUpModel extends BaseModel implements StarUpContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public StarUpModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<UserEntity>> userInfo(LoginUpload loginUpload) {
        return mRepositoryManager.obtainRetrofitService(MineService.class).userInfo(loginUpload);
    }

    @Override
    public Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getAdvList(homeAdvUpload);
    }
    @Override
    public Observable<ResponseBody> getImgFile(String fileName) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).getImg(fileName);
    }

    @Override
    public Observable<CommonEntity<List<BaseUrl>>> getUrl() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getUrl();
    }
}