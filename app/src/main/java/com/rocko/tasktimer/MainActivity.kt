package com.rocko.tasktimer

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),AddEditFragment.OnSaveClicked {

    //wheter or activity is in 2 pane mode
    //running in landscape or tablet
    private var mTwoPane=false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mTwoPane=resources.configuration.orientation==Configuration.ORIENTATION_LANDSCAPE

        var fragment=supportFragmentManager.findFragmentById(R.id.task_details_container)
        if(fragment!=null){
            //there was an existing fragment to edit a task, make sure the panes are set correctly
           showEditPane()
        }else{
            task_details_container.visibility=if(mTwoPane)View.INVISIBLE else View.GONE
            nav_host_fragment.visibility=View.VISIBLE
        }
        Log.d(TAG,"onCreate: finished")

    }
    private fun showEditPane()
    {
        task_details_container.visibility=View.VISIBLE
        //hide left hand pane, if in single pane view
        nav_host_fragment.visibility=if(mTwoPane)View.VISIBLE else View.GONE
    }
    private fun removeEditPane(fragment: Fragment?=null)
    {
        Log.d(TAG,"removeEditPane called")

        if(fragment!=null)
        {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        //Set the visibility of the right hand pane
        task_details_container.visibility=if (mTwoPane) View.INVISIBLE else View.GONE
        //show the left hand pane
        nav_host_fragment.visibility=View.VISIBLE
    }
    override fun onSaveClicked() {
        Log.d(TAG,"onSaveClicked :called")
        var fragment=supportFragmentManager.findFragmentById(R.id.task_details_container)
        removeEditPane(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menumain_addTask-> taskEditRequest(null)
//            R.id.menumain_settings -> true

        }
        return  super.onOptionsItemSelected(item)
    }
    private fun taskEditRequest(task:Task?){
        Log.d(TAG,"taskEditRequest: start")

        val newFragment=AddEditFragment.newInstance(task)
        supportFragmentManager.beginTransaction().replace(R.id.task_details_container,newFragment).commit()
        showEditPane()
        Log.d(TAG,"Exiting taskEditRequest")
    }
}
