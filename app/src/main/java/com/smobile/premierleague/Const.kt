package com.smobile.premierleague

object Const {

    const val DB_NAME = "league.db"
    const val LANGUAGE = "language"

    const val BASE_URL = "https://free-api-live-football-data.p.rapidapi.com/"
    const val API_KEY_HEADER = "x-rapidapi-key"
    const val API_KEY_HEADER_VALUE = "c59360daeamshc94bca2dd4c3b5ep10aebejsn838770ba9409"
    const val API_HOST_HEADER = "x-rapidapi-host"
    const val API_HOST_HEADER_VALUE = "free-api-live-football-data.p.rapidapi.com"
    const val API_PLAYER_URL_ID_PLACEHOLDER = "{id}"
    const val TEAM_LOGO_URL_ID_PLACEHOLDER = "{id}"
    private const val IMAGE_RESOURCES_BASE_URL = "https://images.fotmob.com/image_resources"
    const val TEAM_LOGO_URL =
        "$IMAGE_RESOURCES_BASE_URL/logo/teamlogo/${TEAM_LOGO_URL_ID_PLACEHOLDER}_large.png"
    const val API_PLAYER_URL =
        "$IMAGE_RESOURCES_BASE_URL/playerimages/$API_PLAYER_URL_ID_PLACEHOLDER.png"

}
