package com.dicoding.myfootball.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dicoding.myfootball.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        imgBtn_Match.setOnClickListener{
            val intent = Intent(applicationContext, LeagueActivity::class.java)
            intent.putExtra("typeName", "List Match")
            startActivity(intent)
        }

        imgBtn_Team.setOnClickListener{
            val intent = Intent(applicationContext, LeagueActivity::class.java)
            intent.putExtra("typeName", "List Team")
            startActivity(intent)
        }

        imgBtn_Favorite.setOnClickListener{
            val intent = Intent(applicationContext, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }
}
