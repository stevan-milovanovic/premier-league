package com.smobile.premierleague.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.Const.LANGUAGE
import com.smobile.premierleague.util.Language
import com.smobile.premierleague.util.Language.Companion.fromLanguageCode
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for [SettingsFragment]
 */
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : ViewModel() {

    private val _uiState: MutableLiveData<SettingsScreenUiState> = MutableLiveData()

    val uiState: LiveData<SettingsScreenUiState>
        get() = _uiState

    init {
        _uiState.value = SettingsScreenUiState(loadLanguage())
    }

    private fun loadLanguage(): Language {
        var language = Language.ENGLISH
        sharedPreferences.getString(LANGUAGE, Language.ENGLISH.locale.language)?.let {
            language = fromLanguageCode(it)
        }
        return language
    }

    fun setLanguage(language: Language) {
        if (Objects.equals(language, _uiState.value?.language)) {
            return
        }

        editor.putString(LANGUAGE, language.locale.language).apply()
        _uiState.value = SettingsScreenUiState(language)
    }

}
