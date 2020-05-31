package com.smobile.premierleague.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.model.Standing

/**
 * Application database
 */
@Database(
    entities = [
        Standing::class,
        Player::class],
    version = 5,
    exportSchema = false
)
abstract class LeagueDb : RoomDatabase() {

    abstract fun standingDao(): StandingDao

    abstract fun playerDao(): PlayerDao

}