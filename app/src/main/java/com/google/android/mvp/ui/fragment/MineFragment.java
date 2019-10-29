package com.google.android.mvp.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.google.android.app.utils.TimeUtils;
import com.google.android.di.component.DaggerMineComponent;
import com.google.android.di.module.MineModule;
import com.google.android.mvp.contract.MineContract;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.presenter.MinePresenter;
import com.google.android.mvp.ui.activity.MyHistoryActivity;
import com.google.android.mvp.ui.activity.MyLikeMovieActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {

    @BindView(R.id.mine_id)
    TextView mineId;
    @BindView(R.id.mine_head)
    CircleImageView mineHead;
    @BindView(R.id.mine_not_logo)
    RelativeLayout mineNotLogo;
    @BindView(R.id.mine_nickname)
    TextView mineNickname;
    @BindView(R.id.mine_yunbo_date)
    TextView mineYunboDate;
    @BindView(R.id.mine_zhibo_date)
    TextView mineZhiboDate;
    @BindView(R.id.user_info)
    LinearLayout userInfo;
    @BindView(R.id.mine_apprentice)
    TextView mineApprentice;
    Unbinder unbinder;
    @BindView(R.id.mine_menu_top)//四个菜单
            LinearLayout mineMenutop;
    @BindView(R.id.mine_menu1_line)
    View mineMenu1Line;
    @BindView(R.id.mine_wallet)
    LinearLayout mineWallet;
    @BindView(R.id.mine_invite1)
    LinearLayout mineInvite1;
    @BindView(R.id.mine_accagent)
    LinearLayout mineAccagent;
    @BindView(R.id.mine_makemoney)
    LinearLayout mineMakemoney;
    @BindView(R.id.mine_team)
    LinearLayout mineTeam;
    @BindView(R.id.mine_invite)
    LinearLayout mineInvite;
    @BindView(R.id.mine_pwd)
    LinearLayout minePwd;
    @BindView(R.id.mine_set)
    LinearLayout mineSet;
    @BindView(R.id.mine_invite_line)
    View mineInviteLine;
    @BindView(R.id.mine_set_line)
    View mineSetLine;
    @BindView(R.id.line_df1)
    View lineDf1;
    @BindView(R.id.recharge_details)
    TextView rechargeDetails;
    @BindView(R.id.call_service_text)
    TextView callServiceText;
    @BindView(R.id.invite1_text)
    TextView invite1Text;
    @BindView(R.id.invite_text)
    TextView inviteText;
    @BindView(R.id.mine_recharge_text)
    TextView mineRechargeText;
    @BindView(R.id.mine_wallet_text)
    TextView mineWalletText;
    @BindView(R.id.mine_team_text)
    TextView mineTeamText;
    @BindView(R.id.mine_set_text)
    TextView mineSetText;

    //进度框
    private MaterialDialog materialDialog;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mineModule(new MineModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(getActivity()).content("正在加载...").progress(true, 0).build();
        if (!HbCodeUtils.isEmptyToken() && mPresenter != null) {
            mPresenter.userInfo();
        } else {
            loadUserInfo(null);
        }

    }

    /**
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
//        materialDialog.show();
    }

    @Override
    public void hideLoading() {
//        materialDialog.dismiss();

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
//        ImmersionBar.with(this).statusBarColorTransformEnable(false)
//                .keyboardEnable(false)
//                .navigationBarColor(R.color.background_transparent)
//                .init();
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ImmersionBar.with(this).destroy();
        unbinder.unbind();
    }

    int getNet = 0;

    @Override
    public void onResume() {
        super.onResume();
        if (getNet > 0) {
            if (!HbCodeUtils.isEmptyToken() && mPresenter != null) {
                mPresenter.userInfo();
            }
        }
        getNet++;
    }

    @OnClick({R.id.recharge_details, R.id.mine_not_logo, R.id.mine_menu1, R.id.mine_menu2, R.id.mine_menu3, R.id.mine_makemoney, R.id.mine_menu4, R.id.mine_accagent, R.id.mine_recharge, R.id.mine_wallet, R.id.mine_team, R.id.mine_invite, R.id.mine_invite1, R.id.mine_pwd, R.id.mine_set, R.id.mine_exit, R.id.mine_call_service, R.id.mine_favor, R.id.mine_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge_details://充值明细
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.recharge_details, getActivity());
                }
                break;
            case R.id.mine_not_logo:
//                launchActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.mine_menu1://收支明细
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_menu1, getActivity());
                }
                break;
            case R.id.mine_menu2://分销提成
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_menu2, getActivity());
                }
                break;
            case R.id.mine_menu3://如何赚钱
            case R.id.mine_makemoney://如何赚钱
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_makemoney, getActivity());
                }
                break;
            case R.id.mine_menu4://加入代理
            case R.id.mine_accagent://加入代理
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_accagent, getActivity());
                }
                break;
            case R.id.mine_recharge://vip充值
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_recharge, getActivity());
                }
                break;
            case R.id.mine_wallet://我的钱包
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_wallet, getActivity());
                }
                break;
            case R.id.mine_team://我的徒弟
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_team, getActivity());
                }
                break;
            case R.id.mine_invite://邀请好友
            case R.id.mine_invite1://邀请好友赚钱
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_invite, getActivity());
                }
                break;
            case R.id.mine_pwd://修改密码
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_pwd, getActivity());
                }
                break;
            case R.id.mine_set:
                if (mPresenter != null) {
                    mPresenter.checkUser(R.id.mine_set, getActivity());
                }
                break;
            case R.id.mine_exit:
                if (!TextUtils.isEmpty(HbCodeUtils.getToken())) {
                    ExitLogin();
                } else {
                    ArmsUtils.makeText(getActivity(), "请先登录！");
                }
                break;
            case R.id.mine_call_service://在线客服
                EventBus.getDefault().post("service", EventBusTags.CALL_SERVICE);
                break;
            case R.id.mine_favor://收藏
                launchActivity(new Intent(getActivity(), MyLikeMovieActivity.class));
                break;
            case R.id.mine_history://历史
                launchActivity(new Intent(getActivity(), MyHistoryActivity.class));
                break;
        }
    }


    /**
     * 退出登录
     */
    void ExitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否退出登录？");//提示内容
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.logout();
                userNotLogin();
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


    @Override
    public void loadUserInfo(UserEntity userEntity) {
        if (userEntity != null) {//已经登录了
            mineNotLogo.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
            rechargeDetails.setVisibility(View.VISIBLE);
//            mineId.setText(!TextUtils.isEmpty(userEntity.getNickname_code())?"ID："+userEntity.getNickname_code():"");
            mineId.setText(!TextUtils.isEmpty(userEntity.getUid()) ? "用户ID：" + userEntity.getUid() : "");
            mineNickname.setText(!TextUtils.isEmpty(userEntity.getNickname()) ? "昵称：" + userEntity.getNickname() : "");
            String yunboDate = "云播到期时间:" + (!TextUtils.isEmpty(userEntity.getYunbo_end_time()) ? TimeUtils.timeFormat(Long.parseLong(userEntity.getYunbo_end_time().toString()), "yyyy-MM-dd HH:mm") : "");
            String zhiboDate = "直播到期时间:" + (!TextUtils.isEmpty(userEntity.getZhibo_end_time()) ? TimeUtils.timeFormat(Long.parseLong(userEntity.getZhibo_end_time().toString()), "yyyy-MM-dd HH:mm") : "");
            mineYunboDate.setText(yunboDate);
            mineZhiboDate.setText(zhiboDate);

        } else {
            userNotLogin();
        }
    }

    @Override
    public void isAgent(boolean isAgent) {
        //代理版本只显示简单的菜单，不包含团队、钱包、收支等
        mineMenutop.setVisibility(isAgent ? View.GONE : View.VISIBLE);//顶部四个菜单
        mineMenu1Line.setVisibility(isAgent ? View.GONE : View.VISIBLE);//顶部四个菜单
        mineWallet.setVisibility(isAgent ? View.GONE : View.VISIBLE);//我的钱包
        mineInvite.setVisibility(isAgent ? View.GONE : View.VISIBLE);//我的团队
        mineSet.setVisibility(isAgent ? View.GONE : View.VISIBLE);//我的团队
        mineInviteLine.setVisibility(isAgent ? View.GONE : View.VISIBLE);//我的团队
        mineSetLine.setVisibility(isAgent ? View.GONE : View.VISIBLE);//我的团队

        mineTeam.setVisibility(isAgent ? View.VISIBLE : View.VISIBLE);//我的团队
        lineDf1.setVisibility(isAgent ? View.VISIBLE : View.VISIBLE);//邀请好友赚钱
        mineInvite1.setVisibility(isAgent ? View.VISIBLE : View.GONE);//邀请好友赚钱
        mineAccagent.setVisibility(isAgent ? View.VISIBLE : View.GONE);//加入代理

        mineMakemoney.setVisibility(isAgent ? View.GONE : View.GONE);//如何赚钱
    }

    @Override
    public void bindTip(UserEntity userEntity) {
        if (userEntity.getMenutip() != null) {
            callServiceText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getService_tip()) ? userEntity.getMenutip().getService_tip() : "");
            invite1Text.setText(!TextUtils.isEmpty(userEntity.getMenutip().getInvate_tip()) ? userEntity.getMenutip().getInvate_tip() : "");
            inviteText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getInvate_tip()) ? userEntity.getMenutip().getInvate_tip() : "");
            mineRechargeText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getCharge_tip()) ? userEntity.getMenutip().getCharge_tip() : "");
            mineWalletText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getMybollet_tip()) ? userEntity.getMenutip().getMybollet_tip() : "");
            mineTeamText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getMyteam_tip()) ? userEntity.getMenutip().getMyteam_tip() : "");
            mineSetText.setText(!TextUtils.isEmpty(userEntity.getMenutip().getSet_tip()) ? userEntity.getMenutip().getSet_tip() : "");
        }
    }

    /**
     * 用户未登录
     */
    @Override
    public void userNotLogin() {
        mineNickname.setText("");
        mineYunboDate.setText("");
        mineZhiboDate.setText("");
        mineId.setText("");
        mineNotLogo.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.GONE);
        rechargeDetails.setVisibility(View.GONE);
        mineId.setText("");
        isAgent(true);
    }

    private String getUserId() {
        if (mPresenter != null && mPresenter.getUserEntity() != null && !TextUtils.isEmpty(mPresenter.getUserEntity().getUid())) {
            return "?userId=" + mPresenter.getUserEntity().getUid();
        } else {
            return "";
        }
    }
}
