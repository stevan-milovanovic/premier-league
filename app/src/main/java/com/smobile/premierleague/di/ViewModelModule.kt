package com.smobile.premierleague.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smobile.premierleague.ui.headtohead.HeadToHeadViewModel
import com.smobile.premierleague.ui.settings.SettingsViewModel
import com.smobile.premierleague.ui.standings.StandingsViewModel
import com.smobile.premierleague.ui.team.TeamViewModel
import com.smobile.premierleague.viewmodel.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StandingsViewModel::class)
    abstract fun bindStandingsViewModel(standingsViewModel: StandingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeamViewModel::class)
    abstract fun bindTeamViewModel(teamViewModel: TeamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HeadToHeadViewModel::class)
    abstract fun bindHeadToHeadViewModel(teamViewModel: HeadToHeadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

}