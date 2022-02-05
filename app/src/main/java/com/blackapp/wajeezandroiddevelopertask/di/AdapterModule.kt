package com.blackapp.wajeezandroiddevelopertask.di

import com.blackapp.wajeezandroiddevelopertask.adapter.UserListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AdapterModule {

    @Provides
    fun provideUserListAdapter(): UserListAdapter {
        return UserListAdapter()
    }


}