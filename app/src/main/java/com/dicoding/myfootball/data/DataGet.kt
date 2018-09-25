package com.dicoding.myfootball.data

class ListNextMatch(val homeName: String?, val homeID: String?, val awayName: String?,
                    val awayID: String?, val dateMatch: String?, val matchID: String?)

class ListLastMatch(val homeName: String?, val homeID: String?, val homeScore: String?,
                    val awayName: String?, val awayID: String?, val awayScore: String?,
                    val matchDate: String?, val matchID: String?)

class FullDataLastMatch(val homeName: String?, val homeID: String?, val homeScore: String?,
                        val awayName: String?, val awayID: String?, val awayScore: String?,
                        val homeBadge: String?, val awayBadge: String?,
                        val matchDate: String?, val matchID: String?)

class FullDataNextMatch(val homeName: String?, val homeID: String?,
                        val awayName: String?, val awayID: String?,
                        val homeBadge: String?, val awayBadge: String?,
                        val matchDate: String?, val matchID: String?)

class MatchDetail(val homeName: String?, val homeScore: String?, val homeGoalDetail: String?,
                      val homeYellowCard: String?, val homeRedCard: String?,
                      val homeGoalKeeper: String?, val homeDeffense: String?, val homeMidfield: String?, val homeOffense: String?, val homeSubstitutes: String?,
                      val awayName: String?, val awayScore: String?, val awayGoalDetail: String?,
                      val awayYellowCard: String?, val awayRedCard: String?,
                      val awayGoalKeeper: String?, val awayDeffense: String?, val awayMidfield: String?, val awayOffense: String?, val awaySubstitutes: String?, val matchDate: String?)

class ListTeam(val teamID: String?, val teamName: String?, val teamBadge: String?)

class GetBadgeMatch(val homeBadge: String?, val awayBadge: String?)

class ListLeague(val lagueID: String?, val lagueName: String?, val lagueBadge: String?)

