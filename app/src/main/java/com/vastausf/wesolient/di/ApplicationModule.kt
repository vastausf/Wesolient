package com.vastausf.wesolient.di

import com.vastausf.wesolient.model.DesktopDataStore
import com.vastausf.wesolient.model.RealmDesktopDataStore
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
    fun providesDataStore(realm: Realm): DesktopDataStore {
        return RealmDesktopDataStore(realm)
    }

    @Provides
    @Singleton
    fun providesRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}
