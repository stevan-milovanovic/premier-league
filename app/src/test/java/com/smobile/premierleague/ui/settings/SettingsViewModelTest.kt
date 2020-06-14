package com.smobile.premierleague.ui.settings

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.Const
import com.smobile.premierleague.util.Language
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Unit test class for [SettingsViewModel]
 */
class SettingsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val sharedPrefs = mock(SharedPreferences::class.java)
    private val editor = mock(SharedPreferences.Editor::class.java)
    private val viewModel = SettingsViewModel(sharedPrefs, editor)

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.language)
        assertArrayEquals(Language.values(), viewModel.availableLanguages)
        assertEquals(0, viewModel.selectedLanguageIndex)
    }

    @Test
    fun testLoadLanguage() {
        viewModel.loadLanguage()
        verify(sharedPrefs).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
    }

    @Test
    fun testSetLanguage() {
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)
        viewModel.setLanguage(Language.ENGLISH)
        verify(editor).putString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        verify(editor).apply()
        //load the language after the change
        verify(sharedPrefs).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        assertEquals(0, viewModel.selectedLanguageIndex)
        assertEquals(Language.ENGLISH, viewModel.language.value)

        //try to set the same language for the second time
        viewModel.setLanguage(Language.ENGLISH)
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPrefs)

        `when`(sharedPrefs.getString(Const.LANGUAGE, Language.ENGLISH.locale.language))
            .thenReturn(Language.SERBIAN.locale.language)
        viewModel.setLanguage(Language.SERBIAN)
        verify(editor).putString(Const.LANGUAGE, Language.SERBIAN.locale.language)
        verify(editor, times(2)).apply()
        //load the language after the change
        verify(sharedPrefs, times(2)).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        assertEquals(1, viewModel.selectedLanguageIndex)
        assertEquals(Language.SERBIAN, viewModel.language.value)
    }

}