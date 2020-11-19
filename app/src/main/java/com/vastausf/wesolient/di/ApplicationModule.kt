package com.vastausf.wesolient.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tinder.scarlet.Scarlet
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.vastausf.wesolient.model.ScopeStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    fun providesFirebaseUser(
        firebaseAuth: FirebaseAuth
    ): FirebaseUser {
        return firebaseAuth.currentUser!!
    }

    @Provides
    fun providesFirebaseDatabase(): FirebaseDatabase {
        return Firebase
            .database
    }

    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun providesFirebaseDatabaseScopes(
        firebaseDatabase: FirebaseDatabase,
        firebaseUser: FirebaseUser
    ): DatabaseReference {
        return firebaseDatabase
            .reference
            .child("USERS")
            .child(firebaseUser.uid)
            .child("SCOPES")
    }

    @Provides
    fun providesFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    fun providesScopeStore(
        firebaseDatabaseScopes: DatabaseReference
    ): ScopeStore {
        return ScopeStore(firebaseDatabaseScopes)
    }

    @Provides
    fun providesSocketService(): Scarlet.Builder {
        return Scarlet
            .Builder()
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
    }
}
