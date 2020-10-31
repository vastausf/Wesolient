package com.vastausf.wesolient.di

import com.tinder.scarlet.Scarlet
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.ScopeWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.realm.Realm
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun providesScopeStore(realm: Realm): ScopeStore {
        return ScopeStore(realm)
    }

    @Provides
    @Singleton
    fun providesRealm(): Realm {
        return Realm
            .getDefaultInstance()
    }

    @Provides
    @Singleton
    fun providesScopeWorker(): ScopeWorker {
        return ScopeWorker()
    }

    @Provides
    @Singleton
    fun providesSocketService(): Scarlet.Builder {
        return Scarlet
            .Builder()
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
    }
}
