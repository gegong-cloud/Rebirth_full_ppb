package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.InviteFriendContract;
import com.google.android.mvp.model.InviteFriendModel;
import dagger.Module;
import dagger.Provides;


@Module
public class InviteFriendModule {
    private InviteFriendContract.View view;

    /**
     * 构建InviteFriendModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InviteFriendModule(InviteFriendContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteFriendContract.View provideInviteFriendView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteFriendContract.Model provideInviteFriendModel(InviteFriendModel model) {
        return model;
    }
}