package com.dicoding.myfootball.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.dicoding.myfootball.R
import com.dicoding.myfootball.adapter.TeamViewAdapter
import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.model.Team
import com.dicoding.myfootball.utils.invisible
import com.dicoding.myfootball.utils.visible
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamActivity : AppCompatActivity(), TeamView {

    private var team: MutableList<Team> = mutableListOf()
    private var teamFilter: ArrayList<Team> = arrayListOf()
    private lateinit var adapter: TeamViewAdapter
    private lateinit var presenter: TeamPresenter
    private lateinit var listLeague: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val leagueId = intent.getStringExtra("leagueID")

        team.clear()

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(2)
            leftPadding = dip(2)
            rightPadding = dip(2)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(android.R.color.holo_purple,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    listLeague = recyclerView {
                        id = R.id.list_team
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }

        adapter = TeamViewAdapter(this, team){
            ctx.startActivity<DetailTeamActivity>("teamID" to "${it.teamId}",
                    "teamBadge" to "${it.teamBadge}",
                    "teamName" to "${it.teamName}",
                    "teamDescription" to "${it.teamDescription}")
        }
        listLeague.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamPresenter(this, request, gson)
        presenter.getTeamList(leagueId)

        swipeRefresh.onRefresh {
            team.clear()
            presenter.getTeamList(leagueId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val intent = intent
        val leagueId = intent.getStringExtra("leagueID")

        menuInflater.inflate(R.menu.main_search, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        if(searchItem != null){
            val searchView = searchItem.actionView as android.support.v7.widget.SearchView
            searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    teamFilter.clear()
                    teamFilter.addAll(team)

                    if (newText?.length == 0){
                        team.clear()
                        presenter.getTeamList(leagueId)

                    }else if (newText == "null"){
                        team.clear()
                        presenter.getTeamList(leagueId)
                    }else{
                        val search = newText?.toLowerCase()
                        var a = 0
                        team.clear()
                        while (a < teamFilter.size){
                            val item = teamFilter[a].teamName?.toLowerCase()
                            if (item?.contains(search!!)!!){
                                team.add(teamFilter[a])
                            }
                            a++
                        }
                        team.sortBy { it.teamName }
                        adapter.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeamList(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        team.addAll(data)
        team.sortBy { it.teamName }
        adapter.notifyDataSetChanged()
    }
}
