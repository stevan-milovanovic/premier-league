package com.smobile.premierleague.di

import android.app.Application
import androidx.room.Room
import com.smobile.premierleague.Const
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.db.StandingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(app: Application): LeagueDb =
        Room.databaseBuilder(app, LeagueDb::class.java, Const.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideStandingDao(db: LeagueDb): StandingDao = db.standingDao()

    @Provides
    @Singleton
    fun providesPlayerDao(db: LeagueDb): PlayerDao = db.playerDao()

}