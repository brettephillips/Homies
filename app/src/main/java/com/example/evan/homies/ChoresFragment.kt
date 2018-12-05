package com.example.evan.homies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.evan.homies.viewmodels.ChoreViewModel
import android.arch.lifecycle.Observer
import android.support.design.widget.FloatingActionButton
import android.widget.TextView
import android.widget.Toast
import com.example.evan.homies.R.string.chores
import com.example.evan.homies.entities.Chore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

class ChoresFragment : Fragment(), AddChoreDialogFragment.OnChoreAddDialogFinishedListener {
    private lateinit var choreViewModel: ChoreViewModel
    private var userId: Long? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: ChoreAdapter? = null
    private var choreNames: MutableList<String> = mutableListOf()
    private var choreDates: MutableList<String> = mutableListOf()
    private var choreAssignees: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments!!.getLong(USER_ID,0)
        if (userId == 0.toLong()) {
            //TODO: no user found, redirect to login
        } else {
            choreViewModel = ChoreViewModel(activity?.application!!)
            adapter = ChoreAdapter()

            choreViewModel.getCurrentHouse()
                    .observe(this, Observer { house ->
                        if(house?.name != null || house?.name != "") {
                            doAsync {
                                choreViewModel!!.getAllRooms(house!!.id!!)
                            }
                        }
                    })

            choreViewModel.getCurrentRooms()
                    .observe(this, Observer { rooms ->
                        for(room in rooms!!) {
                            for(chore in room.chores) {
                                choreNames.add(chore.name)
                                choreDates.add(chore.dateDue)
                                choreAssignees.add(chore.userID.toString())
                            }
                        }

                        adapter!!.taskNames = choreNames
                        adapter!!.taskDates = choreDates
                        adapter!!.taskAssignees = choreAssignees

                        adapter!!.notifyDataSetChanged()
                    })

            //static room for now
            doAsync {
                choreViewModel.getUsersHouse(userId!!)
            }

            if(choreNames.size > 0) {
                println("CHORES, SO WE NEED TO REMOVE MESSAGE")
            } else {
                println("NO CHORES KEEP MESSAGE ON SCREEN")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_chore_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_chore)

        layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.fab_add_chore).setOnClickListener {
            val dialog = AddChoreDialogFragment.newInstance()
            dialog.show(fragmentManager!!, "AddChoreDialog")
        }

        return view
    }

    override fun onChoreAddDialogFinished(chore: Chore) {
        doAsync {
            //choreViewModel.addChore(chore) //creates the chore
            uiThread {
                //Toast.makeText(context,"${house.name} has been created", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
