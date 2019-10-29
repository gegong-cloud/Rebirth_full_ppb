package com.google.android.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.di.component.DaggerHomeComponent;
import com.google.android.di.module.HomeModule;
import com.google.android.mvp.contract.HomeContract;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.presenter.HomePresenter;
import com.google.android.mvp.ui.adapter.BannerHomeViewHolder;
import com.google.android.mvp.ui.adapter.HomeTitleAdapter;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.home_hot_recyclerview)
    RecyclerView homeHotRecyclerview;
    @BindView(R.id.home_float_gg)
    RelativeLayout homeFloatGg;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    Unbinder unbinder;

    HomeTitleAdapter homeTitleAdapter;

    //进度框
    private MaterialDialog materialDialog;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(getActivity()).content("正在加载...").progress(true, 0).build();
        adapterAddBanner();
        mPresenter.initInfo(smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.initInfo(refreshLayout);
            }
        });
    }

    /**
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        materialDialog.show();
    }

    @Override
    public void hideLoading() {
        materialDialog.cancel();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImmersionBar.with(this).destroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscriber(tag = EventBusTags.HOME_ITEM_CLICK)
    public void checkUser(HomeVipMov.HomeVipMovList homeVipMov) {
        if (homeVipMov != null) {
            if("3".equals(homeVipMov.getIsinner())&&!TextUtils.isDigitsOnly(homeVipMov.getLink())){//外部浏览器
                EventBus.getDefault().post("", EventBusTags.CLICK_AD);//点击广告
                HbCodeUtils.openBrowser(getActivity(),homeVipMov.getLink());
                return;
            }else{//vip特权播放界面
                mPresenter.checkUser(homeVipMov);
            }

            //            if ("2".equals(homeVipMov.getIsinner())) {//原有vip
//                if (TextUtils.isEmpty(HbCodeUtils.getToken())) {
//                    launchActivity(new Intent(getActivity(), LoginActivity.class));
//                    return;
//                }
//            } else if ("2".equals(homeVipMov.getIsinner())) {//跳转云播和直播
//                if (!TextUtils.isEmpty(homeVipMov.getLink()) && "zhibo".equals(homeVipMov.getLink())) {
//                    mPresenter.liveIndex();
//                } else if (!TextUtils.isEmpty(homeVipMov.getLink()) && homeVipMov.getLink().startsWith("yunbo")) {
//                    String[] yunboIds = homeVipMov.getLink().split("id=");
//                    if (yunboIds != null && yunboIds.length >= 2) {
//                        launchActivity(new Intent(getActivity(), CloudListActivity.class)
//                                .putExtra("cloudTags",yunboIds[1])
//                        );
//                    } else {
//                        if(((MainActivity)getActivity()).bottomBar!=null&&((MainActivity) getActivity()).viewPager != null){
//                            ((MainActivity)getActivity()).bottomBar.selectTabWithId(R.id.tab_cloud);
//                            ((MainActivity) getActivity()).viewPager.setCurrentItem(1);
//                        }
//                    }
//                }
//            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (homeMenuTextDeng != null) {
//            homeMenuTextDeng.startFlipping();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (homeMenuTextDeng != null) {
//            homeMenuTextDeng.stopFlipping();
//        }
    }


    @OnClick({R.id.home_float_gg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_float_gg:
                if (mPresenter != null) {
                    mPresenter.floatClick(getActivity());
                }
                break;
        }
    }

    /**
     * 添加banner
     */
    void adapterAddBanner() {
        homeTitleAdapter = new HomeTitleAdapter(null, getActivity());
//        if (headerView == null) {
//            headerView = initHeaderView();
//            homeTitleAdapter.addHeaderView(headerView);
//        }
        homeHotRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeHotRecyclerview.setAdapter(homeTitleAdapter);
    }


    @Override
    public void setBannerInfo(List<HomeAdv> advList) {
        homeBanner.addPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeBanner.setIndicatorVisible(true);
        homeBanner.setIndicatorPadding(0, 200, 10, 0);
        if (advList != null && advList.size() > 0) {
            homeBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                @Override
                public void onPageClick(View view, int position) {
                    if (advList != null && position < advList.size() && !TextUtils.isEmpty(advList.get(position).getLink())) {
                        EventBus.getDefault().post(advList.get(position).getId(), EventBusTags.CLICK_AD);
                        HbCodeUtils.openBrowser(getActivity(), advList.get(position).getLink());
                    }
                }
            });
            homeBanner.setPages(advList, new MZHolderCreator() {
                @Override
                public MZViewHolder createViewHolder() {
                    return new BannerHomeViewHolder();
                }
            });

        }
    }

    @Override
    public void showDeng(List<ScrollMsg> listDeng) {
        if (listDeng != null && listDeng.size() > 0) {
            homeMenuBg.setVisibility(View.VISIBLE);
//            SimpleMF<ScrollMsg> marqueeFactory = new SimpleMF(getActivity());
//            marqueeFactory.setData(listDeng);
//            homeMenuTextDeng.setMarqueeFactory(marqueeFactory);
//            homeMenuTextDeng.startFlipping();
        } else {
            homeMenuBg.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFloat(HomeAdv homeAdv) {
        homeFloatGg.setVisibility(View.VISIBLE);
    }


    @Override
    public void showAdapter(List<HomeVipMov> homeVipMovList) {
        if (homeVipMovList == null) {
            return;
        }
        homeTitleAdapter.replaceData(homeVipMovList);
        homeTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (homeVipMovList != null && position < homeVipMovList.size()) {//广告
                    HomeVipMov homeVipMov = homeVipMovList.get(position);
                    if (homeVipMov != null && homeVipMov.getBottomAdv() != null && homeVipMov.getBottomAdv().size() > 0 && !TextUtils.isEmpty(homeVipMov.getBottomAdv().get(0).getLink())) {
                        EventBus.getDefault().post(homeVipMov.getBottomAdv().get(position).getId(), EventBusTags.CLICK_AD);
                        HbCodeUtils.openBrowser(getActivity(), homeVipMov.getBottomAdv().get(0).getLink());
                    }
                }
            }
        });
    }

    View headerView;
    MZBannerView homeBanner;
    LinearLayout homeMenuBg;
//    SimpleMarqueeView homeMenuTextDeng;

    public View initHeaderView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_banner, null);
        homeBanner = (MZBannerView) headerView.findViewById(R.id.home_banner);
        homeMenuBg = (LinearLayout) headerView.findViewById(R.id.home_deng_bg);
//        homeMenuTextDeng = (SimpleMarqueeView) headerView.findViewById(R.id.home_menu_text_deng);
//        homeMenuTextDeng.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClickListener(View mView, Object mData, int mPosition) {
//                launchActivity(new Intent(getActivity(), LivingActivity.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .putExtra("title", "aabbbb")
//                        .putExtra("sysmsg", "")
//                        .putExtra("img", "")
//                        .putExtra("path", "rtmp://video.818ousrcc.com/live/linsuochang")
//                );
//            }
//        });

        return headerView;
    }


}
