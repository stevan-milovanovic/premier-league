package com.smobile.premierleague.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Fragment for showing application settings
 */
class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val language by settingsViewModel.language.observeAsState()
                language?.let {
                    SettingsScreen(
                        language = it,
                        onLanguageSelected = { selectedLanguage ->
                            settingsViewModel.setLanguage(selectedLanguage)
                            this@SettingsFragment.activity?.recreate()
                        }
                    )
                }
            }
        }
    }

}
