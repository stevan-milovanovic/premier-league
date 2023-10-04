package com.smobile.premierleague

import android.content.Context
import android.content.res.Configuration
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

    override fun attachBaseContext(newBase: Context?) {
        val configuration = Configuration()
        newBase?.let {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
            sharedPreferences.getString(LANGUAGE, null)?.let {
                configuration.setLocale(Locale(it))
            } ?: configuration.setLocale(Locale.getDefault())
        }

        //Workaround for platform bug on SDK < 26
        configuration.fontScale = 0f

        val updatedContext = newBase?.createConfigurationContext(configuration)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

}
