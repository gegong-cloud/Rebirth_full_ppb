package com.google.android.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerVipQrcodeComponent;
import com.google.android.di.module.VipQrcodeModule;
import com.google.android.mvp.contract.VipQrcodeContract;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.presenter.VipQrcodePresenter;
import com.google.android.mvp.ui.widget.downtimer.DownTimer;
import com.google.android.mvp.ui.widget.downtimer.DownTimerListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.google.android.app.utils.StringUtils.openWeixinToQE_Code;

/**
 * 二维码vip充值
 */
public class VipQrcodeActivity extends BaseActivity<VipQrcodePresenter> implements VipQrcodeContract.View, DownTimerListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.vip_code_card)
    TextView vipCodeCard;
    @BindView(R.id.vip_code_ordernumber)
    TextView vipCodeOrdernumber;
    @BindView(R.id.pay_money)
    TextView payMoney;
    @BindView(R.id.pay_type)
    TextView payType;
    @BindView(R.id.vip_qrcode)
    ImageView vipQrcode;
    @BindView(R.id.service_name)
    TextView serviceName;
    @BindView(R.id.pay_type_bg)
    LinearLayout payTypeBg;
    @BindView(R.id.pay_all_bg)
    LinearLayout payAllBg;

    MoneyShow showQrcode;
    @BindView(R.id.order_time)
    TextView orderTime;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVipQrcodeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .vipQrcodeModule(new VipQrcodeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_vip_qrcode; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        showQrcode = (MoneyShow) getIntent().getSerializableExtra("showQrcode");
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("VIP充值");
        if(showQrcode!=null){

            mPresenter.downImageToLocation(showQrcode.getUrl());
            beginDownTime();
            initInfo();
        }
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
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if(downTimer!=null){
            downTimer.stopDown();
            downTimer = null;
        }
        EventBus.getDefault().unregister(this);
    }


    @Subscriber(tag = EventBusTags.CAST_PAY_RESULT)
    public void castPayResult(String event) {
        complete();
    }


    @OnClick({R.id.toolbar_back, R.id.save_qrcode, R.id.complete_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                userBackPopup();
                break;
            case R.id.save_qrcode:
                saveQrcode(VipQrcodeActivity.this, payAllBg);
                break;
            case R.id.complete_pay:
                if (showQrcode != null && !TextUtils.isEmpty(showQrcode.getOrderNumber())) {
                    mPresenter.queryResult(showQrcode);
                }
                break;
        }
    }


    void initInfo() {
        if (showQrcode != null) {
            vipCodeCard.setText("您正在购买的是:" + (!TextUtils.isEmpty(showQrcode.getCardName()) ? showQrcode.getCardName() : "月卡"));
            vipCodeOrdernumber.setText("订单编号:" + (!TextUtils.isEmpty(showQrcode.getOrderNumber()) ? showQrcode.getOrderNumber() : ""));
            payMoney.setText("¥:" + (!TextUtils.isEmpty(showQrcode.getMoney()) ? showQrcode.getMoney() : ""));
            payType.setText("推荐使用:" + ("wx".equals(showQrcode.getCode()) ? "微信支付" : "支付宝"));
//            payType.setText("推荐使用:" + ("wx".equals(showQrcode.getCode()) ? "微信支付" : "支付宝"));
//            showImg(!TextUtils.isEmpty(showQrcode.getUrl()) ? showQrcode.getUrl() : "");
            payTypeBg.setBackground("wx".equals(showQrcode.getCode()) ? ContextCompat.getDrawable(this, R.color.pay_bg_wx) : ContextCompat.getDrawable(this, R.color.pay_bg_ali));
            payTypeBg.setVisibility(View.VISIBLE);
//            serviceName.setText((!TextUtils.isEmpty(showQrcode.getAccountno()) ? showQrcode.getAccountno() : ""));
        }
    }

    @Override
    public void showQrImg(String qrPath) {
        showImg(qrPath);
    }

    void showImg(String url) {
        ArmsUtils.obtainAppComponentFromContext(VipQrcodeActivity.this).imageLoader().loadImage(VipQrcodeActivity.this,
                ImageConfigImpl
                        .builder()
                        .url( url)
                        .imageView(vipQrcode)
                        .build());
    }


    public void saveQrcode(Activity activity, LinearLayout relativeLayout) {
        savePic2Phone(activity, getCacheBitmapFromView(relativeLayout), relativeLayout);
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(LinearLayout view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        return drawingCache;
    }

    private void savePic2Phone(Context context, Bitmap bmp, LinearLayout view) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "mans");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        view.setDrawingCacheEnabled(false);
        ArmsUtils.makeText(VipQrcodeActivity.this, "保存成功,正在打开微信扫一扫...");
        openWeixinToQE_Code(VipQrcodeActivity.this);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(2==(millisUntilFinished/1000)){
            if (showQrcode != null && !TextUtils.isEmpty(showQrcode.getOrderNumber())) {
                if(mPresenter!=null){
                    mPresenter.queryResult(showQrcode);
                }
            }
        }
        if(orderTime!=null){
            orderTime.setText("订单有效时间:"+(millisUntilFinished/1000)+"秒");
        }
    }

    @Override
    public void onFinish() {
        complete();
    }


    DownTimer downTimer;
    void beginDownTime() {
        downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(120 * 1000);
    }


    @Override
    public void complete() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("showQrcode",showQrcode);
        launchActivity(new Intent(this, OfflineRechargeActivity.class)
                .putExtras(bundle)
        );
        killMyself();
    }



    @Override
    public void onBackPressed() {
        userBackPopup();
        return;
    }

    void userBackPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("退出后将取消当前未完成的订单,是否继续？");//提示内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.unlockMoney(showQrcode);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
