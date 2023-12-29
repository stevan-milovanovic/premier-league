package com.smobile.premierleague.ui.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smobile.premierleague.R
import com.smobile.premierleague.model.Player
import com.smobile.premierleague.ui.common.PlayerCard
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun TeamScreen(
    players: List<Player>,
    playerOne: Player?,
    playerTwo: Player?,
    onPlayerSelected: (Player) -> Unit,
    onComparePlayersClicked: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (playerOne != null && playerTwo != null) {
                FloatingActionButton(
                    onClick = onComparePlayersClicked,
                    shape = FloatingActionButtonDefaults.largeShape,
                    containerColor = colorResource(id = R.color.colorAccent),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_compare_white_48dp),
                        contentDescription = stringResource(id = R.string.compare_players),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.fab_size))
                            .padding(dimensionResource(id = R.dimen.fab_margin))
                    )
                }
            }
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
                    modifier = Modifier.clickable {
                        if (playerOne != null && playerTwo != null
                            && player.id != playerOne.id && player.id != playerTwo.id
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.resources.getString(R.string.players_already_selected)
                                )
                            }
                        }
                        onPlayerSelected(player)
                    },
                    player = player,
                    selected = player.id == playerOne?.id || player.id == playerTwo?.id
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TeamScreenPreview() {
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
    TeamScreen(
        players = players,
        playerOne = player,
        playerTwo = player,
        onPlayerSelected = {},
        onComparePlayersClicked = {}
    )
}
