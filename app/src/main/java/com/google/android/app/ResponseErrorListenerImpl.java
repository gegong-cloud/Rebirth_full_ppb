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
package com.google.android.app;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.ui.activity.StarUpActivity;

import org.json.JSONException;
import org.simple.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link ResponseErrorListener} 的用法
 * <p>
 * ================================================
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {


    @Override
    public void handleResponseError(Context context, Throwable t) {
        Timber.tag("Catch-Error").w(t.getMessage());
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        String msg = "网络不可用";
        if (t instanceof UnknownHostException) {
            msg = "服务器发生错误";
            reloadBaseUrl(context,msg);
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
            reloadBaseUrl(context,msg);
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            reloadBaseUrl(context,"");
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        ArmsUtils.snackbarText(isStar(context)?"正在选择加速通道...":msg);
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

    private void reloadBaseUrl(Context context,String msgStr) {
        if(!isStar(context)){
            ArmsUtils.makeText(context, TextUtils.isEmpty(msgStr)?"服务器开小差了,建议重启APP再试!":msgStr);
            return;
        }
        String defautUrl = "";//判断默认域名是那个
        if (!TextUtils.isEmpty(MyApplication.getsInstance().getImgUrl())) {
            defautUrl = MyApplication.getsInstance().getImgUrl();
        } else {
            defautUrl = TextConstant.APP_DOMAIN;
        }
        String spareStr = !TextUtils.isEmpty(HbCodeUtils.getSpare()) ? HbCodeUtils.getSpare() : "";//备用域名是否被启用过
        int chooseId = 0;
        String nowUrl = "";
        if (TextConstant.APP_SPARE.equals(spareStr)) {//其他域名已用完
            if (HbCodeUtils.getNewUrl() != null && HbCodeUtils.getNewUrl().size() > 0) {
                for (int i = 0; i < HbCodeUtils.getNewUrl().size(); i++) {
                    if (defautUrl.equals(HbCodeUtils.getNewUrl().get(i))) {
                        chooseId = i;
                        break;
                    }
                }
                if (chooseId >= (HbCodeUtils.getNewUrl().size() - 1)) {//新域名已用完
                    nowUrl = TextConstant.APP_SPARE;//备用域名
                    HbCodeUtils.setSpare(TextConstant.APP_SPARE);
                } else {//取备用
                    ++chooseId;
                    nowUrl = HbCodeUtils.getNewUrl().get(chooseId).getUrl();
                }
            } else {
                nowUrl = TextConstant.APP_SPARE;//备用域名
                HbCodeUtils.setSpare(TextConstant.APP_SPARE);
            }

        } else {//用内置域名
            for (int i = 0; i < TextConstant.API_LIST.size(); i++) {
                if (defautUrl.equals(TextConstant.API_LIST.get(i))) {
                    chooseId = i;
                    break;
                }
            }
            if (chooseId >= (TextConstant.API_LIST.size() - 1)) {//内置已用完
                nowUrl = TextConstant.APP_SPARE;//备用域名
                HbCodeUtils.setSpare(TextConstant.APP_SPARE);
            } else {//取内置的
                ++chooseId;
                nowUrl = TextConstant.API_LIST.get(chooseId);
            }
        }
        // 可在 App 运行时,随时切换 BaseUrl (指定了 Domain-Name header 的接口)
        RetrofitUrlManager.getInstance().putDomain("douban", MyApplication.getsInstance().getImgUrl());
        EventBus.getDefault().post(nowUrl, EventBusTags.NET_WORK_URL);
        MyApplication.getsInstance().setImgUrl(HbCodeUtils.checkHttp(nowUrl));
        Timber.e("切换域名为：" + nowUrl);
    }

    boolean isStar(Context context){
        AppComponent appComponent = ArmsUtils.obtainAppComponentFromContext(context);
        AppManager appManager = appComponent.appManager();
        if (appManager != null && appManager.getActivityList() != null && appManager.getActivityList().size() > 0) {
            boolean isMain = true;
            for (Activity activity : appManager.getActivityList()) {
                if (activity instanceof StarUpActivity) {
                    isMain = false;
                    break;
                }
            }
            if (isMain) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
