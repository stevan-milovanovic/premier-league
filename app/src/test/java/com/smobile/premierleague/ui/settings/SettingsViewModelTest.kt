package com.smobile.premierleague.ui.settings

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.Const
import com.smobile.premierleague.util.Language
import com.smobile.premierleague.util.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

/**
 * Unit test class for [SettingsViewModel]
 */
class SettingsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val sharedPrefs: SharedPreferences = mock()
    private val editor: SharedPreferences.Editor = mock()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(sharedPrefs, editor)
    }

    @Test
    fun testInitialState() {
        assertNotNull(viewModel.uiState)
    }

    @Test
    fun testLoadLanguage() {
        verify(sharedPrefs).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
    }

    @Test
    fun testSetLanguage() {
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)
        `when`(sharedPrefs.getString(Const.LANGUAGE, Language.ENGLISH.locale.language))
            .thenReturn(Language.SERBIAN.locale.language)
        viewModel.setLanguage(Language.SERBIAN)
        verify(editor).putString(Const.LANGUAGE, Language.SERBIAN.locale.language)
        verify(editor).apply()
        //load the language after the change
        verify(sharedPrefs).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        assertEquals(Language.SERBIAN, viewModel.uiState.value?.language)

        //try to set the same language for the second time
        viewModel.setLanguage(Language.SERBIAN)
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPrefs)

        `when`(sharedPrefs.getString(Const.LANGUAGE, Language.ENGLISH.locale.language))
            .thenReturn(Language.SERBIAN.locale.language)
        viewModel.setLanguage(Language.ENGLISH)
        verify(editor).putString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        verify(editor, times(2)).apply()
        //load the language after the change
        verify(sharedPrefs).getString(Const.LANGUAGE, Language.ENGLISH.locale.language)
        assertEquals(Language.ENGLISH, viewModel.uiState.value?.language)
    }

}