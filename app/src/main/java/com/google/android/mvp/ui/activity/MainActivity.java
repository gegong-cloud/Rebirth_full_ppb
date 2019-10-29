package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.AdapterViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.livechatinc.inappchat.ChatWindowActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.di.component.DaggerMainComponent;
import com.google.android.di.module.MainModule;
import com.google.android.mvp.contract.MainContract;
import com.google.android.mvp.presenter.MainPresenter;
import com.google.android.mvp.ui.fragment.CloudFragment;
import com.google.android.mvp.ui.fragment.HomeFragment;
import com.google.android.mvp.ui.fragment.LiveFragment;
import com.google.android.mvp.ui.fragment.MineFragment;
import com.google.android.mvp.ui.fragment.MovieFragment;
import com.google.android.mvp.ui.widget.NoScrollViewPager;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.bottomBar)
    public BottomBar bottomBar;
    @BindView(R.id.view_pager)
    public NoScrollViewPager viewPager;
    @BindView(R.id.call_service)
    TextView callService;
    @BindView(R.id.recharge_img)
    ImageView rechargeImg;

    private List<Fragment> mFragments;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        // 所有子类都将继承这些相同的属性,请在设置界面之后设置
        ImmersionBar.with(this).init();
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .statusBarBackgroundAlpha(20)
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);

        String gifUrl = "android.resource://" + getPackageName() + "/" + R.raw.home_recharge;
        showGgImage(rechargeImg,gifUrl);

        if (mPresenter != null) {
            mPresenter.checkUpdate(this);
            mPresenter.homeConfig(this);
            mPresenter.getServiceUrl();
//            mPresenter.getServiceUrl();
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(MovieFragment.newInstance());//新的首页
            mFragments.add(CloudFragment.newInstance());//热门
            mFragments.add(LiveFragment.newInstance());//附近
            mFragments.add(HomeFragment.newInstance());//原首页移动到第四位
            mFragments.add(MineFragment.newInstance());//娱乐城
        }
        viewPager.setNoScroll(true);
        viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(), mFragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                showCallService(tabId);
                switch (tabId) {
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_cloud:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_live:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.tab_movie:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.tab_mine:
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Subscriber(tag = EventBusTags.NET_WORK_URL)
    public void getNowUrl(String urlStr) {//每次启动的时候切换，这里不再增加逻辑
        ArmsUtils.makeText(this, "服务器开小差了,建议重启APP再试!");
    }

    /**
     * @param serviceTag 联系客服
     */
    @Subscriber(tag = EventBusTags.CALL_SERVICE)
    public void callService(String serviceTag) {
        castLiveChat();
    }

    /**
     * @param ad_id 广告id
     */
    @Subscriber(tag = EventBusTags.CLICK_AD)
    public void clickAd(String ad_id) {
        if (mPresenter != null) {
            mPresenter.logAd(ad_id);
        }
    }

    /**
     * @param yunboId 广告id
     */
    @Subscriber(tag = EventBusTags.PLAY_ERROR)
    public void playError(String yunboId) {
        if (mPresenter != null) {
            mPresenter.logError(yunboId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getSupportActionBar().hide();
//        initOpenInstall();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ImmersionBar.with(this).destroy();
    }


    @OnClick({R.id.call_service, R.id.recharge_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.call_service:
                castLiveChat();
                break;
            case R.id.recharge_img:
                launchActivity(new Intent(this, VipRechargeActivity.class));
                break;
        }
    }



    void showCallService(int resId) {
        callService.setVisibility(resId == R.id.tab_mine ? View.VISIBLE : View.GONE);
        rechargeImg.setVisibility(resId != R.id.tab_mine ? View.VISIBLE : View.GONE);
    }

    /**
     * 联系客服
     */
    void castLiveChat() {
        String license = "";//license
        String serviceId = "";//客服id
        String userId = "";//用户id
        if (HbCodeUtils.getServiceAdd() != null) {
            license = !TextUtils.isEmpty(HbCodeUtils.getServiceAdd().getValue()) ? HbCodeUtils.getServiceAdd().getValue() : "";
            serviceId = !TextUtils.isEmpty(HbCodeUtils.getServiceAdd().getName()) ? HbCodeUtils.getServiceAdd().getName() : "";
        }
        if (HbCodeUtils.getUserInfo() != null && !TextUtils.isEmpty(HbCodeUtils.getUserInfo().getUid())) {
            userId = HbCodeUtils.getUserInfo().getUid();
        }
//        if(HbCodeUtils.getUserInfo()!=null&&!TextUtils.isEmpty(HbCodeUtils.getUserInfo().getNickname())){
//            userId = HbCodeUtils.getUserInfo().getNickname();
//        }
        if (TextUtils.isEmpty(serviceId) || TextUtils.isEmpty(license) || TextUtils.isEmpty(userId)) {
            ArmsUtils.makeText(this, "当前客服繁忙,请稍后再试!");
            mPresenter.getServiceUrl();
            return;
        }
        Intent intent = new Intent(this, ChatWindowActivity.class);
        intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, serviceId);
        intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, license);
        intent.putExtra(ChatWindowActivity.KEY_VISITOR_NAME, userId);
//        intent.putExtra(ChatWindowActivity.KEY_VISITOR_EMAIL, "2206062644@qq.com");
//        intent.putExtra("myParam", "Android Rules!");
        launchActivity(intent);
    }


    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                ArmsUtils.makeText(this, "再按一次退出程序");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showGgImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            ArmsUtils.obtainAppComponentFromContext(this)
                    .imageLoader()
                    .loadImage(this, ImageConfigImpl
                            .builder()
                            .imageView(imageView)
                            .url(url)
                            .build());
        }
    }
}
