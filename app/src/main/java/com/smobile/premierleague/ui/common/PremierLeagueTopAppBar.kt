package com.smobile.premierleague.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.smobile.premierleague.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremierLeagueTopAppBar(
    @StringRes titleResId: Int,
    onBackNavigation: (() -> Unit)? = null,
    onTopBarAction: (() -> Unit)? = null,
    topBarActionImageVector: ImageVector? = null,
    @StringRes topBarActionResId: Int? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = stringResource(id = titleResId),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        navigationIcon = {
            if (onBackNavigation != null) {
                IconButton(onClick = onBackNavigation) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        },
        actions = {
            if (onTopBarAction != null && topBarActionImageVector != null && topBarActionResId != null) {
                IconButton(onClick = onTopBarAction) {
                    Icon(
                        imageVector = topBarActionImageVector,
                        contentDescription = stringResource(id = topBarActionResId)
                    )
                }
            }
        }
    )
}