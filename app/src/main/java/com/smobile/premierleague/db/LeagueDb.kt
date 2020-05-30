package com.smobile.premierleague.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smobile.premierleague.model.Standing

/**
 * Application database
 */
@Database(
    entities = [
        Standing::class],
    version = 3,
    exportSchema = false
)
abstract class LeagueDb : RoomDatabase() {

    abstract fun standingDao(): StandingDao

}