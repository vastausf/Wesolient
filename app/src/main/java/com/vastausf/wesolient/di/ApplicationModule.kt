package com.vastausf.wesolient.di

import com.vastausf.wesolient.Wesolient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    fun providesWesolient(): Wesolient {
        return Wesolient()
    }
}
