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
import com.dicoding.myfootball.adapter.LastMatchAdapter
import com.dicoding.myfootball.data.FullDataLastMatch
import com.dicoding.myfootball.data.GetBadgeMatch
import com.dicoding.myfootball.data.ListLastMatch
import com.dicoding.myfootball.data.ListTeam
import com.dicoding.myfootball.main.DetailMatchActivity
import kotlinx.android.synthetic.main.fragment_tab2_league_schedule.*
import kotlinx.android.synthetic.main.fragment_tab2_league_schedule.view.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class FragmentTab2: Fragment() {
    private var lastMatch: ArrayList<ListLastMatch> = arrayListOf()
    private var teamDetail: ArrayList<GetBadgeMatch> = arrayListOf()
    private var listTeam: ArrayList<ListTeam> = arrayListOf()
    private var fullDataLastMatch: ArrayList<FullDataLastMatch> = arrayListOf()
    private var fullDataLastMatchFilter: ArrayList<FullDataLastMatch> = arrayListOf()
    private var temp: ArrayList<FullDataLastMatch> = arrayListOf()

    private lateinit var mRunnable:Runnable
    private lateinit var mHandler: Handler

    private var URL_LAST_MATCH: String? = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id="
    private var leagueName: String? = ""
    private var leagueID: String? = ""

    private lateinit var rootView: View

    private lateinit var adapter: LastMatchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_tab2_league_schedule, container, false)
        mHandler = Handler()

        leagueID = arguments?.getString("leagueID")
        leagueName = arguments?.getString("leagueName")
        URL_LAST_MATCH += leagueID

        getLastMatchList(URL_LAST_MATCH)

        rootView.swipeRefreshLayouts.setOnRefreshListener {
            mRunnable = Runnable {
                showLoading()
                lastMatch.clear()
                teamDetail.clear()
                listTeam.clear()
                getLastMatchList(URL_LAST_MATCH)

                // Hide swipe to refresh icon animation
                rootView.swipeRefreshLayouts.isRefreshing = false
            }
            mHandler.postDelayed(
                    mRunnable,
                    (10).toLong() // Delay 1 to 5 seconds
            )
        }

        return rootView
    }

    private fun getLastMatchList(URL_LAST_MATCH: String?) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL_LAST_MATCH, null, Response.Listener {
            var count = 0
            val URL_TEAM = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="
            if(it.getString("events") != "null") {
                val jsonArray: JSONArray = it.getJSONArray("events")
                while (count < jsonArray.length()) {
                    try {
                        val jsonObject = jsonArray.getJSONObject(count)
                        val listLastMatch = ListLastMatch(
                                jsonObject.getString("strHomeTeam"), jsonObject.getString("idHomeTeam"), jsonObject.getString("intHomeScore"),
                                jsonObject.getString("strAwayTeam"), jsonObject.getString("idAwayTeam"), jsonObject.getString("intAwayScore"),
                                jsonObject.getString("strDate"), jsonObject.getString("idEvent"))
                        lastMatch.add(listLastMatch)
                        count++
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                var a = 0
                var b = 0
                while (a < lastMatch.size * 2) {
                    if (a < lastMatch.size) {
                        getTeamDetail(URL_TEAM + lastMatch[a].homeID)
                        a++
                    } else {
                        getTeamDetail(URL_TEAM + lastMatch[b].awayID)
                        b++
                        a++
                    }
                }
            }
            else{
                longToast("No Last Match In This League")
                searchLastMatch.visibility = View.GONE
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
            if (listTeam.size == lastMatch.size * 2) getTeamBadge()

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
        while(countSum < lastMatch.size){
            val homeID = lastMatch[countSum].homeID
            val awayID = lastMatch[countSum].awayID
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
            val getDataLastMatch = FullDataLastMatch(
                    lastMatch[a].homeName, lastMatch[a].homeID, lastMatch[a].homeScore,
                    lastMatch[a].awayName, lastMatch[a].awayID, lastMatch[a].awayScore,
                    teamDetail[a].homeBadge, teamDetail[a].awayBadge,
                    lastMatch[a].matchDate,lastMatch[a].matchID)
            fullDataLastMatch.add(getDataLastMatch)
            a++
        }
        showListData()
    }

    private fun showLoading(){
        fullDataLastMatch.clear()
        rootView.progressBar2.visible()
    }

    private fun hideLoading(){
        rootView.progressBar2.invisible()
    }

    private fun showListData(){
        hideLoading()
        println(fullDataLastMatch)
        if (fullDataLastMatch.size != 0) {

            adapter = LastMatchAdapter(activity!!.baseContext, fullDataLastMatch) {
                //Item click
                val intent = Intent(activity!!.baseContext, DetailMatchActivity::class.java)
                intent.putExtra("fromFragment", "Last Match")
                intent.putExtra("namaLeague", leagueName)
                intent.putExtra("leagueID", it.matchID)
                intent.putExtra("homeBadge", it.homeBadge)
                intent.putExtra("awayBadge", it.awayBadge)
                startActivity(intent)
            }

            last_match_list.layoutManager = LinearLayoutManager(activity!!.baseContext)
            last_match_list?.adapter = adapter
        }

        temp.addAll(fullDataLastMatch)

        searchLastMatch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fullDataLastMatchFilter.clear()
                fullDataLastMatchFilter.addAll(fullDataLastMatch)
                if (newText?.length == 0){
                    fullDataLastMatch.clear()
                    fullDataLastMatch.addAll(temp)
                    adapter.notifyDataSetChanged()

                }else if (newText == "null"){
                    fullDataLastMatch.clear()
                    fullDataLastMatch.addAll(temp)
                    adapter.notifyDataSetChanged()
                }else{
                    val search = newText?.toLowerCase()
                    var a = 0
                    fullDataLastMatch.clear()
                    while (a < fullDataLastMatchFilter.size){
                        val item = fullDataLastMatchFilter[a].awayName?.toLowerCase()
                        val item2 = fullDataLastMatchFilter[a].homeName?.toLowerCase()
                        if (item?.contains(search!!)!!){
                            fullDataLastMatch.add(fullDataLastMatchFilter[a])
                        }else if(item2?.contains(search!!)!!){
                            fullDataLastMatch.add(fullDataLastMatchFilter[a])
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