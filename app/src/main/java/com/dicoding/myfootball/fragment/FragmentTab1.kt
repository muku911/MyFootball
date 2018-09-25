package com.dicoding.myfootball.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dicoding.myfootball.R
import com.dicoding.myfootball.adapter.NextMatchAdapter
import com.dicoding.myfootball.data.FullDataNextMatch
import com.dicoding.myfootball.data.GetBadgeMatch
import com.dicoding.myfootball.data.ListNextMatch
import com.dicoding.myfootball.data.ListTeam
import com.dicoding.myfootball.main.DetailMatchActivity
import kotlinx.android.synthetic.main.fragment_tab1_league_schedule.*
import kotlinx.android.synthetic.main.fragment_tab1_league_schedule.view.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class FragmentTab1: Fragment(){

    private var nextMatch: ArrayList<ListNextMatch> = arrayListOf()
    private var teamDetail: ArrayList<GetBadgeMatch> = arrayListOf()
    private var listTeam: ArrayList<ListTeam> = arrayListOf()
    private var fullDataNextMatch: ArrayList<FullDataNextMatch> = arrayListOf()
    private var fullDataNextMatchFilter: ArrayList<FullDataNextMatch> = arrayListOf()
    private var temp: ArrayList<FullDataNextMatch> = arrayListOf()
    private lateinit var adapter: NextMatchAdapter
    private lateinit var mRunnable:Runnable
    private lateinit var mHandler: Handler

    private var URL_NEXT_MATCH: String? = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id="
    private lateinit var URL_GO: String
    private lateinit var leagueName: String
    private lateinit var leagueID: String
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_tab1_league_schedule, container, false)
        mHandler = Handler()

        leagueID = arguments?.getString("leagueID").toString()
        leagueName = arguments?.getString("leagueName").toString()
        URL_GO = URL_NEXT_MATCH + leagueID

        showLoading()
        getNextMatchList(URL_GO)

        rootView.swipeRefreshLayout.setOnRefreshListener {
            mRunnable = Runnable {
                showLoading()
                getNextMatchList(URL_GO)

                // Hide swipe to refresh icon animation
                rootView.swipeRefreshLayout.isRefreshing = false
            }
            mHandler.postDelayed(
                    mRunnable,
                    (10).toLong()
            )
        }

        return rootView
    }

    private fun getNextMatchList(URL_NEXT_MATCH: String?) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL_NEXT_MATCH, null, Response.Listener {
            var count = 0
            val URL_TEAM = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="
            if(it.getString("events") != "null") {
                val jsonArray: JSONArray = it.getJSONArray("events")
                while (count < jsonArray.length()) {
                    try {
                        val jsonObject = jsonArray.getJSONObject(count)
                        val listNextMatch = ListNextMatch(jsonObject.getString("strHomeTeam"),
                                jsonObject.getString("idHomeTeam"),
                                jsonObject.getString("strAwayTeam"),
                                jsonObject.getString("idAwayTeam"),
                                jsonObject.getString("strDate"),
                                jsonObject.getString("idEvent"))
                        nextMatch.add(listNextMatch)
                        count++
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                var a = 0
                var b = 0
                while (a < nextMatch.size * 2) {
                    if (a < nextMatch.size) {
                        getTeamDetail(URL_TEAM + nextMatch[a].homeID)
                        a++
                    } else {
                        getTeamDetail(URL_TEAM + nextMatch[b].awayID)
                        b++
                        a++
                    }
                }
            }
            else{

                longToast("No Next Match In This League")
                searchNextMatch.visibility = View.GONE
                hideLoading()
            }

        }, Response.ErrorListener {
            //toast(it.message!!)
            println(it.message)
        })

        val requestQueue: RequestQueue? = Volley.newRequestQueue(activity?.baseContext)
        requestQueue?.add(jsonObjectRequest)
    }

    private fun getTeamDetail(URL_TEAM: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL_TEAM, null, Response.Listener {
            var count = 0
            val jsonArray: JSONArray = it.getJSONArray("teams")
            while (count < jsonArray.length()){
                try {
                    val jsonObject = jsonArray.getJSONObject(count)
                    val getListTeam = ListTeam(jsonObject.getString("idTeam"),
                            jsonObject.getString("strTeam"),
                            jsonObject.getString("strTeamBadge"))
                    listTeam.add(getListTeam)
                    count++
                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }
            println(listTeam.size)
            if (listTeam.size == nextMatch.size * 2) getTeamBadge()

        }, Response.ErrorListener {
            //toast(it.message!!)
            println(it.message)
        })

        val requestQueue: RequestQueue? = Volley.newRequestQueue(activity?.baseContext)
        requestQueue?.add(jsonObjectRequest)
    }

    private fun getTeamBadge() {
        toast("Data Loaded")
        var countSum = 0
        var counting = 0
        var homeBadge = ""
        var awayBadge = ""
        var check = 0
        while(countSum < nextMatch.size){
            val homeID = nextMatch[countSum].homeID
            val awayID = nextMatch[countSum].awayID
            while (counting < listTeam.size){
                if(homeID.equals(listTeam[counting].teamID)){
                    homeBadge = listTeam[counting].teamBadge!!
                    check++
                    break
                }
                counting++
            }
            counting = 0
            while (counting < listTeam.size){
                if(awayID.equals(listTeam[counting].teamID)){
                    awayBadge = listTeam[counting].teamBadge!!
                    check++
                    break
                }
                counting++
            }
            if(check == 2){
                val getBadgeMatch = GetBadgeMatch(homeBadge, awayBadge)
                teamDetail.add(getBadgeMatch)
                check = 0
                counting = 0
                countSum++
            }

        }
        var a = 0
        while (a < teamDetail.size){
            val getDataNextMatch = FullDataNextMatch(
                    nextMatch[a].homeName, nextMatch[a].homeID,
                    nextMatch[a].awayName, nextMatch[a].awayID,
                    teamDetail[a].homeBadge, teamDetail[a].awayBadge,
                    nextMatch[a].dateMatch, nextMatch[a].matchID)
            fullDataNextMatch.add(getDataNextMatch)
            a++
        }
        showListData()
    }

    private fun showLoading(){
        nextMatch.clear()
        teamDetail.clear()
        listTeam.clear()
        fullDataNextMatch.clear()
        rootView.progressBar1.visible()
    }

    private fun hideLoading(){
        rootView.progressBar1.invisible()
    }

    private fun showListData(){
        hideLoading()
        println(fullDataNextMatch)
        if(fullDataNextMatch.size != 0) {

            adapter = NextMatchAdapter(activity!!.baseContext, fullDataNextMatch) {
                //Item click
                val intent = Intent(activity!!.baseContext, DetailMatchActivity::class.java)
                intent.putExtra("fromFragment", "Next Match")
                intent.putExtra("namaLeague", leagueName)
                intent.putExtra("leagueID", it.matchID)
                intent.putExtra("homeBadge", it.homeBadge )
                intent.putExtra("awayBadge", it.awayBadge)
                startActivity(intent)
            }
            next_match_list?.layoutManager = LinearLayoutManager(activity!!.baseContext)
            next_match_list?.adapter = adapter
        }

        temp.addAll(fullDataNextMatch)

        searchNextMatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fullDataNextMatchFilter.clear()
                fullDataNextMatchFilter.addAll(fullDataNextMatch)
                if (newText?.length == 0){
                    fullDataNextMatch.clear()
                    fullDataNextMatch.addAll(temp)
                    adapter.notifyDataSetChanged()

                }else if (newText == "null"){
                    fullDataNextMatch.clear()
                    fullDataNextMatch.addAll(temp)
                    adapter.notifyDataSetChanged()
                }else{
                    val search = newText?.toLowerCase()
                    var a = 0
                    fullDataNextMatch.clear()
                    while (a < fullDataNextMatchFilter.size){
                        val item = fullDataNextMatchFilter[a].awayName?.toLowerCase()
                        val item2 = fullDataNextMatchFilter[a].homeName?.toLowerCase()
                        if (item?.contains(search!!)!!){
                            fullDataNextMatch.add(fullDataNextMatchFilter[a])
                        }else if(item2?.contains(search!!)!!){
                            fullDataNextMatch.add(fullDataNextMatchFilter[a])
                        }
                        a++
                    }
                    adapter.notifyDataSetChanged()
                }
                return true
            }

        })
    }

    private fun View.visible() {
        visibility = View.VISIBLE
    }

    private fun View.invisible() {
        visibility = View.GONE
    }

}
