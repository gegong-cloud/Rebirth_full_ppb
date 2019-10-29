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


/**
 * ================================================
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.5">EventBusTags wiki 官方文档</a>
 * Created by JessYan on 8/30/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusTags {
    public static String EXECUTE_TASK = "execute_task";//页码大小
    public static String PLAY_SUCCESS = "play_success";//播放成功
    public static String REG_SUCCESS = "reg_success";//注册成功
    public static String HOME_ITEM_CLICK = "home_item_click";//首页点击影视检查

    public static String ACCOUNT_CHOOSE = "account_choose";//账号选择
    public static String ADD_ACCOUNT = "add_account";//z增加账号
    public static String CARD_PWD_OPEN = "card_pwd_open";//卡密开通
    public static String PASTE_TEXT = "paste_text";//粘贴

    public static String SELECT_PAY_RESULT = "select_pay_result";//查询结果
    public static String CAST_PAY_RESULT = "cast_pay_result";//跳转支付最终结果

    public static String NET_WORK_URL = "net_work_url";//网络切换

    public static String QUIT_FULL = "quit_full";//退出全屏

    public static String TIP_FREE = "tip_free";//免费获取
    public static String TIP_OPEN = "tip_open";//开通会员

    public static String CALL_SERVICE = "call_service";//联系客服
    public static String CLICK_AD = "click_id";//点击广告
    public static String PLAY_ERROR = "play_error";//播放失败
    //微信支付结果
    public static String WX_PAY_RESULT = "wx_pay_result";

    public static String EDIT_SHOW = "edit_show";//用户是否编辑


    String ACTIVITY_FRAGMENT_REPLACE = "ActivityFragmentReplace";
}
