package com.dicoding.myfootball.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.myfootball.R.color.colorAccent
import com.dicoding.myfootball.adapter.FavoriteMatchAdapter
import com.dicoding.myfootball.db.Favorite
import com.dicoding.myfootball.db.database
import com.dicoding.myfootball.main.DetailMatchActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FragmentFavoriteTab1 : Fragment(), AnkoComponent<Context> {
    private var favoriteNextMatch: MutableList<Favorite> = mutableListOf()
    private lateinit var adapter: FavoriteMatchAdapter
    private lateinit var listNextEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteNextMatch.clear()


        adapter = FavoriteMatchAdapter(activity!!.baseContext, favoriteNextMatch){
            ctx.startActivity<DetailMatchActivity>("leagueID" to "${it.matchId}",
                    "namaLeague" to "${it.leagueName}",
                    "homeBadge" to "${it.homeTeamBadge}",
                    "awayBadge" to "${it.awayTeamBadge}",
                    "fromFragment" to "Next Match")
        }

        listNextEvent.adapter = adapter
        showFavorite()
        swipeRefresh.onRefresh {
            favoriteNextMatch.clear()
            showFavorite()
        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result2 = select(Favorite.TABLE_FAVORITE).whereArgs("(FAVORITE_MATCH = {type})", "type" to "Next Match")
            val getFavoriteNextMatch = result2.parseList(classParser<Favorite>())
            favoriteNextMatch.addAll(getFavoriteNextMatch)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    @SuppressLint("SetTextI18n")
    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        verticalLayout {
            lparams (width = matchParent, height = wrapContent)
            topPadding = dip(2)
            leftPadding = dip(2)
            rightPadding = dip(2)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                verticalLayout {
                    listNextEvent = recyclerView {
                        lparams (width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }
                }
            }
        }
    }
}
