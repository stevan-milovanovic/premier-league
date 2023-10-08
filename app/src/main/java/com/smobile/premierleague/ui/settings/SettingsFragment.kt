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
import com.smobile.premierleague.di.Injectable
import javax.inject.Inject

/**
 * Fragment for showing application settings
 */
class SettingsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by settingsViewModel.uiState.observeAsState()
                uiState?.let {
                    SettingsScreen(
                        uiState = it,
                        onLanguageSelected = { language ->
                            settingsViewModel.setLanguage(language)
                            this@SettingsFragment.activity?.recreate()
                        }
                    )
                }
            }
        }
    }

}
