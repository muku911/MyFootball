package com.dicoding.myfootball.main
import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dicoding.myfootball.data.MatchDetail
import com.dicoding.myfootball.db.Favorite
import com.dicoding.myfootball.R
import com.dicoding.myfootball.R.drawable.ic_add_to_favorites
import com.dicoding.myfootball.R.drawable.ic_added_to_favorites
import com.dicoding.myfootball.R.id.add_to_favorite
import com.dicoding.myfootball.R.menu.detail_menu
import com.dicoding.myfootball.db.database
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_match.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DetailMatchActivity : AppCompatActivity() {
    private var detailMatch: ArrayList<MatchDetail> = arrayListOf()

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var fromFragment: String
    private lateinit var matchID: String
    private lateinit var homeBadge: String
    private lateinit var awayBadge: String
    private lateinit var name: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)


        val intent = intent
        val leagueID = intent.getStringExtra("leagueID")
        fromFragment = intent.getStringExtra("fromFragment")
        name = intent.getStringExtra("namaLeague")
        homeBadge = intent.getStringExtra("homeBadge")
        awayBadge = intent.getStringExtra("awayBadge")
        matchID = intent.getStringExtra("leagueID")
        tvLeagueName.text = name
        //Image
        Picasso.get().load(homeBadge).into(imgview_Home)
        Picasso.get().load(awayBadge).into(imgview_Away)

        favoriteState()
        //getdata
        val URL_DETAIL_MATCH = "https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=" + leagueID
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, URL_DETAIL_MATCH, null, Response.Listener {
            var count = 0
            val jsonArray: JSONArray = it.getJSONArray("events")
            while (count < jsonArray.length()){
                try {
                    val jsonObject = jsonArray.getJSONObject(count)
                    val getDetailMatch = MatchDetail(
                            jsonObject.getString("strHomeTeam"), jsonObject.getString("intHomeScore"), jsonObject.getString("strHomeGoalDetails"),
                            jsonObject.getString("strHomeYellowCards"),jsonObject.getString("strHomeRedCards"),
                            jsonObject.getString("strHomeLineupGoalkeeper"),jsonObject.getString("strHomeLineupDefense"),jsonObject.getString("strHomeLineupMidfield"),jsonObject.getString("strHomeLineupForward"),jsonObject.getString("strHomeLineupSubstitutes"),
                            jsonObject.getString("strAwayTeam"), jsonObject.getString("intAwayScore"), jsonObject.getString("strAwayGoalDetails"),
                            jsonObject.getString("strAwayYellowCards"),jsonObject.getString("strAwayRedCards"),
                            jsonObject.getString("strAwayLineupGoalkeeper"),jsonObject.getString("strAwayLineupDefense"),jsonObject.getString("strAwayLineupMidfield"),jsonObject.getString("strAwayLineupForward"),jsonObject.getString("strAwayLineupSubstitutes"),jsonObject.getString("strDate"))
                    detailMatch.add(getDetailMatch)
                    count++
                }catch (e : JSONException){
                    e.printStackTrace()
                }

            }
            //UI TOP
            var homeScore:String? = detailMatch[0].homeScore
            var awayScore:String? = detailMatch[0].awayScore
            if (homeScore == "null") homeScore = "0"
            if (awayScore == "null") awayScore = "0"
            tvTeamHome.text = detailMatch[0].homeName
            tvTeamAway.text = detailMatch[0].awayName
            tvMatchDate.text = detailMatch[0].matchDate
            tvScoreAll.text = "$homeScore:$awayScore"
            //UI Highlights
            tvGoalHome.text = trimStringData(detailMatch[0].homeGoalDetail)
            tvGoalAway.text = trimStringData(detailMatch[0].awayGoalDetail)
            tvYellowCardHome.text = trimStringData(detailMatch[0].homeYellowCard)
            tvYellowCardAway.text = trimStringData(detailMatch[0].awayYellowCard)
            tvRedCardHome.text = trimStringData(detailMatch[0].homeRedCard)
            tvRedCardAway.text = trimStringData(detailMatch[0].awayRedCard)
            //UI Lineups

            tvGoalKeeperHome.text = trimStringData(detailMatch[0].homeGoalKeeper)
            tvGoalKeeperAway.text = trimStringData(detailMatch[0].awayGoalKeeper)
            tvDeffHome.text = trimStringData(detailMatch[0].homeDeffense)
            tvDeffAway.text = trimStringData(detailMatch[0].awayDeffense)
            tvMidfieldHome.text = trimStringData(detailMatch[0].homeMidfield)
            tvMidfieldAway.text = trimStringData(detailMatch[0].awayMidfield)
            tvForwardHome.text = trimStringData(detailMatch[0].homeOffense)
            tvForwardAway.text = trimStringData(detailMatch[0].awayOffense)
            tvSubtitutesHome.text = trimStringData(detailMatch[0].homeSubstitutes)
            tvSubtitutesAway.text = trimStringData(detailMatch[0].awaySubstitutes)


        }, Response.ErrorListener {
            //toast(it.message!!)
            println(it.message)
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)

    }

    private fun trimStringData(stringData: String?): String {
        val charString = stringData?.toCharArray()
        val charSave = CharArray(charString?.size!!)

        var a = 0
        var b = 0
        var checkBreak = false
        while(a < charString.size){
            if(charString[a].equals(';')){
                charString[a] = '\n'
                checkBreak = true
                charSave.set(b, charString[a])
                b++
            }else if(charString[a] == ' ' && checkBreak){
                charSave.set(b, charString[a])
                checkBreak = false
            }else{
                charSave.set(b, charString[a])
                b++
            }
            a++
        }
        var charToString = String(charSave)
        if(charToString == "null") charToString = "\t-\n"
        else if(charToString == "") charToString+="\t-\n"
        return charToString
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
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
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(MATCH_ID = {id})",
                            "id" to matchID)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.FAVORITE_MATCH to fromFragment,
                        Favorite.LEAGUE_NAME to name,
                        Favorite.MATCH_ID to matchID,
                        Favorite.MATCH_DATE to detailMatch[0].matchDate,
                        Favorite.HOME_TEAM_NAME to detailMatch[0].homeName,
                        Favorite.HOME_SCORE to detailMatch[0].homeScore,
                        Favorite.HOME_TEAM_BADGE to homeBadge,
                        Favorite.AWAY_TEAM_NAME to detailMatch[0].awayName,
                        Favorite.AWAY_SCORE to detailMatch[0].awayScore,
                        Favorite.AWAY_TEAM_BADGE to awayBadge)
            }
            snackbar(swipeRefreshMatchDetail, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefreshMatchDetail, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "(MATCH_ID = {id})",
                        "id" to matchID)
            }
            snackbar(swipeRefreshMatchDetail, "Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefreshMatchDetail, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }
}
