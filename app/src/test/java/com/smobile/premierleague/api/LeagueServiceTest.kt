package com.smobile.premierleague.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.smobile.premierleague.util.LiveDataCallAdapterFactory
import com.smobile.premierleague.util.LiveDataTestUtil.getValue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Unit test class for [LeagueService]
 */
class LeagueServiceTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: LeagueService

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(LeagueService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStandings() {
        enqueueResponse("standings.json")
        val standingsNetworkResponse =
            (getValue(service.getStandings(10)) as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, CoreMatchers.`is`("/leagueTable/10"))
        assertThat(standingsNetworkResponse.api.results, CoreMatchers.`is`(1))

        val standingsList = standingsNetworkResponse.api.standings[0]
        assertThat(standingsList.size, CoreMatchers.`is`(20))

        val firstTeam = standingsList[0]
        assertThat(firstTeam.id, CoreMatchers.`is`(40))
        assertThat(firstTeam.name, CoreMatchers.`is`("Liverpool"))
        assertThat(
            firstTeam.logo,
            CoreMatchers.`is`("https://media.api-sports.io/football/teams/40.png")
        )
        assertThat(firstTeam.rank, CoreMatchers.`is`(1))
        assertThat(firstTeam.points, CoreMatchers.`is`(82))

        val firstTeamStatistics = firstTeam.statistics
        assertThat(firstTeamStatistics.played, CoreMatchers.`is`(29))
        assertThat(firstTeamStatistics.win, CoreMatchers.`is`(27))
        assertThat(firstTeamStatistics.lose, CoreMatchers.`is`(1))
        assertThat(firstTeamStatistics.draw, CoreMatchers.`is`(1))

        val secondTeam = standingsList[1]
        assertThat(secondTeam.id, CoreMatchers.`is`(50))
        assertThat(secondTeam.name, CoreMatchers.`is`("Manchester City"))
        assertThat(
            secondTeam.logo,
            CoreMatchers.`is`("https://media.api-sports.io/football/teams/50.png")
        )
        assertThat(secondTeam.rank, CoreMatchers.`is`(2))
        assertThat(secondTeam.points, CoreMatchers.`is`(57))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        javaClass.classLoader?.getResourceAsStream("api-response/$fileName")?.let {
            val source = it.source().buffer()
            val mockResponse = MockResponse()
            for ((key, value) in headers) {
                mockResponse.addHeader(key, value)
            }
            mockWebServer.enqueue(
                mockResponse
                    .setBody(source.readString(Charsets.UTF_8))
            )
        }
    }

}