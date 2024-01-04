package com.smobile.premierleague.ui.headtohead

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smobile.premierleague.R
import com.smobile.premierleague.model.Goals
import com.smobile.premierleague.model.Passes
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.ui.common.PlayerCard
import com.smobile.premierleague.ui.common.PremierLeagueTopAppBar
import kotlin.random.Random


@Composable
fun HeadToHeadScreen(
    players: List<Player>,
    winnerId: Int,
    onBackNavigation: () -> Unit
) {
    Scaffold(
        topBar = {
            PremierLeagueTopAppBar(
                titleResId = R.string.head_to_head,
                onBackNavigation = onBackNavigation
            )
        }
    ) { contentPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            items(players) { player ->
                PlayerCard(
                    player = player,
                    selected = player.id == winnerId,
                    overviewMode = false
                )
            }
        }
    }
}

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
        Goals(
            total = Random.nextInt(0, 10),
            conceded = Random.nextInt(0, 10),
            assists = Random.nextInt(0, 10),
        ),
        Passes(
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
    HeadToHeadScreen(players = players, winnerId = 2, onBackNavigation = {})
}
