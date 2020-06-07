package com.smobile.premierleague.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.smobile.premierleague.Const.API_HOST_HEADER
import com.smobile.premierleague.Const.API_HOST_HEADER_VALUE
import com.smobile.premierleague.Const.API_KEY_HEADER
import com.smobile.premierleague.Const.API_KEY_HEADER_VALUE
import com.smobile.premierleague.Const.BASE_URL
import com.smobile.premierleague.Const.DB_NAME
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.db.StandingDao
import com.smobile.premierleague.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideInterceptor() = Interceptor { chain ->
        var request: Request = chain.request()
        val headers: Headers =
            request.headers().newBuilder()
                .add(API_KEY_HEADER, API_KEY_HEADER_VALUE)
                .add(API_HOST_HEADER, API_HOST_HEADER_VALUE)
                .build()
        request = request.newBuilder().headers(headers).build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun providePremierLeagueService(client: OkHttpClient): LeagueService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(LeagueService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): LeagueDb {
        return Room.databaseBuilder(app, LeagueDb::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStandingDao(db: LeagueDb): StandingDao = db.standingDao()

    @Singleton
    @Provides
    fun providesPlayerDao(db: LeagueDb): PlayerDao = db.playerDao()

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