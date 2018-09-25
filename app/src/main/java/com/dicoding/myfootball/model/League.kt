package com.dicoding.myfootball.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by root on 1/23/18.
 */
data class League(
        @SerializedName("idLeague")
        var leagueId: String? = null,

        @SerializedName("strLeague")
        var leagueName: String? = null,

        @SerializedName("strSport")
        var leagueType: String? = null,

        @SerializedName("strBadge")
        var leagueBadge: String? = null
        )

data class LeagueDetail(
        @SerializedName("idLeague")
        var leagueId: String? = null,

        @SerializedName("strLeague")
        var leagueName: String? = null,

        @SerializedName("strBadge")
        var leagueBadge: String? = null
)