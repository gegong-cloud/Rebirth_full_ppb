package com.google.android.mvp.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;
import com.yanzhenjie.sofia.Sofia;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.MyApplication;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerMinePlayComponent;
import com.google.android.di.module.MinePlayModule;
import com.google.android.mvp.contract.MinePlayContract;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.presenter.MinePlayPresenter;
import com.google.android.mvp.ui.adapter.HistoryAdapter;
import com.google.android.mvp.ui.adapter.PlayAdvAdapter;
import com.google.android.mvp.ui.widget.player.CloudVideo;
import com.google.android.mvp.ui.widget.player.OnTransitionListener;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.google.android.app.utils.StringUtils.getStrHashCode;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MinePlayActivity extends BaseActivity<MinePlayPresenter> implements MinePlayContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.gg_recyclerview)
    RecyclerView ggRecyclerview;
    @BindView(R.id.gsy_player)
    CloudVideo videoPlayer;
    @BindView(R.id.tip_title)
    TextView tipTitle;
    @BindView(R.id.play_tip)
    RelativeLayout playTip;
    @BindView(R.id.tip_second)
    TextView tipSecond;
    @BindView(R.id.play_title)
    TextView playTitle;
    @BindView(R.id.play_number)
    TextView playNumber;
    @BindView(R.id.play_details_text)
    TextView playDetailsText;
    @BindView(R.id.play_details)
    LinearLayout playDetails;
    @BindView(R.id.play_recyclerview)
    RecyclerView playRecyclerview;
    @BindView(R.id.favor_img)
    ImageView favorImg;
    @BindView(R.id.play_jj)
    TextView playJj;
    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;

    //默认简介收起
    boolean showJj = false;


    private GSYVideoOptionBuilder gsyVideoOptionBuilder;

    /**
     * 播放器相关参数
     */
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    private boolean isTransition;
    private boolean isPlay;
    private boolean isPause;
    private Transition transition;
    //播放地址
    String playTitleStr = "";
    //影片id
    String movId = "";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMinePlayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .minePlayModule(new MinePlayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_play; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        movId = getIntent().getStringExtra("movId");
        if (mPresenter != null) {
            mPresenter.yunboView(movId);
            mPresenter.getAdvList();
            mPresenter.movSimilar(movId);
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (videoPlayer != null) {
            videoPlayer.release();
            GSYVideoManager.releaseAllVideos();
        }
        if (getOrientationUtils() != null) {
            getOrientationUtils().releaseListener();
        }
    }

    @Subscriber(tag = EventBusTags.TIP_FREE)
    public void tipFree(String event) {
        launchActivity(new Intent(this, InviteFriendActivity.class));
    }

    @Subscriber(tag = EventBusTags.TIP_OPEN)
    public void tipOpen(String event) {
        mPresenter.castRecharge(this);
    }


    @Override
    protected void onPause() {
        videoPlayer.onVideoPause();
        super.onPause();
        isPause = true;

    }

    @Override
    protected void onResume() {
        videoPlayer.onVideoResume();
        super.onResume();
        isPause = false;

    }

    @Override
    public void onBackPressed() {
        if (getOrientationUtils() != null) {
            getOrientationUtils().backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        super.onBackPressed();

//        if (videoPlayer.isIfCurrentIsFullscreen()) {
//            super.onBackPressed();
//        } else {
//            videoPlayer.setVideoAllCallBack(null);
//            GSYVideoManager.releaseAllVideos();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//                }
//            }, 500);
//        }
    }


    @OnClick({R.id.toolbar_back, R.id.tip_free, R.id.tip_open, R.id.play_jj, R.id.favor_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                dissMissAll(view);
                break;
            case R.id.tip_free:
                launchActivity(new Intent(this, InviteFriendActivity.class));
                break;
            case R.id.tip_open:
                mPresenter.castRecharge(this);
                break;
            case R.id.play_jj:
                showJJ();
                break;
            case R.id.favor_click:
                if (mPresenter != null) {
                    mPresenter.favor();
                }
                break;
        }
    }


    /**
     * 循环切换播放链接
     */
    int switchUrl = 0;


    @Override
    public void initVedio(YunboMov movieInfo) {
        updateFavoUI("1".equals(movieInfo.getHas_favor()));
        showDetails(movieInfo);
        YunboMov.AddressEntity hVideoDetail;
        if (!TextUtils.isEmpty(movieInfo.getAddress())) {
            hVideoDetail = (MyApplication.getsInstance()).getAppComponent().gson().fromJson(StringUtils.replaceXieGang(movieInfo.getAddress()), YunboMov.AddressEntity.class);
        } else {
            return;
        }
        LinkedHashMap<String, String> map = new LinkedHashMap();
        boolean highTag = false;//高清
        boolean sighTag = false;//标清
        if (hVideoDetail.getP1080() != null && hVideoDetail.getP1080().size() > 0 && hVideoDetail.getP1080().size() > switchUrl && !TextUtils.isEmpty(hVideoDetail.getP1080().get(switchUrl).getAddr())) {
            highTag = true;
            map.put("高清", hVideoDetail.getP1080().get(switchUrl).getAddr());
            Timber.e("播放地址：" + hVideoDetail.getP1080().get(switchUrl).getAddr());
            ArmsUtils.makeText(this, "已自动切换到线路：" + hVideoDetail.getP1080().get(switchUrl).getRatio());
        } else if (hVideoDetail.getP720() != null && hVideoDetail.getP720().size() > 0 && hVideoDetail.getP720().size() > switchUrl && !TextUtils.isEmpty(hVideoDetail.getP720().get(switchUrl).getAddr())) {
            if (!highTag) {//高清添加
                highTag = true;
                map.put("高清", hVideoDetail.getP720().get(switchUrl).getAddr());
                Timber.e("播放地址：" + hVideoDetail.getP720().get(switchUrl).getAddr());
                ArmsUtils.makeText(this, "已自动切换到线路：" + hVideoDetail.getP720().get(switchUrl).getRatio());
            }
        }
        if (hVideoDetail.getP480() != null && hVideoDetail.getP480().size() > 0 && hVideoDetail.getP480().size() > switchUrl && !TextUtils.isEmpty(hVideoDetail.getP480().get(switchUrl).getAddr())) {
            sighTag = true;
            map.put("标清", hVideoDetail.getP480().get(switchUrl).getAddr());
            Timber.e("播放地址：" + hVideoDetail.getP480().get(switchUrl).getAddr());
            ArmsUtils.makeText(this, "已自动切换到线路：" + hVideoDetail.getP480().get(switchUrl).getRatio());
        } else if (hVideoDetail.getP360() != null && hVideoDetail.getP360().size() > 0 && hVideoDetail.getP360().size() > switchUrl && !TextUtils.isEmpty(hVideoDetail.getP360().get(switchUrl).getAddr())) {
            if (!sighTag) {//高清添加
                sighTag = true;
                map.put("标清", hVideoDetail.getP360().get(switchUrl).getAddr());
                Timber.e("播放地址：" + hVideoDetail.getP360().get(switchUrl).getAddr());
                ArmsUtils.makeText(this, "已自动切换到线路：" + hVideoDetail.getP360().get(switchUrl).getRatio());
            }
        } else if (hVideoDetail.getP240() != null && hVideoDetail.getP240().size() > 0 && hVideoDetail.getP240().size() > switchUrl && !TextUtils.isEmpty(hVideoDetail.getP240().get(switchUrl).getAddr())) {
            if (!sighTag) {//高清添加
                sighTag = true;
                map.put("标清", hVideoDetail.getP240().get(switchUrl).getAddr());
                Timber.e("播放地址：" + hVideoDetail.getP240().get(switchUrl).getAddr());
                ArmsUtils.makeText(this, "已自动切换到线路：" + hVideoDetail.getP240().get(switchUrl).getRatio());
            }
        }

        switchUrl++;
        if ((hVideoDetail.getP360() != null && hVideoDetail.getP360().size() > 0 && hVideoDetail.getP360().size() <= switchUrl) || (hVideoDetail.getP480() != null && hVideoDetail.getP480().size() > 0 && hVideoDetail.getP480().size() <= switchUrl) || (hVideoDetail.getP720() != null && hVideoDetail.getP720().size() > 0 && hVideoDetail.getP720().size() <= switchUrl) || (hVideoDetail.getP1080() != null && hVideoDetail.getP1080().size() > 0 && hVideoDetail.getP1080().size() <= switchUrl)) {
            switchUrl = 0;
        }

        if (map != null) {
            if (!TextUtils.isEmpty(map.get("高清"))) {
                playTitleStr = map.get("高清");
            } else if (!TextUtils.isEmpty(map.get("标清"))) {
                playTitleStr = map.get("标清");
            }
        }
        initAnchorLive(movieInfo);
        //gif
        if (videoPlayer != null) {
        }
    }

    @Override
    public void showJJ() {
        showJj = showJj ? false : true;
        Drawable drawable = showJj ? getResources().getDrawable(
                R.drawable.all_collapse_img) : getResources().getDrawable(
                R.drawable.all_expand_img);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        playJj.setCompoundDrawables(null, null, drawable, null);
        playDetailsText.setVisibility(showJj ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateFavoUI(boolean isFavo) {
        favorImg.setImageResource(isFavo ? R.drawable.favor_press : R.drawable.favor_nopress);
    }


    void showDetails(YunboMov movieInfo) {
        if (movieInfo == null) {
            return;
        }
        playTitle.setText(!TextUtils.isEmpty(movieInfo.getName()) ? movieInfo.getName() : "");
        playNumber.setText(!TextUtils.isEmpty(movieInfo.getView_num()) ? ("播放次数:" + movieInfo.getView_num()) : "");
        playDetailsText.setText(!TextUtils.isEmpty(movieInfo.getContent()) ? movieInfo.getContent() : "");
        playNumber.setVisibility(!TextUtils.isEmpty(movieInfo.getView_num()) ? View.VISIBLE : View.GONE);
//        playDetailsText.setVisibility(!TextUtils.isEmpty(movieInfo.getContent()) ? View.VISIBLE : View.GONE);
        playDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAdapter(PlayAdvAdapter playAdvAdapter) {
        ggRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        ggRecyclerview.setAdapter(playAdvAdapter);
    }

    @Override
    public void showAdapter(HistoryAdapter historyAdapter) {
        playRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        playRecyclerview.setAdapter(historyAdapter);
    }


    /**
     * 初始化直播数据
     *
     * @param movieInfo
     */
    private void initAnchorLive(YunboMov movieInfo) {
        //初始化不打开外部的旋转
        if (getOrientationUtils() != null) {
            getOrientationUtils().setEnable(false);
        }
        Map<String, String> headMap = null;
        if (movieInfo.getHeader_list() != null && movieInfo.getHeader_list().size() > 0) {
            headMap = new HashMap<>();
            for (YunboMov.HeaderList headerList : movieInfo.getHeader_list()) {
                headMap.put(headerList.getKey(), headerList.getValue());
            }
        }
        videoPlayer.getBackButton().setVisibility(View.INVISIBLE);//隐藏返回
        ImageView imageView = new ImageView(this);
        showImg(movieInfo.getCover(), imageView);
        if (!TextUtils.isEmpty(playTitleStr)) {
            gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                    .setMapHeadData(headMap == null ? new HashMap<>() : headMap)
                    .setIsTouchWiget(true)//滑动功能
                    .setRotateViewAuto(false)//自动旋转
                    .setLockLand(false)//一全屏就锁屏横屏
                    .setShowFullAnimation(false)
                    .setNeedShowWifiTip(true)//wifi tishi
                    .setNeedLockFull(true)//需要锁屏
                    .setThumbImageView(imageView)//封面图
                    .setSeekRatio(1)
                    .setUrl(playTitleStr)//播放链接
                    .setCacheWithPlay(false)
//                    .setStartAfterPrepared(false)
//                    .setVideoTitle(movieInfo.getName())//标题
                    .setVideoAllCallBack(new VideoAllCallBack() {
                        @Override
                        public void onStartPrepared(String url, Object... objects) {
                            Log.e("lhw---", "onStartPrepared,url=" + url);
                        }

                        @Override
                        public void onPrepared(String url, Object... objects) { //开始播放了
                            Log.e("lhw---", "onPrepared,url=" + url);
                            //开始播放了才能旋转和全屏
                            if (getOrientationUtils() != null) {
                                getOrientationUtils().setEnable(true);
                                isPlay = true;
                            }
                        }

                        @Override
                        public void onClickStartIcon(String url, Object... objects) {
                            Log.e("lhw---", "onClickStartIcon,url=" + url);
                        }

                        @Override
                        public void onClickStartError(String url, Object... objects) {
                            Log.e("lhw---", "onClickStartError,url=" + url);
                        }

                        @Override
                        public void onClickStop(String url, Object... objects) {
                            Log.e("lhw---", "onClickStop,url=" + url);
                        }

                        @Override
                        public void onClickStopFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onClickStopFullscreen,url=" + url);
                        }

                        @Override
                        public void onClickResume(String url, Object... objects) {
                            Log.e("lhw---", "onClickResume,url=" + url);
                        }

                        @Override
                        public void onClickResumeFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onClickResumeFullscreen,url=" + url);
                        }

                        @Override
                        public void onClickSeekbar(String url, Object... objects) {
                            Log.e("lhw---", "onClickSeekbar,url=" + url);
                        }

                        @Override
                        public void onClickSeekbarFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onClickSeekbarFullscreen,url=" + url);
                        }

                        @Override
                        public void onAutoComplete(String url, Object... objects) {
                            Log.e("lhw---", "onAutoComplete,url=" + url);
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onEnterFullscreen,url=" + url);
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onQuitFullscreen,url=" + url);
                            if (getOrientationUtils() != null) {
                                getOrientationUtils().backToProtVideo();
                            }
                        }

                        @Override
                        public void onQuitSmallWidget(String url, Object... objects) {
                            Log.e("lhw---", "onQuitSmallWidget,url=" + url);
                        }

                        @Override
                        public void onEnterSmallWidget(String url, Object... objects) {
                            Log.e("lhw---", "onEnterSmallWidget,url=" + url);
                        }

                        @Override
                        public void onTouchScreenSeekVolume(String url, Object... objects) {
                            Log.e("lhw---", "onTouchScreenSeekVolume,url=" + url);
                        }

                        @Override
                        public void onTouchScreenSeekPosition(String url, Object... objects) {
                            Log.e("lhw---", "onTouchScreenSeekPosition,url=" + url);
                        }

                        @Override
                        public void onTouchScreenSeekLight(String url, Object... objects) {
                            Log.e("lhw---", "onTouchScreenSeekLight,url=" + url);
                        }

                        @Override
                        public void onPlayError(String url, Object... objects) {
                            Log.e("lhw---", "onPlayError,url=" + url);
                            EventBus.getDefault().post(movieInfo.getId(), EventBusTags.PLAY_ERROR);
                            ArmsUtils.makeText(MinePlayActivity.this, "视频加载失败!");
                        }

                        @Override
                        public void onClickStartThumb(String url, Object... objects) {
                            Log.e("lhw---", "onClickStartThumb,url=" + url);
                        }

                        @Override
                        public void onClickBlank(String url, Object... objects) {
                            Log.e("lhw---", "onClickBlank,url=" + url);
                        }

                        @Override
                        public void onClickBlankFullscreen(String url, Object... objects) {
                            Log.e("lhw---", "onClickBlankFullscreen,url=" + url);
                        }
                    })
                    .setLockClickListener(new LockClickListener() {
                        @Override
                        public void onClick(View view, boolean lock) {
                            if (getOrientationUtils() != null) {
                                //配合下方的onConfigurationChanged
                                getOrientationUtils().setEnable(!lock);
                            }
                        }
                    })//锁屏点击
                    .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                        @Override
                        public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
//                            Timber.e(" progress= " + progress + "--- secProgress= " + secProgress + "---currentPosition= " + currentPosition + "--- duration =" + duration);
                            if (movieInfo != null && !TextUtils.isEmpty(movieInfo.getView_time())) {
                                int viewTime = Integer.parseInt(movieInfo.getView_time());
                                if (videoPlayer != null && viewTime != 0 && currentPosition >= (viewTime * 1000)) {
                                    videoPlayer.showTip(false);
                                }
                            }
                        }
                    })
            ;//设置视频播放回调
            gsyVideoOptionBuilder.build(videoPlayer);
            videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接横屏
                    if (getOrientationUtils() != null) {
                        getOrientationUtils().resolveByClick();
                    }

                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    videoPlayer.startWindowFullscreen(MinePlayActivity.this, false, false);
                }
            });
            //过渡动画
            initTransition();
            isPlayMovie(movieInfo);
        } else {
            ArmsUtils.makeText(this, "加载失败");
        }
    }

    void showImg(String url, ImageView imageView) {
        StringUtils.showImgView(ArmsUtils.obtainAppComponentFromContext(MinePlayActivity.this), imageView, MinePlayActivity.this, url, getStrHashCode(url));
//        ArmsUtils.obtainAppComponentFromContext(MinePlayActivity.this).imageLoader().loadImage(MinePlayActivity.this,
//                ImageConfigImpl
//                        .builder()
//                        .url(url)
//                        .imageView(imageView)
//                        .build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            if (getOrientationUtils() != null) {
                videoPlayer.onConfigurationChanged(this, newConfig, getOrientationUtils(), false, false);
            }
        } else {
            hideOld();
        }
    }

    void hideOld() {
        getSupportActionBar().hide();
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
    }


    private OrientationUtils getOrientationUtils() {
        if (videoPlayer != null) {
            if (videoPlayer.orientationUtils == null) {
                videoPlayer.orientationUtils = new OrientationUtils(this, videoPlayer);
            }
        }
        return videoPlayer == null ? null : videoPlayer.orientationUtils;
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    videoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    private void dissMissAll(View view) {
        RxView.clicks(view)
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // do something
                        if (videoPlayer != null) {
                            videoPlayer.setVideoAllCallBack(null);
                        }
                        GSYVideoManager.releaseAllVideos();
                        finish();
                    }
                });
        //释放所有

    }


    /**
     * 用户是否可以播放影片
     */
    void isPlayMovie(YunboMov movieInfo) {
        if (videoPlayer != null && movieInfo != null && !TextUtils.isEmpty(movieInfo.getView_time())) {
            int viewTime = Integer.parseInt(movieInfo.getView_time());
//            tipTitle.setText(movieInfo.getTip());
            videoPlayer.tipTitle.setText(movieInfo.getTip());
            tipSecond.setVisibility(viewTime > 0 ? View.VISIBLE : View.GONE);
            tipSecond.setText("试看" + viewTime + "秒");
            videoPlayer.showTip(viewTime >= 0);
        }
    }

    /**
     * @param isShow 是否显示提示信息
     */
    void showTip(boolean isShow) {
        if (isShow) {
            playTip.setVisibility(View.GONE);
            if (videoPlayer != null) {
                if (videoPlayer.getCurrentState() == GSYVideoView.CURRENT_STATE_PAUSE) {
                    videoPlayer.onVideoResume();
                }
                videoPlayer.setVisibility(View.VISIBLE);
            }
        } else {
            if (videoPlayer != null) {
                videoPlayer.release();
                videoPlayer.setVisibility(View.GONE);
            }
            playTip.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (videoPlayer != null) {
                videoPlayer.setVideoAllCallBack(null);
            }
            GSYVideoManager.releaseAllVideos();
            ArmsUtils.obtainAppComponentFromContext(MinePlayActivity.this).appManager().killActivity(MinePlayActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
