package com.smobile.premierleague.di

import com.smobile.premierleague.standings.StandingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeStandingsFragment(): StandingsFragment
}