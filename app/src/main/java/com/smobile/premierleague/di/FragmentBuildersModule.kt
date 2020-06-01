package com.smobile.premierleague.di

import com.smobile.premierleague.headtohead.HeadToHeadFragment
import com.smobile.premierleague.standings.StandingsFragment
import com.smobile.premierleague.team.TeamFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeStandingsFragment(): StandingsFragment

    @ContributesAndroidInjector
    abstract fun contributeTeamFragment(): TeamFragment

    @ContributesAndroidInjector
    abstract fun contributeHeadToHeadFragment(): HeadToHeadFragment
}