package com.dicoding.myfootball.db

data class Favorite(val id: Long?, val favoriteMatch: String?, val leagueName: String?, val matchId: String?, val matchDate: String?, val homeTeamName: String?, val homeScore: String?, val homeTeamBadge: String?, val awayTeamName: String?, val awayScore: String?, val awayTeamBadge: String?) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val FAVORITE_MATCH: String = "FAVORITE_MATCH"
        const val LEAGUE_NAME: String = "LEAGUE_NAME"
        const val MATCH_ID: String = "MATCH_ID"
        const val MATCH_DATE: String = "MATCH_DATE"
        const val HOME_TEAM_NAME: String = "HOME_TEAM_NAME"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val HOME_TEAM_BADGE: String = "HOME_TEAM_BADGE"
        const val AWAY_TEAM_NAME: String = "AWAY_TEAM_NAME"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val AWAY_TEAM_BADGE: String = "AWAY_TEAM_BADGE"
    }
}

data class FavoriteTeam(val id: Long?, val favoriteTeam: String?, val teamID: String?, val teamName: String?, val teamBadge: String?, val teamDescription: String?) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE_TEAM"
        const val ID: String = "ID_"
        const val FAVORITE_TEAM: String = "FAVORITE_TEAM"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_BADGE: String = "TEAM_BADGE"
        const val TEAM_DESCRIPTION: String = "TEAM_DESCRIPTION"
    }
}