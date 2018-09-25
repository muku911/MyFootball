package com.dicoding.myfootball.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by root on 2/6/18.
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteMatch4.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(Favorite.TABLE_FAVORITE, true,
                Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Favorite.FAVORITE_MATCH to TEXT,
                Favorite.LEAGUE_NAME to TEXT,
                Favorite.MATCH_ID to TEXT + UNIQUE,
                Favorite.MATCH_DATE to TEXT,
                Favorite.HOME_TEAM_NAME to TEXT,
                Favorite.HOME_SCORE to TEXT,
                Favorite.HOME_TEAM_BADGE to TEXT,
                Favorite.AWAY_TEAM_NAME to TEXT,
                Favorite.AWAY_SCORE to TEXT,
                Favorite.AWAY_TEAM_BADGE to TEXT)

        db.createTable(FavoriteTeam.TABLE_FAVORITE, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.FAVORITE_TEAM to TEXT,
                FavoriteTeam.TEAM_ID to TEXT,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT + UNIQUE,
                FavoriteTeam.TEAM_DESCRIPTION to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(Favorite.TABLE_FAVORITE, true)
        db.dropTable(FavoriteTeam.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)