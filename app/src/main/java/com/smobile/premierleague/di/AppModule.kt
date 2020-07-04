package com.smobile.premierleague.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class, NetworkModule::class, DatabaseModule::class])
class AppModule {

    @Singleton
    @Provides
    fun getSharedPrefs(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Singleton
    @Provides
    fun getEditor(preferences: SharedPreferences): SharedPreferences.Editor {
        return preferences.edit()
    }

}