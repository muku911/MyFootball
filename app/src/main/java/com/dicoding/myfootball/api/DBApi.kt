package com.dicoding.myfootball.api

import com.dicoding.myfootball.BuildConfig

object DBApi {

    fun getLeague(): String {
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/all_leagues.php?"
    }

    fun getDetailLeague(league: String): String {
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/lookupleague.php?id="+league
    }

    fun getTeam(league: String): String {
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/lookup_all_teams.php?id="+league
    }

    fun getPlayer(teamId: String): String {
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/lookup_all_players.php?id="+teamId
    }
}