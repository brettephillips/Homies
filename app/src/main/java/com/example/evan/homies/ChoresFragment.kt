package com.example.evan.homies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.evan.homies.viewmodels.ChoreViewModel
import android.arch.lifecycle.Observer
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.widget.*
import androidx.annotation.UiThread
import com.example.evan.homies.entities.Chore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

class ChoresFragment : Fragment(), AddChoreDialogFragment.OnChoreAddDialogFinishedListener, ChoreAdapter.OnChoreCheckCompleted{
    private lateinit var choreViewModel: ChoreViewModel
    private var totalChores: Int = 0
    private var userId: Long? = null
    private var houseId: Long? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: ChoreAdapter? = null
    private var userMappings: MutableMap<Long, String> = mutableMapOf()
    private var roomMappings: MutableMap<Long, String> = mutableMapOf()
    private var choresList: MutableList<Chore>? = null
    private var filterName: String? = null
    var filterList: MutableMap<Long, String> = mutableMapOf()

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
                            houseId = house!!.id!!

                            doAsync {
                                var userList = choreViewModel!!.getAllUsers(houseId!!)
                                for(user in userList) {
                                    userMappings.put(user.id, "${user.firstName} ${user.lastName}")
                                }

                                choreViewModel!!.getAllRooms(houseId!!)

                                if(filterName == null) {
                                    filterName = "All"

                                    uiThread {
                                        setupFilter()
                                        return@uiThread
                                    }
                                }
                            }
                        }
                    })

            choreViewModel.getCurrentRooms()
                    .observe(this, Observer { rooms ->
                        val choreNames: MutableList<String> = mutableListOf()
                        val choreDates: MutableList<String> = mutableListOf()
                        val choreAssignees: MutableList<String> = mutableListOf()
                        choresList = mutableListOf()
                        totalChores = 0

                        for(room in rooms!!) {
                            roomMappings.put(room.room!!.id, room.room!!.name)

                            for(chore in room.chores) {
                                if(filterName != userMappings[chore.userID] && filterName != "All") {
                                    continue
                                }

                                if(chore.completed == true) {
                                    continue
                                }

                                choreNames.add(chore.name)
                                choreDates.add(chore.dateDue)
                                var firstInitial = userMappings[chore.userID]!!.substring(0, 1)
                                var space = userMappings[chore.userID]!!.indexOf(" ") + 1
                                var lastInitial = userMappings[chore.userID]!!.substring(space, space + 1)
                                choreAssignees.add(firstInitial+lastInitial)

                                choresList!!.add(chore)
                                totalChores++
                            }
                        }

                        if(totalChores > 0) {
                            view!!.findViewById<TextView>(R.id.no_chores_message).visibility = View.GONE
                        } else {
                            view!!.findViewById<TextView>(R.id.no_chores_message).visibility = View.VISIBLE
                        }

                        adapter!!.taskNames = choreNames
                        adapter!!.taskDates = choreDates
                        adapter!!.taskAssignees = choreAssignees

                        adapter!!.notifyDataSetChanged()
                    })

            doAsync {
                choreViewModel.getUsersHouse(userId!!)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_chore_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_chore)

        layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter!!.listener = this
        setRecyclerViewItemTouchListener(recyclerView)

        view.findViewById<FloatingActionButton>(R.id.fab_add_chore).setOnClickListener {
            val dialog = AddChoreDialogFragment.newInstance(userMappings, roomMappings)
            dialog.listener = this
            dialog.show(fragmentManager!!, "AddChoreDialog")
        }

        return view
    }

    override fun onChoreAddDialogFinished(chore: Chore) {
        doAsync {
            choreViewModel.addChore(chore, houseId!!) //creates the chore

            uiThread {
                Toast.makeText(context,"${chore.name} has been created", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onChoreCheckCompleted(choreID: Int) {
        //Get the chore now update it to make the chore completed
        println(choresList!![choreID].toString() + ": DID IT WORK?")
        choresList!![choreID].completed = true
        println(choresList!![choreID].toString() + ": AFTER UPDATE?")
        doAsync {
            choreViewModel.updateChore(choresList!![choreID])

            uiThread {
                Toast.makeText(context,"${choresList!![choreID].name} has been updated", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRecyclerViewItemTouchListener(rv: RecyclerView) {
        val itemTouchCallback =  object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {
                val position = viewHolder!!.adapterPosition

                doAsync {
                    choreViewModel.deleteChore(choresList!![position])
                }

                adapter!!.taskNames.removeAt(position)
                adapter!!.taskDates.removeAt(position)
                adapter!!.taskAssignees.removeAt(position)

                adapter!!.notifyItemRemoved(position)
            }
        }

        //initialize and attach
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    fun setupFilter() {
        filterList.put(0.toLong(), "All")
        filterList.putAll(userMappings)

        var filterDropdown = view!!.findViewById<Spinner>(R.id.filterDropdown)
        filterDropdown.adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item,
                filterList.values.toMutableList())

        filterDropdown.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(filterName != filterList.values.toMutableList()[p2]) {
                            doAsync {
                                filterName = filterList.values.toMutableList()[p2]
                                choreViewModel.getAllRooms(houseId!!)
                            }
                        }
                    }
                }
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
