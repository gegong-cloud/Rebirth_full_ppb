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

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.marquee.dingrui.marqueeviewlib.MarqueeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.sofia.StatusView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.di.component.DaggerMovieComponent;
import com.google.android.di.module.MovieModule;
import com.google.android.mvp.contract.MovieContract;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.LabelEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.presenter.MoviePresenter;
import com.google.android.mvp.ui.activity.AllSearchActivity;
import com.google.android.mvp.ui.activity.MyHistoryActivity;
import com.google.android.mvp.ui.adapter.BannerHomeViewHolder;
import com.google.android.mvp.ui.adapter.CloudTitleAdapter;
import com.google.android.mvp.ui.adapter.HomeMovAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 新的首页
 */
public class MovieFragment extends BaseFragment<MoviePresenter> implements MovieContract.View {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.title_recyclerview)
    RecyclerView titleRecyclerview;
    @BindView(R.id.home_hot_recyclerview)
    RecyclerView homeHotRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.home_float_gg)
    RelativeLayout homeFloatGg;

    Unbinder unbinder;

    HomeMovAdapter homeMovAdapter;
    List<HomeVipMov.HomeVipMovList> centerData;

    /**
     * 初始化首页top数据
     */
    List<LabelEntity> topList;



    //进度框
    private MaterialDialog materialDialog;

    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMovieComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .movieModule(new MovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(getActivity()).content("正在加载...").progress(true, 0).build();
        adapterAddBanner();
        mPresenter.loadTop(initTopData(), smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.homeIndex(1, smartRefresh, true);
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMore(smartRefresh);
            }
        });

    }

    List<LabelEntity> initTopData() {
        if (topList == null) {
            topList = new ArrayList<>();
            LabelEntity labelEntity = new LabelEntity("1", "推荐", true);
            LabelEntity labelEntity1 = new LabelEntity("2", "排名", false);
            LabelEntity labelEntity2 = new LabelEntity("3", "最新", false);
            topList.add(labelEntity);
            topList.add(labelEntity1);
            topList.add(labelEntity2);
        }
        return topList;
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
        materialDialog.dismiss();
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
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
        // TODO: inflate a fragment view
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
        if (homeBanner != null) {
            homeBanner.pause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @OnClick(R.id.home_float_gg)
    public void onViewClicked() {
//        if (mPresenter != null) {
//            mPresenter.floatClick(getActivity());
//        }
    }

    @Override
    public void setBannerInfo(List<HomeAdv> advList) {
//        homeBanner.setVisibility(advList.size()>0?View.VISIBLE:View.GONE);
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
//            advList.get(0).setImage("http://test.k0fahc1.top/img/secret_8296fe07750f4f8094afa5987338baf9.gif");
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
            homeBanner.start();

        }
    }

    @Override
    public void showDeng(List<ScrollMsg> listDeng) {
        if (listDeng != null && listDeng.size() > 0) {
            homeMenuBg.setVisibility(View.VISIBLE);
            List<String> listData = new ArrayList<>();
            for (ScrollMsg scrollMsg : listDeng) {
                listData.add(scrollMsg.toString());
            }
            homeMenuTextDeng.setContent(listData);
        } else {
            homeMenuBg.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFloat(HomeAdv homeAdv) {
        homeFloatGg.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAdapter(List<HomeVipMov.HomeVipMovList> list, boolean loadMore) {
        if (list == null || homeMovAdapter == null) {
            return;
        }
        if (loadMore) {
            centerData.addAll(list);
            homeMovAdapter.addData(list);
        } else {
            centerData = list;
            homeMovAdapter.replaceData(list);
        }
        homeMovAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.castItemClick(centerData.get(position));
            }
        });

        homeMovAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (R.id.my_favor == view.getId()) {
                    mPresenter.favor(centerData.get(position), position);
                }
            }
        });

    }

    @Override
    public HomeMovAdapter getHomeAdapter() {
        return homeMovAdapter == null ? adapterAddBanner() : homeMovAdapter;
    }

    @Override
    public void showAdapterTop(CloudTitleAdapter cloudTitleAdapter) {
        titleRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        titleRecyclerview.setAdapter(cloudTitleAdapter);
    }

    @Override
    public void showCenterAdapter(boolean flag) {
        if (homeHotRecyclerview != null) {
            homeHotRecyclerview.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void updateFavoUI(int position) {
        if (homeMovAdapter != null && centerData != null && centerData.size() > 0 && position < centerData.size()) {
            centerData.get(position).setHas_favor("1".equals(centerData.get(position).getHas_favor()) ? "0" : "1");
            if (homeMovAdapter.getData().size() > position + 1) {
                homeMovAdapter.notifyItemChanged(position + 1, centerData.get(position));
            }
        }
    }

    /**
     * 添加banner
     */
    HomeMovAdapter adapterAddBanner() {
        if (homeMovAdapter == null) {
            if (centerData == null) {
                centerData = new ArrayList<>();
            }
            homeMovAdapter = new HomeMovAdapter(centerData, getActivity());
        }

        if (headerView == null) {
            headerView = initHeaderView();
            homeMovAdapter.addHeaderView(headerView);
        }

        homeHotRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeHotRecyclerview.setAdapter(homeMovAdapter);
        return homeMovAdapter;
    }


    View headerView;
    MZBannerView homeBanner;
    LinearLayout homeMenuBg;
    MarqueeView homeMenuTextDeng;

    public View initHeaderView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_banner, null);
        homeBanner = (MZBannerView) headerView.findViewById(R.id.home_banner);
        homeMenuBg = (LinearLayout) headerView.findViewById(R.id.home_deng_bg);
        homeMenuTextDeng = (MarqueeView) headerView.findViewById(R.id.home_menu_text_deng);

        return headerView;
    }

    @OnClick({R.id.search_menu, R.id.history_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_menu:
                launchActivity(new Intent(getActivity(), AllSearchActivity.class));
                break;
            case R.id.history_menu:
                launchActivity(new Intent(getActivity(), MyHistoryActivity.class));
                break;
        }
    }


}
