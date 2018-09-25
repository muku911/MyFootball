package com.dicoding.myfootball.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.myfootball.R
import com.dicoding.myfootball.data.FullDataLastMatch
import com.dicoding.myfootball.data.FullDataNextMatch
import com.dicoding.myfootball.model.LeagueDetail
import com.dicoding.myfootball.model.Player
import com.dicoding.myfootball.model.Team
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_league.view.*
import kotlinx.android.synthetic.main.item_list_league_last_match.view.*
import kotlinx.android.synthetic.main.item_list_league_next_match.view.*
import java.util.*

class RecyclerViewAdapter(private val context: Context, private val items: List<LeagueDetail>, private val listener: (LeagueDetail) -> Unit)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){


        fun bindItem(items: LeagueDetail, listener: (LeagueDetail) -> Unit) {
            itemView.tv_league.text = items.leagueName
            Picasso.get().load(items.leagueBadge).into(itemView.imgview_icon_league)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}

class TeamViewAdapter(private val context: Context, private val items: List<Team>, private val listener: (Team) -> Unit)
    : RecyclerView.Adapter<TeamViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){


        fun bindItem(items: Team, listener: (Team) -> Unit) {
            itemView.tv_league.text = items.teamName
            Picasso.get().load(items.teamBadge).into(itemView.imgview_icon_league)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}

class PlayerViewAdapter(private val context: Context, private val items: List<Player>, private val listener: (Player) -> Unit)
    : RecyclerView.Adapter<PlayerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        @SuppressLint("SetTextI18n")
        fun bindItem(items: Player, listener: (Player) -> Unit) {
            itemView.tv_league.text = items.playerName+"\t("+items.playerPosition+")"
            Picasso.get().load(items.playerFace).into(itemView.imgview_icon_league)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}

class NextMatchAdapter(private val context: Context, private val items: ArrayList<FullDataNextMatch>, private val listener: (FullDataNextMatch) -> Unit)
    : RecyclerView.Adapter<NextMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league_next_match, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){


        fun bindItem(items: FullDataNextMatch, listener: (FullDataNextMatch) -> Unit) {
            itemView.tv_home.text = items.homeName
            itemView.tv_away.text = items.awayName
            itemView.tv_tanggal.text = items.matchDate
            Picasso.get().load(items.homeBadge).into(itemView.imgview_home)
            Picasso.get().load(items.awayBadge).into(itemView.imgview_away)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}

class LastMatchAdapter(private val context: Context, private val items: ArrayList<FullDataLastMatch>, private val listener: (FullDataLastMatch) -> Unit)
    : RecyclerView.Adapter<LastMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_league_last_match, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){


        fun bindItem(items: FullDataLastMatch, listener: (FullDataLastMatch) -> Unit) {
            itemView.tv_home2.text = items.homeName
            itemView.tv_away2.text = items.awayName
            itemView.tv_tanggal2.text = items.matchDate
            var homeScore = items.homeScore
            var awayScore = items.awayScore
            if (homeScore == "null") homeScore = "0"
            if (awayScore == "null") awayScore = "0"
            itemView.tv_scoreHome2.text = homeScore
            itemView.tv_scoreAway2.text = awayScore
            Picasso.get().load(items.homeBadge).into(itemView.imgview_home2)
            Picasso.get().load(items.awayBadge).into(itemView.imgview_away2)
            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
}