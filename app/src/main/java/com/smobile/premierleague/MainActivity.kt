package com.smobile.premierleague

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.smobile.premierleague.Const.LANGUAGE
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_main)
        val navigationController = findNavController(R.id.fragment_container)
        NavigationUI.setupActionBarWithNavController(this, navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navigationController = findNavController(R.id.fragment_container)
        return navigationController.navigateUp()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun setLanguage() {
        val dm = resources.displayMetrics
        val conf = resources.configuration

        sharedPreferences.getString(LANGUAGE, null)?.let {
            conf.setLocale(Locale(it))
        } ?: conf.setLocale(Locale.getDefault())

        resources.updateConfiguration(conf, dm)
    }

}