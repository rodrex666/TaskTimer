package com.rocko.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.AccessControlContext

private const val TAG="AppDatabase"
private const val DATABASE_NAME="TaskTimer.db"
private const val DATABASE_VERSION=1
internal class AppDatabase constructor(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION){
    init {
        Log.d(TAG,"AppDatabase: initialising")
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG,"onCreate Starts")
        val sSQL="""CREATE TABLE ${TasksContract.TABLE_NAME} ( 
            |${TasksContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            |${TasksContract.Columns.TASK_NAME} TEXT NOT NULL,
            |${TasksContract.Columns.TASK_DESCRIPTION} TEXT,
            |${TasksContract.Columns.TASK_SORT_ORDER} INTEGER);""".replaceIndent(" ")
        Log.d(TAG,sSQL)
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG,"onUpgrade Starts")
        when(oldVersion){
            1 ->{
                //upgrade von version 1
            }
            else -> throw IllegalStateException("onUpgrade with unknow newVersion $newVersion")
        }
    }
    companion object: SingletonHolder<AppDatabase,Context>(::AppDatabase)
}