package com.smobile.premierleague.util

import androidx.annotation.StringRes
import com.smobile.premierleague.R
import java.util.*

enum class Language(@StringRes val titleId: Int, val locale: Locale) {

    ENGLISH(R.string.english, Locale.ENGLISH),
    SERBIAN(R.string.serbian, Locale("sr"));

    companion object {
        fun fromLanguageCode(languageCode: String) = when (languageCode) {
            SERBIAN.locale.language -> SERBIAN
            else -> ENGLISH
        }
    }

}