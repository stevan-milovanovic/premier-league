package com.smobile.premierleague.di

import android.app.Application
import androidx.room.Room
import com.smobile.premierleague.Const
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.PlayerDao
import com.smobile.premierleague.db.StandingDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): LeagueDb {
        return Room.databaseBuilder(app, LeagueDb::class.java, Const.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStandingDao(db: LeagueDb): StandingDao = db.standingDao()

    @Singleton
    @Provides
    fun providesPlayerDao(db: LeagueDb): PlayerDao = db.playerDao()

}