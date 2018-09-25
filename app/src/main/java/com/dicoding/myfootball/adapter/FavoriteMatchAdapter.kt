package com.dicoding.myfootball.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.myfootball.db.Favorite
import com.dicoding.myfootball.R
import com.dicoding.myfootball.db.FavoriteTeam
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_league.view.*
import kotlinx.android.synthetic.main.item_list_league_last_match.view.*

/**
 * Created by root on 1/16/18.
 */

class FavoriteMatchAdapter(private val context: Context, private val items: List<Favorite>, private val listener: (Favorite) -> Unit)
    : RecyclerView.Adapter<FavoriteMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league_last_match, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindItem(items: Favorite, listener: (Favorite) -> Unit) {
            itemView.tv_home2.text = items.homeTeamName
            itemView.tv_away2.text = items.awayTeamName
            itemView.tv_tanggal2.text = items.matchDate
            var homeScore = items.homeScore
            var awayeScore = items.awayScore
            if(homeScore == "null") homeScore = "0"
            if(awayeScore == "null") awayeScore = "0"
            itemView.tv_scoreHome2.text = homeScore
            itemView.tv_scoreAway2.text = awayeScore
            Picasso.get().load(items.homeTeamBadge).into(itemView.imgview_home2)
            Picasso.get().load(items.awayTeamBadge).into(itemView.imgview_away2)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}

class FavoriteTeamdapter(private val context: Context, private val items: List<FavoriteTeam>, private val listener: (FavoriteTeam) -> Unit)
    : RecyclerView.Adapter<FavoriteTeamdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindItem(items: FavoriteTeam, listener: (FavoriteTeam) -> Unit) {
            itemView.tv_league.text = items.teamName
            Picasso.get().load(items.teamBadge).into(itemView.imgview_icon_league)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}