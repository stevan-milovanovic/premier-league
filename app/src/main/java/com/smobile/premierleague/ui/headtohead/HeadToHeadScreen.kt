package com.smobile.premierleague.ui.headtohead

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
fun HeadToHeadScreen(
    players: List<Player>,
    winnerId: Int
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(players) { player ->
            PlayerRow(
                player = player,
                winnerId = winnerId
            )
        }
    }
}

@Composable
private fun PlayerRow(
    player: Player,
    winnerId: Int
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.logo_padding)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(
                id = if (winnerId == player.id) R.color.colorBackground else android.R.color.white
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
                placeholder = painterResource(id = R.drawable.ic_account_circle_background_64dp)
            )
            Text(
                text = player.name,
                color = textColor(player, winnerId),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.age, player.age),
                color = textColor(player, winnerId)
            )
            Text(
                text = stringResource(id = R.string.goals, player.goals?.total ?: 0),
                color = textColor(player, winnerId)
            )
            Text(
                text = stringResource(id = R.string.assists, player.goals?.assists ?: 0),
                color = textColor(player, winnerId)
            )
            Text(
                text = stringResource(id = R.string.passes, player.passes?.total ?: 0),
                color = textColor(player, winnerId)
            )
        }
    }
}

@Composable
private fun textColor(player: Player, winnerId: Int): Color =
    if (winnerId == player.id) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.secondary

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HeadToHeadPreview() {
    val player = Player(
        1,
        "Player",
        null,
        Random.nextInt(18, 35),
        null,
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
    val players = generateSequence { player }.take(2).toList()
    HeadToHeadScreen(players = players, winnerId = 2)
}
