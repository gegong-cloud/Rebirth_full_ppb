package com.google.android.mvp.ui.widget.player;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.google.android.R;
import com.google.android.app.EventBusTags;

import org.simple.eventbus.EventBus;

/**
 * 自定义播放器ui-直播
 */
public class CustomerVideo extends StandardGSYVideoPlayer {

    //处理屏幕旋转的的逻辑
    public OrientationUtils orientationUtils;

    private TextView mCustomerFull;

    Context ct;

    public TextView tipTitle;
    TextView tipFree;
    TextView tipOpen;
    RelativeLayout playTip;


    public CustomerVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        ct = context;
    }

    public CustomerVideo(Context context) {
        super(context);
        ct = context;
    }

    public CustomerVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        ct = context;
    }

    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.customer_video_fullscreen;
        }
        return R.layout.customer_video_normal;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mBottomContainer.setBackground(ContextCompat.getDrawable(context,R.color.background_transparent));
        hideTime();
        if(mIfCurrentIsFullscreen){
            tipTitle = findViewById(R.id.tip_title);
            tipFree = findViewById(R.id.tip_free);
            tipOpen = findViewById(R.id.tip_open);
            playTip = findViewById(R.id.play_tip);

            tipFree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post("", EventBusTags.TIP_FREE);
                }
            });
            tipOpen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post("", EventBusTags.TIP_OPEN);
                }
            });
        }
    }

    /**
     * @param isShow 是否显示提示信息
     */
    public void showTip(boolean isShow) {
        if (isShow) {
//            playTip.setVisibility(View.GONE);
        } else {
            clearFullscreenLayout();
            release();
//            playTip.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getShrinkImageRes() {
        return R.drawable.video_open;
    }

    @Override
    public void setBottomProgressBarDrawable(Drawable drawable) {
        super.setBottomProgressBarDrawable(drawable);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        CustomerVideo customerVideo = (CustomerVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        customerVideo.hideFullBottomUi();
        return customerVideo;
    }

    void hideFullBottomUi() {
        findViewById(R.id.layout_bottom).setVisibility(GONE);
//        findViewById(R.id.mine_new_btn).setVisibility(VISIBLE);
//        findViewById(R.id.mine_new_btn).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearFullscreenLayout();
//            }
//        });
    }

    public void hideMovie(){
        if(mIfCurrentIsFullscreen){
            clearFullscreenLayout();
        }
        release();
    }

    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        hideTime();
    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        hideTime();
    }

    @Override
    protected void resetProgressAndTime() {
        super.resetProgressAndTime();
        hideTime();
    }

    @Override
    protected void loopSetProgressAndTime() {
        super.loopSetProgressAndTime();
        hideTime();
    }

    void hideTime() {
        mCurrentTimeTextView.setVisibility(GONE);
        mTotalTimeTextView.setVisibility(GONE);
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        CustomerVideo landLayoutVideo = (CustomerVideo) gsyVideoPlayer;
        landLayoutVideo.dismissProgressDialog();
        landLayoutVideo.dismissVolumeDialog();
        landLayoutVideo.dismissBrightnessDialog();
        EventBus.getDefault().post("", EventBusTags.QUIT_FULL);
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    @Override
    protected void onClickUiToggle() {
        super.onClickUiToggle();
        hideBottom();
    }

    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        hideBottom();
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        hideBottom();
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        hideBottom();
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        hideBottom();
    }

    void hideBottom() {
        if(mIfCurrentIsFullscreen&&mBottomContainer!=null){
            mBottomContainer.setVisibility(VISIBLE);
            mBottomContainer.findViewById(R.id.current).setVisibility(INVISIBLE);
            mBottomContainer.findViewById(R.id.progress).setVisibility(INVISIBLE);
            mBottomContainer.findViewById(R.id.total).setVisibility(INVISIBLE);
            mBottomContainer.findViewById(R.id.switchSize).setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onError(int what, int extra) {//视频出错
        super.onError(what, extra);
    }

    @Override
    public void onCompletion() {//视频成功
        super.onCompletion();
    }

    public TextView getmCustomerFull() {
        return mCustomerFull;
    }
}
