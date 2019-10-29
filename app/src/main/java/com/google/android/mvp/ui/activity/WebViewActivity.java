package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.gyf.barlibrary.ImmersionBar;
import com.google.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.webview_adv)
    WebView webviewAdv;

    String url = "";
    String titleStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
        titleStr = getIntent().getStringExtra("titleStr");
        url = getIntent().getStringExtra("url");
        initWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (webviewAdv != null) {
            webviewAdv.removeAllViews();
            try {
                webviewAdv.destroy();
            } catch (Throwable t) {
            }
            webviewAdv = null;
        }
    }


    void initWebView() {
        Timber.e("lhw---url===" + url);
        initSet();
        toolbarTitle.setText(!TextUtils.isEmpty(titleStr) ? titleStr : "");
        if ("VIP充值".equals(titleStr)) {
            //微信支付
//            if (getIntent().getStringExtra("payType").startsWith("微信")) {
//
//            }else{
            webviewAdv.setWebViewClient(new MyWebViewClient());
//            }
        } else {
            webviewAdv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if ((url.startsWith("http") || url.startsWith("https")) && !TextUtils.isEmpty(url)) {
                        view.loadUrl(url);
                    }
                    return true;
                }
            });
        }
        webviewAdv.loadUrl(url);
    }


    /**
     * 基本设置
     */
    void initSet() {
        WebSettings webSettings = webviewAdv.getSettings();
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(false);
        webviewAdv.setVerticalScrollbarOverlay(true);
        webviewAdv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress > 99) {
                    if (toolbarRight != null) {
                        toolbarRight.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (toolbarRight != null) {
                        toolbarRight.setVisibility(View.VISIBLE);
                        toolbarRight.setText(newProgress + "%");
                    }
                }
            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }
            //微信支付
            if (getIntent().getStringExtra("payType").startsWith("微信")) {
                //微信H5支付不做客户端集成
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
//                view.loadUrl(url);
                return true;
            }

            /**
             * 支付宝支付
             * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
             */
            final PayTask task = new PayTask(WebViewActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    final String url = result.getReturnUrl();
                    if (!TextUtils.isEmpty(url)) {
                        WebViewActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl(url);
                            }
                        });
                    }
                }
            });

            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!isIntercepted) {
                view.loadUrl(url);
            }
            return true;
        }
    }
}
