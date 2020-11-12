package com.vastausf.wesolient.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tinder.scarlet.Scarlet
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.ScopeWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun providesFirebaseUser(): FirebaseUser {
        return Firebase.auth.currentUser!!
    }

    @Provides
    @Singleton
    fun providesFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    @Singleton
    fun providesFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesScopeStore(
        databaseReference: FirebaseDatabase,
        firebaseUser: FirebaseUser
    ): ScopeStore {
        return ScopeStore(databaseReference, firebaseUser)
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
