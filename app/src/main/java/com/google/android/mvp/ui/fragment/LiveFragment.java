package com.google.android.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.di.component.DaggerLiveComponent;
import com.google.android.di.module.LiveModule;
import com.google.android.mvp.contract.LiveContract;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.presenter.LivePresenter;
import com.google.android.mvp.ui.activity.LiveListActivity;
import com.google.android.mvp.ui.adapter.BannerHomeViewHolder;
import com.google.android.mvp.ui.adapter.LivePlatformAdapter;
import com.yanzhenjie.sofia.StatusView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LiveFragment extends BaseFragment<LivePresenter> implements LiveContract.View {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.live_recyclerview)
    RecyclerView liveRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    Unbinder unbinder;

    int toolBarPositionY = 0;

    LivePlatformAdapter livePlatformAdapter;

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLiveComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .liveModule(new LiveModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImmersionBar.with(this).destroy();
        unbinder.unbind();
        if(homeBanner!=null){
            homeBanner.pause();
        }
    }


    /**
     * 添加banner
     */
    void adapterAddBanner() {
        livePlatformAdapter = new LivePlatformAdapter(null, getActivity());
        if (headerView == null) {
            headerView = initHeaderView();
            livePlatformAdapter.addHeaderView(headerView);
        }
        liveRecyclerview.setAdapter(livePlatformAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = livePlatformAdapter.getItemViewType(position);
                if (type == 0) {
                    return 1;//只占一行中的一列，
                } else {
                    return gridLayoutManager.getSpanCount();//独占一行
                }
            }
        });
        liveRecyclerview.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void setBannerInfo(List<HomeAdv> adHomeEntity) {
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
        if (adHomeEntity != null && adHomeEntity.size() > 0) {
            homeBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                @Override
                public void onPageClick(View view, int position) {
                    if (adHomeEntity != null && position < adHomeEntity.size() && !TextUtils.isEmpty(adHomeEntity.get(position).getLink())) {
                        EventBus.getDefault().post(adHomeEntity.get(position).getId(), EventBusTags.CLICK_AD);
                        HbCodeUtils.openBrowser(getActivity(), adHomeEntity.get(position).getLink());
                    }
                }
            });
            homeBanner.setPages(adHomeEntity, new MZHolderCreator() {
                @Override
                public MZViewHolder createViewHolder() {
                    return new BannerHomeViewHolder();
                }
            });

            homeBanner.start();

        }
    }

    @Override
    public void showAdapter(List<LiveInfo> list) {
        if (list == null) {
            return;
        }
        livePlatformAdapter.replaceData(list);
        livePlatformAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (TextUtils.isEmpty(HbCodeUtils.getToken())) {
//                    launchActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    return;
//                }
                launchActivity(new Intent(getActivity(), LiveListActivity.class)
                        .putExtra("name", list.get(position).getName())
                );
            }
        });

    }

    @Override
    public void setCountTitle(String countTitle) {
        liveJgNumber.setText(countTitle);
    }

    View headerView;
    MZBannerView homeBanner;
    TextView liveJgNumber;

    public View initHeaderView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_live_platform_banner, null);
        homeBanner = (MZBannerView) headerView.findViewById(R.id.home_banner);
        liveJgNumber = (TextView) headerView.findViewById(R.id.live_jg_number);
        return headerView;
    }
}
