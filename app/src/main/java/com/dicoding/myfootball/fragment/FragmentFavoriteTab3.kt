package com.dicoding.myfootball.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.myfootball.R.color.colorAccent
import com.dicoding.myfootball.adapter.FavoriteTeamdapter
import com.dicoding.myfootball.db.FavoriteTeam
import com.dicoding.myfootball.db.database
import com.dicoding.myfootball.main.DetailTeamActivity
import kotlinx.android.synthetic.main.activity_detail_match.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FragmentFavoriteTab3 : Fragment(), AnkoComponent<Context> {
    private var favoriteTeamMatch: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var adapter: FavoriteTeamdapter
    private lateinit var listTeamFavorite: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteTeamMatch.clear()

        adapter = FavoriteTeamdapter(activity!!.baseContext, favoriteTeamMatch){
            ctx.startActivity<DetailTeamActivity>("teamID" to "${it.teamID}",
                    "teamName" to "${it.teamName}",
                    "teamBadge" to "${it.teamBadge}",
                    "teamDescription" to "${it.teamDescription}")
        }


        listTeamFavorite.adapter = adapter
        showFavorite()
        swipeRefresh.onRefresh {
            favoriteTeamMatch.clear()
            showFavorite()
        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_FAVORITE).whereArgs("(FAVORITE_TEAM = {type})", "type" to "Team")
            val getFavoriteTeam = result.parseList(classParser<FavoriteTeam>())
            favoriteTeamMatch.addAll(getFavoriteTeam)
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

                    listTeamFavorite = recyclerView {
                        lparams (width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }
                }
            }
        }
    }
}
