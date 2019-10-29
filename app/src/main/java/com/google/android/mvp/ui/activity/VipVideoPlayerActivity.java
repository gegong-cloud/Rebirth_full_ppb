package com.google.android.mvp.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.R;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 播放界面
 */
public class VipVideoPlayerActivity extends AppCompatActivity implements View.OnKeyListener {

    @BindView(R.id.webview_adv)
    WebView webView;
    @BindView(R.id.webviewRelativeLayout)
    RelativeLayout relativeLayout;

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private android.webkit.WebChromeClient.CustomViewCallback customViewCallback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vip_video_player);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /** 展示网页界面 **/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initWebView() {
        Sofia.with(this)
                .statusBarLightFont()
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        setStatusBarVisibility(true);
        String vip_url = getIntent().getStringExtra("vip_url");
        android.webkit.WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        android.webkit.WebViewClient wvc = new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                if ((url.startsWith("http") || url.startsWith("https")) && !TextUtils.isEmpty(url)) {
                    webView.loadUrl(url);
                }
                return false;
            }
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                VipVideoPlayerActivity.this.setTitle(view.getTitle());
            }
        };


        webView.setWebViewClient(wvc);

        webView.setWebChromeClient(new android.webkit.WebChromeClient() {

            /*** 视频播放相关的方法 **/

            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(VipVideoPlayerActivity.this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//播放时横屏幕，如果需要改变横竖屏，只需该参数就行了

            }
            @Override
            public void onHideCustomView() {
                hideCustomView();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不播放时竖屏
            }
        });

        // 加载Web地址
        webView.loadUrl(vip_url);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, android.webkit.WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        setStatusBarVisibility(false);
        VipVideoPlayerActivity.this.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        customViewCallback = callback;
        webView.setVisibility( View.INVISIBLE);

    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        Sofia.with(this)
                .statusBarLightFont()//深色字体
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }



    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                    if (webView != null) {
                        try {
                            webView.destroy();
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        relativeLayout.removeView(webView);
        webView.removeAllViews();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView = null;
    }

}
