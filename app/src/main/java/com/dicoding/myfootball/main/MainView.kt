package com.dicoding.myfootball.main

import com.dicoding.myfootball.model.League
import com.dicoding.myfootball.model.LeagueDetail

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data: List<League>)
    fun showLeagueDetail(data: List<LeagueDetail>)
}
