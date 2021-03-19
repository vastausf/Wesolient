package com.vastausf.wesolient.di

import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.store.scope.ScopeStore
import com.vastausf.wesolient.model.store.scope.ScopeStoreRealm
import com.vastausf.wesolient.model.store.settings.SettingsStore
import com.vastausf.wesolient.model.store.settings.SettingsStoreRealm
import com.vastausf.wesolient.model.store.template.TemplateStore
import com.vastausf.wesolient.model.store.template.TemplateStoreRealm
import com.vastausf.wesolient.model.store.vatiable.VariableStore
import com.vastausf.wesolient.model.store.vatiable.VariableStoreRealm
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.realm.Realm
import io.realm.RealmConfiguration

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun providesServiceCreator(): ServiceCreator {
        return ServiceCreator()
    }

    @Provides
    fun providesRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration
            .Builder()
            .build()
    }

    @Provides
    fun providesRealm(
        realmConfiguration: RealmConfiguration
    ): Realm {
        return Realm.getInstance(realmConfiguration)
    }

    @Provides
    fun providesScopeStore(
        realm: Realm
    ): ScopeStore {
        return ScopeStoreRealm(realm)
    }

    @Provides
    fun providesSettingsStore(
        realm: Realm
    ): SettingsStore {
        return SettingsStoreRealm(realm)
    }

    @Provides
    fun providesTemplateStore(
        realm: Realm
    ): TemplateStore {
        return TemplateStoreRealm(realm)
    }

    @Provides
    fun providesVariableStore(
        realm: Realm
    ): VariableStore {
        return VariableStoreRealm(realm)
    }
}
