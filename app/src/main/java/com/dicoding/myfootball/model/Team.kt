package com.dicoding.myfootball.model

import com.google.gson.annotations.SerializedName

data class Team(
        @SerializedName("idTeam")
        var teamId: String? = null,

        @SerializedName("strTeam")
        var teamName: String? = null,

        @SerializedName("strTeamBadge")
        var teamBadge: String? = null,

        @SerializedName("strDescriptionEN")
        var teamDescription: String? = null
)

data class Player(
        @SerializedName("idPlayer")
        var playerId: String? = null,

        @SerializedName("strPlayer")
        var playerName: String? = null,

        @SerializedName("strBirthLocation")
        var playerBornLocation: String? = null,

        @SerializedName("dateBorn")
        var playerBorn: String? = null,

        @SerializedName("strGender")
        var playerGender: String? = null,

        @SerializedName("strTeam")
        var playerTeam: String? = null,

        @SerializedName("dateSigned")
        var playerTeamSign: String? = null,

        @SerializedName("strPosition")
        var playerPosition: String? = null,

        @SerializedName("strCutout")
        var playerFace: String? = null,

        @SerializedName("strDescriptionEN")
        var playerDescription: String? = null,

        @SerializedName("strThumb")
        var playerCover: String? = null,

        @SerializedName("strHeight")
        var playerHeight: String? = null,

        @SerializedName("strWeight")
        var playerWeight: String? = null,

        @SerializedName("strNationality")
        var playerNationality: String? = null
)