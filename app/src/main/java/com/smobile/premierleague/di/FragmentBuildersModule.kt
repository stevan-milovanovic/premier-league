package com.smobile.premierleague.di

import com.smobile.premierleague.ui.headtohead.HeadToHeadFragment
import com.smobile.premierleague.ui.settings.SettingsFragment
import com.smobile.premierleague.ui.standings.StandingsFragment
import com.smobile.premierleague.ui.team.TeamFragment
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

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}