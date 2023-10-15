package com.smobile.premierleague.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.smobile.premierleague.R
import com.smobile.premierleague.util.Language

@Composable
fun SettingsScreen(
    language: Language,
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
                    text = stringResource(id = language.titleId),
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
            language = language,
            onDismissRequest = { isDialogShown.value = false },
            onLanguageSelected = onLanguageSelected
        )
    }
}

@Composable
private fun LanguagesDialog(
    language: Language,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (Language) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.choose_language),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                    Language.values().forEach {
                        LanguageRadioButton(
                            isSelected = language == it,
                            language = it,
                            onLanguageSelected = {
                                onDismissRequest()
                                onLanguageSelected(it)
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
    isSelected: Boolean,
    language: Language,
    onLanguageSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onLanguageSelected
        )
        Text(
            text = stringResource(id = language.titleId),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(Language.SERBIAN) {}
}

@Preview(showSystemUi = true, showBackground = true, locale = "sr")
@Composable
private fun LanguageDialogPreview() {
    LanguagesDialog(Language.SERBIAN, {}) {}
}
