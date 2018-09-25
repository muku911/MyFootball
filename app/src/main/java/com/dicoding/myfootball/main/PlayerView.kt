package com.dicoding.myfootball.main

import com.dicoding.myfootball.model.Player

interface PlayerView {
    fun showPlayerList(data: List<Player>)
}