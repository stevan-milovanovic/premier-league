package com.smobile.premierleague.di

import com.smobile.premierleague.Const
import com.smobile.premierleague.api.LeagueService
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

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideInterceptor() = Interceptor { chain ->
        var request: Request = chain.request()
        val headers: Headers =
            request.headers.newBuilder()
                .add(Const.API_KEY_HEADER, Const.API_KEY_HEADER_VALUE)
                .add(Const.API_HOST_HEADER, Const.API_HOST_HEADER_VALUE)
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
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(LeagueService::class.java)
    }

}