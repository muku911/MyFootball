package com.dicoding.myfootball.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dicoding.myfootball.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_player.*

class DetailPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)

        val intent = intent
        val playerBadge = intent.getStringExtra("playerBadge")
        val playerName = intent.getStringExtra("playerName")
        val playerBornPlace = intent.getStringExtra("playerBornPlace")
        val playerBornDate =  intent.getStringExtra("playerBornDate")
        val playerGender = intent.getStringExtra("playerGender")
        val playerTeam = intent.getStringExtra("playerTeam")
        val playerTeamSign = intent.getStringExtra("playerTeamSign")
        val playerPosition = intent.getStringExtra("playerPosition")
        val playerHeight =  intent.getStringExtra("playerHeight")
        val playerWeight = intent.getStringExtra("playerWeight")
        val playerDescription = intent.getStringExtra("playerDescription")
        val playerNationality =   intent.getStringExtra("playerNationality")

        Picasso.get().load(playerBadge).into(imgPlayer)

        tvNamePlayer.text = playerName
        tvTempatTL.text = playerBornPlace + ", " + playerBornDate
        tvGender.text = playerGender
        tvTeam.text = playerTeam
        tvDateSign.text = "From: " + playerTeamSign
        tvPosition.text = playerPosition
        tvWeight.text = playerWeight+"Kg"
        tvHeight.text = playerHeight+"M"
        tvNation.text = playerNationality
        tvDescription.text = playerDescription

    }
}
