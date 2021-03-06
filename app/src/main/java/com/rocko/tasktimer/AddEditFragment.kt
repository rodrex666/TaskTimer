package com.rocko.tasktimer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_add_edit.*
import java.lang.RuntimeException

private const val TAG="AddEditFragment"
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TASK = "task"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEditFragment : Fragment() {

    private var task: Task? = null
    private var listener: OnSaveClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate: starts")
        super.onCreate(savedInstanceState)
        task=arguments?.getParcelable<Task?>(ARG_TASK)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView: starts")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated: called")
        if(savedInstanceState==null){
            val task=task
            if (task!=null){
                Log.d(TAG,"onViewCreated: Task details found, editing task ${task.id}")
                addedit_name.setText(task.name)
                addedit_description.setText(task.description)
                addedit_sortorder.setText(Integer.toString(task.sortOrder))

            }else{
                //No task, add new task and NOT edit a existing one
                Log.d(TAG,"onViewCreated: No arguments, adding new record")
            }
        }
    }
    private fun saveTask(){
        //Update database if one field has changed, otherwise dont need to hit db
        val sortOrder=if((addedit_sortorder.text.isNotEmpty())){
            Integer.parseInt(addedit_sortorder.text.toString())
        }else{
            0
        }
        val values = ContentValues()
        val task=task
        if(task!=null)
        {
            Log.d(TAG,"saveTask: updating existing task")
            if(addedit_name.text.toString()!=task.name)
            {
                values.put(TasksContract.Columns.TASK_NAME,addedit_name.text.toString())
            }
            if(addedit_description.text.toString()!=task.description){
                values.put(TasksContract.Columns.TASK_DESCRIPTION,addedit_description.text.toString())
            }
            if(sortOrder!=task.sortOrder){
                values.put(TasksContract.Columns.TASK_SORT_ORDER,sortOrder)
            }
            if (values.size()!=0){
                Log.d(TAG,"saveTask: Updating task")
                activity?.contentResolver?.update(TasksContract.buildUriFromId(task.id),values,null,null)
            }
        }else{
            Log.d(TAG,"saveTask: adding new task")
            if(addedit_name.text.isNotEmpty()){
                values.put(TasksContract.Columns.TASK_NAME,addedit_name.text.toString())
                if (addedit_description.text.isNotEmpty()){
                    values.put(TasksContract.Columns.TASK_DESCRIPTION,addedit_description.text.toString())
                }
                values.put(TasksContract.Columns.TASK_SORT_ORDER,sortOrder) //defaults to zero if empty
                activity?.contentResolver?.insert(TasksContract.CONTENT_URI,values)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG,"onActivityCreated: starts")
        super.onActivityCreated(savedInstanceState)
        if(listener is AppCompatActivity) {
            val actionBar = (listener as AppCompatActivity?)?.supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
       addedit_save.setOnClickListener {
           saveTask()
           listener?.onSaveClicked()
       }
    }

    override fun onAttach(context: Context) {
        Log.d(TAG,"onAttach: starts")
        super.onAttach(context)
        if(context is OnSaveClicked){
            listener=context
        }else{
            throw RuntimeException("$context must implement OnSaveClicked")
        }
    }

    override fun onDetach() {
        Log.d(TAG,"onDetach: starts")
        super.onDetach()
        listener=null
    }

    interface OnSaveClicked{
    fun onSaveClicked()
}
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param task The task to be edited or  null to add new task
         * @return A new instance of fragment AddEditFragment.
         */

        @JvmStatic
        fun newInstance(task: Task?) =
            AddEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TASK,task)
                }
            }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
