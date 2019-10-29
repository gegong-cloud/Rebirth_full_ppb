package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.di.component.DaggerVipVideoComponent;
import com.google.android.di.module.VipVideoModule;
import com.google.android.mvp.contract.VipVideoContract;
import com.google.android.mvp.model.back_entity.VIPVideoEntity;
import com.google.android.mvp.presenter.VipVideoPresenter;
import com.yanzhenjie.sofia.StatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * vip影视界面
 */
public class VipVideoActivity extends BaseActivity<VipVideoPresenter> implements VipVideoContract.View, View.OnKeyListener {

    @BindView(R.id.status_view)
    StatusView statusView;
    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.webview_adv)
    WebView webviewAdv;
    @BindView(R.id.content_container)
    LinearLayout contentContainer;
    @BindView(R.id.bt_vippaly)
    Button btVippaly;
    @BindView(R.id.webviewRelativeLayout)
    RelativeLayout webviewRelativeLayout;

    /**
     * 影院地址
     */
    String baseUrl = "";

    /**
     * 影片地址
     */
    String video_url = "";


    List<String> urlList = new ArrayList<>();


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVipVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .vipVideoModule(new VipVideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vip_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("VIP影院");
        btVippaly.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);

        baseUrl = getIntent().getStringExtra("baseUrl");
        if (mPresenter != null) {
            mPresenter.tvVip(getIntent().getStringExtra("vip_cate_id"));
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
        if (webviewAdv != null) {
            try {
                webviewAdv.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        try {
            if (webviewAdv != null) {
                webviewRelativeLayout.removeView(webviewAdv);
                webviewAdv.removeAllViews();
                webviewAdv.setWebChromeClient(null);
                webviewAdv.setWebViewClient(null);
                webviewAdv.destroy();
                webviewAdv = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.toolbar_back, R.id.bt_vippaly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.bt_vippaly:
                if (TextUtils.isEmpty(video_url)) {
                    ArmsUtils.makeText(this, "请打开视频播放页");
                    return;
                }
                break;
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (webviewAdv.canGoBack()) {
                    webviewAdv.goBack();
                } else {
                    finish();
                    if (webviewAdv != null) {
                        try {
                            webviewAdv.destroy();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showWebView(VIPVideoEntity vipVideoEntity) {
        if (vipVideoEntity != null) {
            urlList = vipVideoEntity.getTv_vip_interface();
            WebSettings webSettings = webviewAdv.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webSettings.setDomStorageEnabled(true);
            contentContainer.setVisibility(View.VISIBLE);
            initVipBtContaniner(vipVideoEntity);
            webviewAdv.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress > 99) {
                        if (toolbarRight != null) {
                            toolbarRight.setVisibility(View.INVISIBLE);
                        }
                        /*  bt_vippaly.setVisibility(View.VISIBLE);*/


                    } else {
                        if (toolbarRight != null) {
                            toolbarRight.setVisibility(View.VISIBLE);
                            toolbarRight.setText(newProgress + "%");
                        }
                    }
                }
            });
            webviewAdv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                    /tbv_view.setTv_title_text(TextUtils.isEmpty(view.getTitle())?"加载中":view.getTitle());
                    if ((url.startsWith("http") || url.startsWith("https")) && !TextUtils.isEmpty(url)) {
                        // video_url = checkUrl(vipVideoEntity.data.tv_vip_link, url);
                        video_url = url;
                        view.loadUrl(url);
                    }
                    return true;
                }
            });
            //添加下载监听
            webviewAdv.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            webviewAdv.loadUrl(baseUrl);
            webviewAdv.setOnKeyListener(VipVideoActivity.this);
        }
    }


    private void initVipBtContaniner(VIPVideoEntity vipVideoEntity) {
        for (int i = 0; i < urlList.size(); i++) {
            if(!(vipVideoEntity.getTv_vip_interface()!=null&&!TextUtils.isEmpty(vipVideoEntity.getTv_vip_interface().get(i)))){
                return;//判断返回vip地址为空时，不添加
            }
            LinearLayout containItemLayout = (LinearLayout) LayoutInflater.from(VipVideoActivity.this).inflate(R.layout.vippay_bt_container, contentContainer, false);
            contentContainer.addView(containItemLayout);
            Button vipBt = (Button) containItemLayout.getChildAt(0);
            final int finalI = i;
            vipBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ToastUtils.showToast(ActivityWebViewVipVideo.this, finalI + "");
                    String video_url=getVideoUrl();
                    if (TextUtils.isEmpty(video_url)) {
                        ArmsUtils.makeText(VipVideoActivity.this, "请打开视频播放页");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("vip_url", vipVideoEntity.getTv_vip_interface().get(finalI) + (video_url));
                    launchActivity(new Intent(VipVideoActivity.this, VipVideoPlayerActivity.class)
                            .putExtras(bundle)
                    );
                }
            });
        }
    }


    private String getVideoUrl(){
        if(webviewAdv==null){
            return "";
        }
        String video_url=webviewAdv.getUrl();
        if (TextUtils.isEmpty(video_url)) {
            return "";
        }
        if ((!video_url.startsWith("http")
                &&!video_url.startsWith("https")) ) {
            return "";
        }
        if(video_url.indexOf(".apk")!=-1){
            return "";
        }
        if(video_url.equals(baseUrl)){
            return "";
        }
        return video_url;
    }


}
