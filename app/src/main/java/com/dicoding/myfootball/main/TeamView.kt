package com.dicoding.myfootball.main

import com.dicoding.myfootball.model.Team

interface TeamView{

    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
}