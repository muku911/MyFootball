package com.dicoding.myfootball.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.dicoding.myfootball.R
import com.dicoding.myfootball.adapter.RecyclerViewAdapter
import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.model.League
import com.dicoding.myfootball.model.LeagueDetail
import com.dicoding.myfootball.utils.invisible
import com.dicoding.myfootball.utils.visible
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class LeagueActivity : AppCompatActivity(), MainView {
    private var items: MutableList<League> = mutableListOf()
    private var leagueDetail: MutableList<LeagueDetail> = mutableListOf()
    private var leagueDetailFilter: ArrayList<LeagueDetail> = arrayListOf()
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var presenter: MainPresenter
    private lateinit var listLeague: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val typeName = intent.getStringExtra("typeName")

        items.clear()
        leagueDetail.clear()

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
                        id = R.id.list_league
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

        adapter = RecyclerViewAdapter(this, leagueDetail){
            if(typeName == "List Match") {
                ctx.startActivity<LeagueScheduleActivity>("namaLeague" to "${it.leagueName}",
                        "leagueID" to "${it.leagueId}")
            }else if(typeName == "List Team"){
                ctx.startActivity<TeamActivity>("namaLeague" to "${it.leagueName}",
                        "leagueID" to "${it.leagueId}")
            }
        }

        listLeague.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)
        presenter.getLeagueList()

        swipeRefresh.onRefresh {
            items.clear()
            leagueDetail.clear()
            presenter.getLeagueList()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_search, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        if(searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    leagueDetailFilter.clear()
                    leagueDetailFilter.addAll(leagueDetail)

                    if (newText?.length == 0){
                        items.clear()
                        leagueDetail.clear()
                        presenter.getLeagueList()

                    }else if (newText == "null"){
                        items.clear()
                        leagueDetail.clear()
                        presenter.getLeagueList()
                    }else{
                        val search = newText?.toLowerCase()
                        var a = 0
                        leagueDetail.clear()
                        while (a < leagueDetailFilter.size){
                            val item = leagueDetailFilter[a].leagueName?.toLowerCase()
                            if (item?.contains(search!!)!!){
                                leagueDetail.add(leagueDetailFilter[a])
                            }
                            a++
                        }
                        leagueDetail.sortBy { it.leagueName }
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

    override fun showLeagueList(data: List<League>) {
        swipeRefresh.isRefreshing = false
        items.addAll(data)
        var a = 0
        while (a < items.size){
            if(items[a].leagueType == "Soccer") presenter.getLeagueDetail(items[a].leagueId!!)
            a++
        }

    }

    override fun showLeagueDetail(data: List<LeagueDetail>) {
        swipeRefresh.isRefreshing = false
        leagueDetail.addAll(data)
        leagueDetail.sortBy { it.leagueName }
        adapter.notifyDataSetChanged()
    }
}

