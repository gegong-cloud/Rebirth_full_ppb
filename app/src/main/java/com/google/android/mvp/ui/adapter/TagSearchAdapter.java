package com.google.android.mvp.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.YunboMov;

import static com.google.android.app.utils.StringUtils.getStrHashCode;

/**
 * 标签搜索结果
 */
public class TagSearchAdapter extends BaseQuickAdapter<YunboMov, BaseViewHolder> {
    Activity activity;
    private AppComponent mAppComponent;
    List datas;

    /**
     * 用于显示标签
     */
    LayoutInflater mInflater;

    public TagSearchAdapter(@Nullable List data, Activity activity) {
        super(R.layout.adapter_history, data);
        this.activity = activity;
        this.datas = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, YunboMov item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        if (mInflater == null) {
            mInflater = LayoutInflater.from(activity);
        }

        helper.setText(R.id.history_title, item.getName())
                .setText(R.id.history_descirption, item.getView_num());
        showGgImage(helper, R.id.history_img, StringUtils.getBaseUrl()+item.getCover());
        //显示标签
//        TagFlowLayout tagFlowLayout = helper.getView(R.id.history_tagflowlayout);
//        if (item.getCls_list() != null && item.getCls_list().length > 0) {
//            tagFlowLayout.setVisibility(View.VISIBLE);
//            initTagView(item.getCls_list(), tagFlowLayout);
//        } else {
//            tagFlowLayout.setVisibility(View.GONE);
//        }


    }


//    private void initTagView(MovieEntity.ClsList[] cls_list, TagFlowLayout tagFlowLayout) {
//        TagAdapter<MovieEntity.ClsList> tagAdapter = new TagAdapter<MovieEntity.ClsList>(cls_list) {
//            @Override
//            public View getView(FlowLayout parent, int position, MovieEntity.ClsList clsList) {
//                TextView textView = (TextView) mInflater.inflate(R.layout.textview_movie_tag,
//                        tagFlowLayout, false);
//                textView.setText(clsList.getCls_name());
//                return textView;
//            }
//        };
//        tagFlowLayout.setAdapter(tagAdapter);
//        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                if (cls_list != null && cls_list.length > position) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("tagClick", (Serializable) cls_list[position]);
//                    activity.startActivity(new Intent(activity, TagSearchActivity.class)
//                            .putExtras(bundle)
//                    );
//                }
//                return false;
//            }
//        });
//    }

    private void showGgImage(BaseViewHolder helper, int resId, String url) {
        if (!TextUtils.isEmpty(url)) {
            StringUtils.showImgView(mAppComponent,helper.getView(resId),activity,url,getStrHashCode(url));
        } else {
            helper.setImageResource(resId, R.drawable.cloud_tag_default);
        }
    }


//    private void showImage(BaseViewHolder helper, int resId, MovieEntity.CoverEntity url, String id) {
//        if (url != null && !TextUtils.isEmpty(url.getH_small())) {
//            List<String> dataList = StringUtils.getSplit(url.getH_small());
//            showImage(helper, resId, dataList, id);
//        } else {
//            helper.setImageResource(resId, R.drawable.gg_default);
//        }
//    }

    private void showImage(BaseViewHolder helper, int resId, List<String> dataList, String id) {
        if (dataList != null && dataList.size() > 0) {
            Glide.with(mContext).load(dataList.get(0))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (helper.getView(resId) != null && helper.getView(resId).getTag() != null && helper.getView(resId).getTag() instanceof String && id.equals(helper.getView(resId).getTag())) {
                                helper.setImageDrawable(resId, resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            if (dataList.size() > 1 && !TextUtils.isEmpty(dataList.get(1))) {
                                dataList.remove(0);
                                showImage(helper, resId, dataList, id);
                            }
                        }
                    });
        } else {
            helper.setImageResource(resId, R.drawable.cloud_default_img);
        }
    }

}
