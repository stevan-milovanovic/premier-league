package com.smobile.premierleague.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.smobile.premierleague.R
import com.smobile.premierleague.model.Player
import kotlin.random.Random

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player,
    selected: Boolean,
    overviewMode: Boolean = true
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.logo_padding)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(
                id = if (selected) R.color.colorBackground else android.R.color.white
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.logo_padding))
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = player.imageUrl,
                contentDescription = stringResource(id = R.string.team_logo),
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.logo_padding))
                    .size(dimensionResource(id = R.dimen.logo_size))
                    .clip(CircleShape),
                placeholder = painterResource(
                    id = if (selected) R.drawable.ic_account_circle_white_64dp
                    else R.drawable.ic_account_circle_background_64dp
                )
            )
            Text(
                text = player.name,
                color = textColor(selected),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.age, player.age),
                color = textColor(selected)
            )
            if (overviewMode) {
                OverviewStatistics(player, selected)
            } else {
                DetailedStatistics(player, selected)
            }
        }
    }
}

@Composable
private fun OverviewStatistics(
    player: Player,
    selected: Boolean
) {
    Text(
        text = player.position ?: "",
        color = textColor(selected)
    )
    Text(
        text = player.nationality ?: "",
        color = textColor(selected)
    )
}

@Composable
private fun DetailedStatistics(
    player: Player,
    selected: Boolean
) {
    Text(
        text = stringResource(id = R.string.goals, player.goals?.total ?: 0),
        color = textColor(selected)
    )
    Text(
        text = stringResource(id = R.string.assists, player.goals?.assists ?: 0),
        color = textColor(selected)
    )
    Text(
        text = stringResource(id = R.string.passes, player.passes?.total ?: 0),
        color = textColor(selected)
    )
}

@Composable
private fun textColor(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.secondary

@Preview
@Composable
fun PlayerCardWithOverviewStatistics() {
    PlayerCard(player = previewPlayer, selected = true)
}

@Preview
@Composable
fun PlayerCardWithDetailedStatistics() {
    PlayerCard(player = previewPlayer, selected = false, overviewMode = false)
}

private val previewPlayer = Player(
    1,
    "Player",
    "Defender",
    Random.nextInt(18, 35),
    "Serbia",
    null,
    null,
    Player.Goals(
        total = Random.nextInt(0, 10),
        conceded = Random.nextInt(0, 10),
        assists = Random.nextInt(0, 10),
    ),
    Player.Passes(
        total = Random.nextInt(0, 10),
        key = Random.nextInt(0, 10),
        accuracy = Random.nextInt(0, 10)
    ),
    null,
    null,
    null,
    null
)
