package com.smobile.premierleague

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.smobile.premierleague.Const.LANGUAGE
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        setLanguage()
        super.onCreate(savedInstanceState)
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val dm = resources.displayMetrics
        val conf = resources.configuration
        val lang = sharedPreferences.getString(LANGUAGE, null)
        if (lang == null) {
            conf.setLocale(Locale.getDefault())
        } else conf.setLocale(Locale(lang))

        resources.updateConfiguration(conf, dm)
    }

}