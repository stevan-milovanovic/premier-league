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

    private val _language: MutableLiveData<Language> = MutableLiveData()

    val language: LiveData<Language>
        get() = _language

    val availableLanguages: Array<Language>
        get() = Language.values()

    val selectedLanguageIndex: Int
        get() = availableLanguages.toList().find { it == language.value }?.ordinal ?: 0

    fun loadLanguage() {
        sharedPreferences.getString(LANGUAGE, Language.ENGLISH.locale.language)?.let {
            _language.value = fromLanguageCode(it)
        } ?: run {
            _language.value = Language.ENGLISH
        }
    }

    fun setLanguage(language: Language) {
        if (Objects.equals(language, _language.value)) {
            return
        }

        editor.putString(LANGUAGE, language.locale.language).apply()
        loadLanguage()
    }

}