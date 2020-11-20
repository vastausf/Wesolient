package com.vastausf.wesolient.di

import com.vastausf.wesolient.model.ServiceCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {
    @Provides
    fun providesServiceCreator(): ServiceCreator {
        return ServiceCreator()
    }
}
