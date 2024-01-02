package com.smobile.premierleague

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.preference.PreferenceManager
import com.smobile.premierleague.Const.LANGUAGE
import com.smobile.premierleague.ui.headtohead.HeadToHeadScreen
import com.smobile.premierleague.ui.headtohead.HeadToHeadViewModel
import com.smobile.premierleague.ui.standings.StandingsScreen
import com.smobile.premierleague.ui.standings.StandingsViewModel
import com.smobile.premierleague.ui.team.TeamScreen
import com.smobile.premierleague.ui.team.TeamViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val configuration = Configuration()
        newBase?.let {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
            sharedPreferences.getString(LANGUAGE, null)?.let {
                configuration.setLocale(Locale(it))
            } ?: configuration.setLocale(Locale.getDefault())
        }

        //Workaround for platform bug on SDK < 26
        configuration.fontScale = 0f

        val updatedContext = newBase?.createConfigurationContext(configuration)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            //TODO: Add App Bar
            PremierLeagueNavHost(navController)
        }
    }

    @Composable
    private fun PremierLeagueNavHost(
        navController: NavHostController
    ) {
        NavHost(navController = navController, startDestination = STANDINGS_ROUTE) {
            standingsComposable(navController)
            teamComposable(navController)
            headToHeadComposable()
        }
    }


    private fun NavGraphBuilder.standingsComposable(
        navController: NavHostController
    ) = composable(STANDINGS_ROUTE) {
        val standingsViewModel: StandingsViewModel = hiltViewModel()
        val standings by standingsViewModel.standings.observeAsState()
        standings?.let { resource ->
            if (resource.data != null) {
                StandingsScreen(
                    standings = resource.data,
                    showSelectedTeam = { teamId -> navigateToTeam(navController, teamId) }
                )
            }
        }
    }

    private fun NavGraphBuilder.teamComposable(
        navController: NavHostController
    ) = composable(
        route = TEAM_ROUTE,
        arguments = listOf(navArgument(TEAM_ID_ARG) { type = NavType.IntType })
    ) { backStackEntry ->
        val teamId = backStackEntry.arguments?.getInt(TEAM_ID_ARG) ?: return@composable
        val teamViewModel: TeamViewModel = hiltViewModel()
        teamViewModel.setTeamId(teamId)
        val playersResource by teamViewModel.players.observeAsState()
        val playerOne by teamViewModel.playerOne.observeAsState()
        val playerTwo by teamViewModel.playerTwo.observeAsState()
        playersResource?.let { resource ->
            TeamScreen(
                players = resource.data ?: emptyList(),
                playerOne = playerOne,
                playerTwo = playerTwo,
                onPlayerSelected = { teamViewModel.choosePlayer(it.id) },
                onComparePlayersClicked = {
                    val playerOneId = playerOne?.id ?: return@TeamScreen
                    val playerTwoId = playerTwo?.id ?: return@TeamScreen
                    navigateToHeadToHead(navController, playerOneId, playerTwoId, teamId)
                }
            )
        }
    }

    private fun NavGraphBuilder.headToHeadComposable() = composable(
        route = HEAD_TO_HEAD_ROUTE,
        arguments = listOf(
            navArgument(PLAYER_ONE_ID_ARG) { type = NavType.IntType },
            navArgument(PLAYER_TWO_ID_ARG) { type = NavType.IntType },
            navArgument(TEAM_ID_ARG) { type = NavType.IntType },
        )
    ) {
        val playerOneId = it.arguments?.getInt(PLAYER_ONE_ID_ARG) ?: return@composable
        val playerTwoId = it.arguments?.getInt(PLAYER_TWO_ID_ARG) ?: return@composable
        val teamId = it.arguments?.getInt(TEAM_ID_ARG) ?: return@composable
        val headToHeadViewModel: HeadToHeadViewModel = hiltViewModel()
        headToHeadViewModel.setParams(playerOneId, playerTwoId, teamId)
        val playersResource by headToHeadViewModel.players.observeAsState()
        val winnerId by headToHeadViewModel.winnerId.observeAsState()
        playersResource?.let { resource ->
            HeadToHeadScreen(
                players = resource.data ?: emptyList(),
                winnerId = winnerId ?: 0
            )
        }
    }

    private fun navigateToTeam(
        navController: NavHostController,
        teamId: Int
    ) {
        navController.navigate("$TEAM/$teamId")
    }

    private fun navigateToHeadToHead(
        navController: NavHostController,
        playerOneId: Int,
        playerTwoId: Int,
        teamId: Int
    ) {
        navController.navigate("$HEAD_TO_HEAD/${playerOneId}/${playerTwoId}/$teamId")
    }

    companion object {
        private const val PLAYER_ONE_ID_ARG = "playerOneId"
        private const val PLAYER_TWO_ID_ARG = "playerTwoId"
        private const val TEAM_ID_ARG = "teamId"
        private const val HEAD_TO_HEAD = "headToHead"
        private const val TEAM = "team"

        private const val STANDINGS_ROUTE = "standings"
        private const val TEAM_ROUTE = "$TEAM/{$TEAM_ID_ARG}"
        private const val HEAD_TO_HEAD_ROUTE =
            "$HEAD_TO_HEAD/{$PLAYER_ONE_ID_ARG}/{$PLAYER_TWO_ID_ARG}/{$TEAM_ID_ARG}"
    }

}
