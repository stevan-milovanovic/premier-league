package com.smobile.premierleague.di

import android.app.Application
import androidx.room.Room
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.db.LeagueDb
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
    fun providePremierLeagueService(): LeagueService {
        val clientBuilder = OkHttpClient.Builder()
        val headerAuthorizationInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            val headers: Headers =
                request.headers().newBuilder()
                    .add(
                        "x-rapidapi-key",
                        "63284ca007msh15dbd714e326824p1120e4jsn99bdcaf89bea"
                    )
                    .add(
                        "x-rapidapi-host",
                        "api-football-v1.p.rapidapi.com"
                    ).build()
            request = request.newBuilder().headers(headers).build()
            chain.proceed(request)
        }
        clientBuilder.addInterceptor(headerAuthorizationInterceptor)

        return Retrofit.Builder()
            .baseUrl("https://api-football-v1.p.rapidapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(clientBuilder.build())
            .build()
            .create(LeagueService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): LeagueDb {
        return Room.databaseBuilder(app, LeagueDb::class.java, "league.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStandingDao(db: LeagueDb): StandingDao {
        return db.standingDao()
    }
}