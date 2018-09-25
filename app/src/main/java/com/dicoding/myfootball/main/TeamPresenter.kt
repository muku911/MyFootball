package com.dicoding.myfootball.main

import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.api.DBApi
import com.dicoding.myfootball.model.PlayerResponse
import com.dicoding.myfootball.model.TeamResponse
import com.dicoding.myfootball.test.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class TeamPresenter(private val view: TeamView,
                    private val apiRepository: ApiRepository,
                    private val gson: Gson,
                    private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamList(leagueID: String) {
        view.showLoading()

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(DBApi.getTeam(leagueID)),
                        TeamResponse::class.java
                )
            }
            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }
}