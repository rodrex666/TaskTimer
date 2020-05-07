package com.rocko.tasktimer

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_list_items.view.*
import java.lang.IllegalStateException

class TaskViewHolder(override  val containerView: View):RecyclerView.ViewHolder(containerView),LayoutContainer{

}
private const val TAG="CursorRecyclerViewAda"
class CursorRecyclerViewAdapter(private var cursor:Cursor?):RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.d(TAG,"onCreateViewHolder: new view request")
        val view=LayoutInflater.from(parent.context).inflate(R.layout.task_list_items,parent,false)
        return TaskViewHolder(view)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder: starts")
        val cursor=cursor // no problem with smart cast
        if(cursor==null||cursor.count==0){
            Log.d(TAG, "onBindViewHolder: providing instructions")
            holder.containerView.tit_name.text = "Instructions"
            holder.containerView.tli_description.text = "meta meta meta"
            holder.containerView.til_edit.visibility=View.GONE
            holder.containerView.til_delete.visibility=View.GONE

        }else{
            if (!cursor.moveToPosition(position))
                throw IllegalStateException("Couldn't move cursor to $position")
            //create a Task object from data in the cursor
            val task=Task(
                cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASK_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASK_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(TasksContract.Columns.TASK_SORT_ORDER))
            //id is not set in constructor
            )
            task.id=cursor.getLong(cursor.getColumnIndex(TasksContract.Columns.ID))

            holder.containerView.tit_name.text=task.name
            holder.containerView.tli_description.text=task.description
            holder.containerView.til_edit.visibility=View.VISIBLE
            holder.containerView.til_delete.visibility=View.VISIBLE

        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG,"getItemCount: starts")
        return 0

    }


}