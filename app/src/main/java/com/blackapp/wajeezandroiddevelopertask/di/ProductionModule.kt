package com.blackapp.wajeezandroiddevelopertask.di

import com.blackapp.wajeezandroiddevelopertask.common.util.RVItemDecoration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideRVItemDecoration(): RVItemDecoration {
        return RVItemDecoration(10)
    }



}

