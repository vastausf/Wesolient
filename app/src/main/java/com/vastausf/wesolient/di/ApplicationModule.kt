package com.vastausf.wesolient.di

import com.vastausf.wesolient.Wesolient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    fun providesWesolient(): Wesolient {
        return Wesolient()
    }
}
