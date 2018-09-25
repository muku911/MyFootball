package com.dicoding.myfootball.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.dicoding.myfootball.R
import com.dicoding.myfootball.fragment.FragmentTab1
import com.dicoding.myfootball.fragment.FragmentTab2
import kotlinx.android.synthetic.main.activity_league_schedule.*

class LeagueScheduleActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var name: String? = ""
    private var leagueID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_schedule)

        val intent = intent
        name = intent.getStringExtra("namaLeague")
        leagueID = intent.getStringExtra("leagueID")
        println("league ID: " + leagueID)
        toolbar.setTitle(name)
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            val intent = intent
            val name = intent.getStringExtra("namaLeague")
            val leagueID = intent.getStringExtra("leagueID")
            var fragment : Fragment? = null
            val bundle = Bundle()
            bundle.putString("leagueID", leagueID)
            bundle.putString("leagueName", name)
            when(position){
                0 -> {
                    fragment = FragmentTab1()
                }
                1 -> {
                    fragment = FragmentTab2()
                }
            }
            fragment?.arguments = bundle
            return fragment
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
