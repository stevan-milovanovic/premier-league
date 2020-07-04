package com.smobile.premierleague.di

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class, NetworkModule::class, DatabaseModule::class])
class AppModule {

    @Singleton
    @Provides
    fun getSharedPrefs(app: Application): SharedPreferences {
        return EncryptedSharedPreferences.create(
            app,
            "secret_shared_prefs_palmhr",
            MasterKey.Builder(app, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun getEditor(preferences: SharedPreferences): SharedPreferences.Editor {
        return preferences.edit()
    }

}