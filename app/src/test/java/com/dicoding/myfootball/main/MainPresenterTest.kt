package com.dicoding.myfootball.main

import com.dicoding.myfootball.api.ApiRepository
import com.dicoding.myfootball.api.DBApi
import com.dicoding.myfootball.model.League
import com.dicoding.myfootball.model.LeagueDetail
import com.dicoding.myfootball.model.LeagueDetailResponse
import com.dicoding.myfootball.model.LeagueResponse
import com.dicoding.myfootball.test.TestContextProvider
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private
    lateinit var view: MainView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun getLeagueList() {
        val leagueList: MutableList<League> = mutableListOf()
        val response = LeagueResponse(leagueList)

        `when`(gson.fromJson(apiRepository
                 .doRequest(DBApi.getLeague()),
                LeagueResponse::class.java
        )).thenReturn(response)

        presenter.getLeagueList()

        verify(view).showLoading()
        verify(view).showLeagueList(leagueList)
        verify(view).hideLoading()
    }

    @Test
    fun getLeagueDetail() {
        val leagueDetail: MutableList<LeagueDetail> = mutableListOf()
        val response = LeagueDetailResponse(leagueDetail)
        val leagueID = "4328"

        `when`(gson.fromJson(apiRepository
                .doRequest(DBApi.getDetailLeague(leagueID)),
                LeagueDetailResponse::class.java
        )).thenReturn(response)

        presenter.getLeagueDetail(leagueID)

        verify(view).showLoading()
        verify(view).showLeagueDetail(leagueDetail)
        verify(view).hideLoading()
    }
}