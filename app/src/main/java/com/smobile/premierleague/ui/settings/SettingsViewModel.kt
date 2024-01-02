package com.smobile.premierleague.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smobile.premierleague.Const.LANGUAGE
import com.smobile.premierleague.util.Language
import com.smobile.premierleague.util.Language.Companion.fromLanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for [SettingsFragment]
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) : ViewModel() {

    private val _language: MutableLiveData<Language> = MutableLiveData()

    val language: LiveData<Language>
        get() = _language

    init {
        _language.value = loadLanguage()
    }

    private fun loadLanguage(): Language {
        var language = Language.ENGLISH
        sharedPreferences.getString(LANGUAGE, Language.ENGLISH.locale.language)?.let {
            language = fromLanguageCode(it)
        }
        return language
    }

    fun setLanguage(language: Language) {
        if (Objects.equals(language, _language.value)) {
            return
        }

        editor.putString(LANGUAGE, language.locale.language).apply()
        _language.value = language
    }

}
