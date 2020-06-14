package com.smobile.premierleague.util

import com.smobile.premierleague.AppExecutors
import java.util.concurrent.Executor

/**
 * App executors for unit tests
 */
class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}