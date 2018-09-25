package com.dicoding.myfootball.main

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.dicoding.myfootball.R
import com.dicoding.myfootball.adapter.PlayerViewAdapter
import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.db.FavoriteTeam
import com.dicoding.myfootball.db.database
import com.dicoding.myfootball.model.Player
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_match.*
import kotlinx.android.synthetic.main.activity_detail_team.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast

class DetailTeamActivity : AppCompatActivity(), PlayerView {

    private var player: MutableList<Player> = mutableListOf()
    private var playerFilter: ArrayList<Player> = arrayListOf()
    private var temp: ArrayList<Player> = arrayListOf()
    private lateinit var adapter: PlayerViewAdapter
    private lateinit var presenter: PlayerPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    private lateinit var teamID: String
    private lateinit var teamBadge: String
    private lateinit var teamName: String
    private lateinit var teamDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_team)

        val intent = intent
        teamID = intent.getStringExtra("teamID")
        teamBadge = intent.getStringExtra("teamBadge")
        teamName = intent.getStringExtra("teamName")
        teamDescription = intent.getStringExtra("teamDescription")

        toolbarTeam.setTitle(teamName)
        setSupportActionBar(toolbarTeam)

        favoriteState()

        tvTeamName.text = teamName
        tvTeamDescription.text = teamDescription
        Picasso.get().load(teamBadge).into(imgTeamBadge)

        val request = ApiRepository()
        val gson = Gson()
        presenter = PlayerPresenter(this, request, gson)
        presenter.getPlayerList(teamID)
    }

    override fun showPlayerList(data: List<Player>) {
        player.addAll(data)
        player.sortBy { it.playerPosition }

        adapter = PlayerViewAdapter(this, player){
            val intent = Intent(applicationContext, DetailPlayerActivity::class.java)
            intent.putExtra("playerBadge", it.playerFace)
            intent.putExtra("playerName", it.playerName)
            intent.putExtra("playerBornPlace", it.playerBornLocation)
            intent.putExtra("playerBornDate", it.playerBorn)
            intent.putExtra("playerGender", it.playerGender)
            intent.putExtra("playerTeam", it.playerTeam)
            intent.putExtra("playerTeamSign", it.playerTeamSign)
            intent.putExtra("playerPosition", it.playerPosition)
            intent.putExtra("playerHeight", it.playerHeight)
            intent.putExtra("playerWeight", it.playerWeight)
            intent.putExtra("playerDescription", it.playerDescription)
            intent.putExtra("playerNationality", it.playerNationality)
            startActivity(intent)
        }
        listPlayer.layoutManager = LinearLayoutManager(this)
        listPlayer.adapter = adapter

        temp.addAll(player)

        searchViewPlayer.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                playerFilter.clear()
                playerFilter.addAll(player)
                if (newText?.length == 0){
                    player.clear()
                    player.addAll(temp)
                    adapter.notifyDataSetChanged()

                }else if (newText == "null"){
                    player.clear()
                    player.addAll(temp)
                    adapter.notifyDataSetChanged()
                }else{
                    val search = newText?.toLowerCase()
                    var a = 0
                    player.clear()
                    while (a < playerFilter.size){
                        val item = playerFilter[a].playerName?.toLowerCase()
                        val item2 = playerFilter[a].playerPosition?.toLowerCase()
                        if (item?.contains(search!!)!!){
                            player.add(playerFilter[a])
                        }else if (item2?.contains(search!!)!!){
                            player.add(playerFilter[a])
                        }
                        a++
                    }
                    adapter.notifyDataSetChanged()
                }
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to teamID)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(FavoriteTeam.TABLE_FAVORITE,
                        FavoriteTeam.FAVORITE_TEAM to "Team",
                        FavoriteTeam.TEAM_ID to teamID,
                        FavoriteTeam.TEAM_NAME to teamName,
                        FavoriteTeam.TEAM_BADGE to teamBadge,
                        FavoriteTeam.TEAM_DESCRIPTION to teamDescription)
            }
            snackbar(linearLayoutTeamDetail, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(linearLayoutTeamDetail, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(FavoriteTeam.TABLE_FAVORITE, "(TEAM_ID = {id})",
                        "id" to teamID)
            }
            snackbar(linearLayoutTeamDetail, "Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(linearLayoutTeamDetail, e.localizedMessage).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }
}
