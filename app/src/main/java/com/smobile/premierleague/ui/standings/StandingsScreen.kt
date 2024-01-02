package com.smobile.premierleague.ui.standings

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.smobile.premierleague.R
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.ui.common.PremierLeagueTopAppBar

@Composable
fun StandingsScreen(
    standings: List<Standing>,
    showSelectedTeam: (Int) -> Unit,
    onSettingsNavigation: () -> Unit
) {
    Scaffold(
        topBar = {
            PremierLeagueTopAppBar(
                titleResId = R.string.app_name,
                onTopBarAction = onSettingsNavigation,
                topBarActionImageVector = Icons.Filled.Settings,
                topBarActionResId = R.string.settings
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            HeaderStandingRow()
            Divider(color = colorResource(id = R.color.divider_color))
            StandingsList(
                standings = standings,
                showSelectedTeam = showSelectedTeam
            )
        }
    }
}

@Composable
private fun StandingsList(
    standings: List<Standing>,
    showSelectedTeam: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(standings) { standing ->
            StandingRow(
                teamId = standing.id,
                position = stringResource(id = R.string.rank, standing.rank),
                teamLogoUrl = standing.logo,
                teamName = standing.name,
                playedGames = stringResource(id = R.string.value, standing.statistics.played),
                wonGames = stringResource(id = R.string.value, standing.statistics.win),
                drawnGames = stringResource(id = R.string.value, standing.statistics.draw),
                lostGames = stringResource(id = R.string.value, standing.statistics.lose),
                points = stringResource(id = R.string.value, standing.points),
                showSelectedTeam = showSelectedTeam
            )
        }
    }
}

@Composable
private fun HeaderStandingRow() {
    StandingRow(
        teamId = null,
        position = stringResource(id = R.string.position),
        teamLogoUrl = null,
        teamName = null,
        playedGames = stringResource(id = R.string.played),
        wonGames = stringResource(id = R.string.won),
        drawnGames = stringResource(id = R.string.drawn),
        lostGames = stringResource(id = R.string.lost),
        points = stringResource(id = R.string.points),
        showSelectedTeam = {}
    )
}

@Composable
private fun StandingRow(
    teamId: Int?,
    position: String,
    teamLogoUrl: String?,
    teamName: String?,
    playedGames: String,
    wonGames: String,
    drawnGames: String,
    lostGames: String,
    points: String,
    showSelectedTeam: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.logo_size))
            .clickable(
                enabled = teamId != null,
                indication = if (teamId != null) LocalIndication.current else null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { showSelectedTeam(teamId!!) },
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = position,
            Modifier
                .width(dimensionResource(id = R.dimen.rank_width))
                .padding(start = dimensionResource(id = R.dimen.medium_padding)),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        val logoModifier = Modifier.padding(dimensionResource(id = R.dimen.logo_padding))
        if (teamLogoUrl != null) {
            AsyncImage(
                model = teamLogoUrl,
                contentDescription = stringResource(id = R.string.team_logo),
                modifier = logoModifier.size(dimensionResource(id = R.dimen.logo_size)),
                placeholder = painterResource(id = R.drawable.football)
            )
        } else {
            Text(
                text = stringResource(id = R.string.club),
                modifier = logoModifier.width(dimensionResource(id = R.dimen.logo_size)),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        val teamNameModifier = Modifier.weight(1f)
        if (teamName != null) {
            Text(
                text = teamName.take(3).uppercase(),
                modifier = teamNameModifier,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        } else {
            Spacer(modifier = teamNameModifier)
        }
        StandingCategory(value = playedGames)
        StandingCategory(value = wonGames)
        StandingCategory(value = drawnGames)
        StandingCategory(value = lostGames)
        StandingCategory(value = points, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StandingCategory(
    value: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = value,
        modifier
            .width(dimensionResource(id = R.dimen.rank_width))
            .padding(dimensionResource(id = R.dimen.small_padding)),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}

@Preview(showSystemUi = true, device = Devices.NEXUS_5)
@Composable
private fun StandingsScreenPreview() {
    val statistics1 = Standing.TeamStatistic(6, 3, 1, 2)
    val statistics2 = Standing.TeamStatistic(6, 2, 1, 3)
    val standings = listOf(
        Standing(1, 1, "Arsenal", "arsenal.com", statistics1, 10),
        Standing(2, 2, "Chelsea", "Chelsea.com", statistics2, 7)
    )
    StandingsScreen(
        standings = standings,
        showSelectedTeam = {},
        onSettingsNavigation = {}
    )
}

@Preview(showSystemUi = true, locale = "sr", device = Devices.PIXEL)
@Composable
private fun StandingsScreenPreviewSerbianLocale() {
    val statistics1 = Standing.TeamStatistic(6, 3, 1, 2)
    val statistics2 = Standing.TeamStatistic(6, 2, 1, 3)
    val standings = listOf(
        Standing(1, 1, "Arsenal", "arsenal.com", statistics1, 10),
        Standing(2, 2, "Chelsea", "Chelsea.com", statistics2, 7)
    )
    StandingsScreen(
        standings = standings,
        showSelectedTeam = {},
        onSettingsNavigation = {}
    )
}
