package com.smobile.premierleague.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.smobile.premierleague.R
import com.smobile.premierleague.util.Language

data class SettingsScreenState(
    val language: Language
)

@Composable
fun SettingsScreen(
    uiState: SettingsScreenState,
    onLanguageSelected: (Language) -> Unit
) {
    val isDialogShown = remember { mutableStateOf(false) }

    SettingsOptions(uiState) {
        isDialogShown.value = true
    }

    if (isDialogShown.value) {
        LanguagesDialog(
            uiState = uiState,
            setIsDialogShown = { isDialogShown.value = it },
            onLanguageSelected = onLanguageSelected
        )
    }
}

@Composable
private fun SettingsOptions(
    uiState: SettingsScreenState,
    onChooseLanguageOptionSelected: () -> Unit
) {
    Scaffold { innerPadding ->
        Column {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .clickable {
                        onChooseLanguageOptionSelected()
                    },
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.language),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.settings_padding)),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = stringResource(id = uiState.language.titleId),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.settings_padding)),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            }
            Divider(
                color = colorResource(id = R.color.divider_color)
            )
        }
    }
}

@Composable
private fun LanguagesDialog(
    uiState: SettingsScreenState,
    setIsDialogShown: (Boolean) -> Unit,
    onLanguageSelected: (Language) -> Unit
) {
    Dialog(onDismissRequest = { setIsDialogShown(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.choose_language),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Language.values().forEach {
                        LanguageRadioButton(
                            uiState = uiState,
                            language = it,
                            onLanguageSelected = { language ->
                                setIsDialogShown(false)
                                onLanguageSelected(language)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageRadioButton(
    uiState: SettingsScreenState,
    language: Language,
    onLanguageSelected: (Language) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = CenterVertically
    ) {
        RadioButton(
            selected = language == uiState.language,
            onClick = { onLanguageSelected(language) }
        )
        Text(
            text = stringResource(id = language.titleId),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    val uiState = SettingsScreenState(Language.ENGLISH)
    SettingsScreen(uiState) { _ -> }
}

@Preview
@Composable
private fun LanguagesDialogPreview() {
    val uiState = SettingsScreenState(Language.ENGLISH)
    LanguagesDialog(uiState, {}, { _ -> })
}