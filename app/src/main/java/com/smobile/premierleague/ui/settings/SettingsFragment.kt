package com.smobile.premierleague.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smobile.premierleague.R
import com.smobile.premierleague.binding.FragmentDataBindingComponent
import com.smobile.premierleague.databinding.FragmentSettingsBinding
import com.smobile.premierleague.di.Injectable
import com.smobile.premierleague.util.autoCleared
import javax.inject.Inject

/**
 * Fragment for showing application settings
 */
class SettingsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var binding by autoCleared<FragmentSettingsBinding>()

    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false,
            dataBindingComponent
        )

        binding.languageContainer.setOnClickListener {
            showLanguagesDialog()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDataObserver()
        settingsViewModel.loadLanguage()
    }

    private fun showLanguagesDialog() {
        val languages = settingsViewModel.availableLanguages
        val values = languages.map { getString(it.titleId) }.toTypedArray()
        val selectedLanguage = settingsViewModel.selectedLanguageIndex

        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.choose_language))
            .setSingleChoiceItems(values, selectedLanguage) { dialog, item ->
                settingsViewModel.setLanguage(languages[item])
                activity?.recreate()
                dialog.dismiss()
            }
            .show()
    }

    private fun setupDataObserver() {
        settingsViewModel.language.observe(viewLifecycleOwner, Observer { language ->
            binding.language = getString(language.titleId)
        })
    }

}