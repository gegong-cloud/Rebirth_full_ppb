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

import android.content.Context;
import android.text.TextUtils;

import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.app.utils.SystemUtil;
import com.google.android.app.utils.sv.DeviceInformationHelper;
import com.jess.arms.http.GlobalHttpHandler;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 展示 {@link GlobalHttpHandler} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalHttpHandlerImpl implements GlobalHttpHandler {
    private Context context;

    public GlobalHttpHandlerImpl(Context context) {
        this.context = context;
    }

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link Interceptor.Chain}
     * @param response   {@link Response}
     * @return
     */
    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
        MediaType mediaType = response.body().contentType();
        return response;
    }

    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link Interceptor.Chain}
     * @param request {@link Request}
     * @return
     */
    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
        /* 如果需要再请求服务器之前做一些操作, 则重新返回一个做过操作的的 Request 如增加 Header, 不做操作则直接返回参数 request
        return chain.request().newBuilder().header("token", tokenId)
                              .build(); */
        String uuid = HbCodeUtils.getAndroidDevice();
        if (TextUtils.isEmpty(uuid)) {
            uuid = DeviceInformationHelper.imei2(context);
            HbCodeUtils.setAndroidDevice(uuid);
        }
        Request request1 = chain.request().newBuilder().
                header("token", HbCodeUtils.getToken())//token
                .header("idfa", StringUtils.getUniquePsuedoID())//token
                .header("seq", TextUtils.isEmpty(uuid) ? DeviceInformationHelper.imei2(context) : uuid).//根据设备信息生成的唯一标识，相同设备重新安装标识不变。通过header上传数据
                header("version", "a_" + StringUtils.getLocalVersionName(context)).//版本号的组成 该app特定字符+版本编号，比如 a_1.1
                header("deviceos", SystemUtil.uploadPlatform())//设备信息(操作系统版本)。 通过header上传数据
                .build();
        return request1;
    }
}
