package com.dicoding.myfootball.main

import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.api.DBApi
import com.dicoding.myfootball.model.PlayerResponse
import com.dicoding.myfootball.test.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class PlayerPresenter(private val view: PlayerView,
                    private val apiRepository: ApiRepository,
                    private val gson: Gson,
                    private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getPlayerList(teamID: String) {

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(DBApi.getPlayer(teamID)),
                        PlayerResponse::class.java
                )
            }
            view.showPlayerList(data.await().player)
        }
    }
}