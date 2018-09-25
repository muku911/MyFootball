package com.dicoding.myfootball.main

import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.api.DBApi
import com.dicoding.myfootball.model.LeagueDetailResponse
import com.dicoding.myfootball.model.LeagueResponse
import com.dicoding.myfootball.test.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MainPresenter(private val view: MainView,
                    private val apiRepository: ApiRepository,
                    private val gson: Gson,
                    private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getLeagueList() {
        view.showLoading()

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(DBApi.getLeague()),
                        LeagueResponse::class.java
                )
            }
            view.showLeagueList(data.await().leagues)
            view.hideLoading()
        }
    }

    fun getLeagueDetail(league: String) {
        view.showLoading()

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(DBApi.getDetailLeague(league)),
                        LeagueDetailResponse::class.java
                )
            }
            view.showLeagueDetail(data.await().leagues)
            view.hideLoading()
        }
    }

}

