package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.InviteFriendContract;
import com.google.android.mvp.model.api.service.LoginService;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.UserInvite;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


@ActivityScope
public class InviteFriendModel extends BaseModel implements InviteFriendContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public InviteFriendModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<UserInvite>> userInvite() {
        return mRepositoryManager.obtainRetrofitService(MineService.class).userInvite();
    }

    @Override
    public Observable<ResponseBody> getImgFile(String fileName) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).getImg(fileName);
    }
}