package com.smobile.premierleague

import android.app.Application
import com.smobile.premierleague.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class PremierLeagueApp : Application(), HasAndroidInjector {

    companion object {
        const val SEASON = "2019-2020"
        const val LEAGUE = "Premier League"
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}