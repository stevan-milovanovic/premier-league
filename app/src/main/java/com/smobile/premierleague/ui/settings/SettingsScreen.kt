package com.smobile.premierleague.ui.settings

import androidx.compose.foundation.clickable
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

data class SettingsScreenUiState(
    val language: Language
)

@Composable
fun SettingsScreen(
    uiState: SettingsScreenUiState,
    onLanguageSelected: (Language) -> Unit
) {
    val isDialogShown = remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .clickable { isDialogShown.value = true }
            ) {
                Text(
                    text = stringResource(id = R.string.language),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.settings_padding)),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = uiState.language.titleId),
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.settings_padding)),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider(
                color = colorResource(id = R.color.divider_color)
            )
        }
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
private fun LanguagesDialog(
    uiState: SettingsScreenUiState,
    setIsDialogShown: (Boolean) -> Unit,
    onLanguageSelected: (Language) -> Unit
) {
    Dialog(
        onDismissRequest = { setIsDialogShown(false) }
    ) {
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
                        style = MaterialTheme.typography.headlineSmall
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
    uiState: SettingsScreenUiState,
    language: Language,
    onLanguageSelected: (Language) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = language == uiState.language,
            onClick = { onLanguageSelected(language) }
        )
        Text(
            text = stringResource(id = language.titleId),
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Composable
@Preview
private fun SettingsScreenPreview() {
    SettingsScreen(SettingsScreenUiState(Language.ENGLISH)) {}
}

@Composable
@Preview(locale = "sr", showBackground = true, showSystemUi = true)
private fun LanguagesDialogPreview() {
    LanguagesDialog(
        uiState = SettingsScreenUiState(Language.ENGLISH),
        setIsDialogShown = {}
    ) {}
}


