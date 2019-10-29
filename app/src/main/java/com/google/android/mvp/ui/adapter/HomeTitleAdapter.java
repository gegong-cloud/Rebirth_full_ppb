package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.HomeVipMov;

import org.simple.eventbus.EventBus;

import java.util.List;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * vip影视title
 */
public class HomeTitleAdapter extends BaseQuickAdapter<HomeVipMov, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    public HomeTitleAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_home_menu_title, data);
        this.activity = activity;
        this.datas = data;
        setMultiTypeDelegate(new MultiTypeDelegate<HomeVipMov>() {
            @Override
            protected int getItemType(HomeVipMov hVideo) {
                return hVideo.getType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.adapter_home_menu_title)//标签
                .registerItemType(1, R.layout.adapter_gg_img);//banner
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeVipMov item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.home_menu_title, !TextUtils.isEmpty(item.getName()) ? item.getName() : "");
                showLeftImg(helper,item);
                if (item.getList() != null && item.getList().size() > 0) {
                    initRecyclerView(helper, item);
                }

                break;
            case 1:
                if(item.getBottomAdv()!=null&&item.getBottomAdv().size()>0){
                    showGgImage(helper, R.id.home_two_mode_img, StringUtils.getBaseUrl()+item.getBottomAdv().get(0).getImage());
                }
                break;
        }


    }

    private void showLeftImg(BaseViewHolder helper, HomeVipMov item){
        if("推荐服务".equals(item.getName())){
            helper.setImageResource(R.id.home_menu_img,R.drawable.home_recommend);
        }else{
            helper.setImageResource(R.id.home_menu_img,R.drawable.home_hot_img);
        }
    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent,helper.getView(resId),activity,url,getStrHashCode(url));
//            mAppComponent
//                    .imageLoader()
//                    .loadImage(mContext, ImageConfigImpl
//                            .builder()
//                            .imageView(helper.getView(resId))
//                            .url(url)
//                            .build());
        } else {
            helper.setImageResource(resId, R.drawable.ic_launcher_background);
        }
    }


    void initRecyclerView(BaseViewHolder helper, HomeVipMov item) {
        RecyclerView recyclerView = helper.getView(R.id.home_menu_recyclerview);
        HomeOldMovAdapter cloudCenterAdapter = new HomeOldMovAdapter(item.getList(), activity);
        recyclerView.setAdapter(cloudCenterAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        cloudCenterAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                castVipVideo(item.getList().get(position));
            }
        });
    }


    private void castVipVideo(HomeVipMov.HomeVipMovList homeVipMov) {//
//        if (TextUtils.isEmpty(HbCodeUtils.getToken())) {
//            activity.startActivity(new Intent(activity, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            return;
//        }else{
            EventBus.getDefault().post(homeVipMov, EventBusTags.HOME_ITEM_CLICK);
//        }
    }


}
