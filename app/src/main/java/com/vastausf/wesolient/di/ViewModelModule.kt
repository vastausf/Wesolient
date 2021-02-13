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
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.SettingsStore
import com.vastausf.wesolient.model.store.TemplateStore
import com.vastausf.wesolient.model.store.VariableStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
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
    fun providesFirebaseDatabaseUserRoot(
        firebaseDatabase: FirebaseDatabase,
        firebaseUser: FirebaseUser
    ): DatabaseReference {
        return firebaseDatabase
            .reference
            .child("USERS")
            .child(firebaseUser.uid)
    }

    @Provides
    fun providesFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    fun providesServiceCreator(): ServiceCreator {
        return ServiceCreator()
    }

    @Provides
    fun providesScopeStore(
        firebaseDatabaseUserRoot: DatabaseReference
    ): ScopeStore {
        return ScopeStore(
            firebaseDatabaseUserRoot
        )
    }

    @Provides
    fun providesSettingsStore(
        firebaseDatabaseUserRoot: DatabaseReference
    ): SettingsStore {
        return SettingsStore(
            firebaseDatabaseUserRoot
        )
    }

    @Provides
    fun providesTemplateStore(
        firebaseDatabaseUserRoot: DatabaseReference
    ): TemplateStore {
        return TemplateStore(
            firebaseDatabaseUserRoot
        )
    }

    @Provides
    fun providesVariableStore(
        firebaseDatabaseUserRoot: DatabaseReference
    ): VariableStore {
        return VariableStore(
            firebaseDatabaseUserRoot
        )
    }
}
