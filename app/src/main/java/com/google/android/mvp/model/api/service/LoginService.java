/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.mvp.model.api.service;

import com.google.android.mvp.model.back_entity.AdvEntity;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LoginEntity;
import com.google.android.mvp.model.upload.LoginUpload;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ================================================
 * 支付选择接口
 * <p>
 * ================================================
 */
public interface LoginService {


    //密码登录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/login")
    Observable<CommonEntity<LoginEntity>> login(@Body LoginUpload tokenEntity);

    //找回密码  手机号找回
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/resetpwd")
    Observable<CommonEntity> resetPwd(@Body LoginUpload tokenEntity);

    //置/修改密码
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/editpwd")
    Observable<CommonEntity> editPwd(@Body LoginUpload tokenEntity);


    //用户注册
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/register")
    Observable<CommonEntity<LoginEntity>> register(@Body LoginUpload tokenEntity);


    //获取验证码
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/sms/send")
    Observable<CommonEntity<LoginEntity>> smsSend(@Body LoginUpload tokenEntity);


    //获取启动广告图连接
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/index/startAdv")
    Observable<CommonEntity<AdvEntity>> startAdv(@Body LoginUpload tokenEntity);


    //退出登录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/logout")
    Observable<CommonEntity> logout();

    //下载图片
    @GET()
    @Streaming
    Observable<ResponseBody> getImg(@Url String fileName);


}



