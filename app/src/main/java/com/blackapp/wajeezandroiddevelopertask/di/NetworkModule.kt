package com.blackapp.wajeezandroiddevelopertask.di

import com.blackapp.wajeezandroiddevelopertask.data.remote.repository.WajeezRepositoryImpl
import com.blackapp.wajeezandroiddevelopertask.domain.datasource.remote.WajeezRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideWajeezRepository(): WajeezRepository {
        return WajeezRepositoryImpl()
    }

}