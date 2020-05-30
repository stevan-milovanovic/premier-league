package com.smobile.premierleague.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smobile.premierleague.standings.StandingsViewModel
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
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

}