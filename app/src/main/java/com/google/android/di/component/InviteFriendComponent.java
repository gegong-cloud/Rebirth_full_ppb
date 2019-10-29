package com.google.android.di.component;

import com.google.android.mvp.ui.activity.InviteFriendActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.InviteFriendModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = InviteFriendModule.class, dependencies = AppComponent.class)
public interface InviteFriendComponent {
    void inject(InviteFriendActivity activity);
}