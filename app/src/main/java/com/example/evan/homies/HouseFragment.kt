package com.example.evan.homies

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import org.jetbrains.anko.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.evan.homies.entities.House
import com.example.evan.homies.entities.HouseInfo
import com.example.evan.homies.entities.HouseRoom
import com.example.evan.homies.viewmodels.HouseViewModel
import kotlinx.android.synthetic.main.fragment_house.*

class HouseFragment : Fragment(),
    CreateHouseDialogFragment.OnCreateDialogFinishedListener,
    JoinHouseDialogFragment.OnJoinDialogFinishedListener,
    CreateRoomDialogFragment.OnRoomDialogFinishedListener {

    private var userId: Long? = null
    private var house: HouseInfo? = null
    private var rooms: List<HouseRoom>? = null

    private var houseViewModel: HouseViewModel? = null

    var mAdapter: HouseAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //check user id
        userId = arguments!!.getLong(ChoresFragment.USER_ID,0)
        if (userId == 0.toLong()){
            //TODO: send to login screen
        }
        // init viewModel and adapter
        houseViewModel = HouseViewModel(activity!!.application)
        mAdapter = HouseAdapter(mutableListOf<HouseRoom>())

        houseViewModel!!.getCurrentHouse().observe(this, Observer { data ->
            Log.d("CURRENT HOUSE:", data.toString())
            house = data
            if(house?.name != null || house?.name != "") {
                updateTitle(); hideBottomButtons()
                //get house rooms and give to adapter to put in recycler view
                doAsync {
                    houseViewModel!!.getAllRooms(house!!.id!!)
                }
            }
        })

        houseViewModel!!.getCurrentRooms().observe(this, Observer { data ->
            rooms = data
            Log.d("CURRENT ROOMS:", rooms.toString())
            mAdapter!!.addAll(rooms ?: emptyList())
        })

        //get current house
        doAsync {
            //gets the user's house, will update the observer
            houseViewModel!!.getUsersHouse(userId!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as Homies).supportActionBar?.title = "House"
        val view = inflater.inflate(R.layout.fragment_house, container, false)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter

        fab?.hide()

        //add house dialog
        view.findViewById<Button>(R.id.add_house_btn).setOnClickListener {
            val createHouseDialog = CreateHouseDialogFragment.newInstance()
            createHouseDialog.listener = this
            createHouseDialog.show(fragmentManager!!, "addHouse")
        }

        //join house dialog
        view.findViewById<Button>(R.id.join_house_btn).setOnClickListener {
            val joinHouseDialog = JoinHouseDialogFragment.newInstance()
            joinHouseDialog.listener = this
            joinHouseDialog.show(fragmentManager!!, "joinHouse")
        }

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val createRoomDialogFragment = CreateRoomDialogFragment.newInstance()
            createRoomDialogFragment.listener = this
            createRoomDialogFragment.show(fragmentManager!!, "addRoom")
        }

        return view
    }

    // called after the CreateHouseDialog "save" button is clicked
    override fun onCreateDialogFinished(house: House) {
        doAsync {
            houseViewModel!!.insertHouseAndAddUser(house, userId!!) //creates house and userHouse relationship
            uiThread {
                Toast.makeText(context,"${house.name} has been created", Toast.LENGTH_LONG).show()
            }
        }
    }

    //called after the JoinHouseDialog "join" button is clicked
    override fun onJoinDialogFinished(id: String) {
        doAsync {
            houseViewModel!!.insertUserToHouse(id.toLong(), userId!!)
            uiThread {
                Toast.makeText(context,"You've successfully joined a house!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRoomDialogFinished(roomName: String) {
        doAsync {
            houseViewModel!!.insertRoom(HouseRoom(roomName, house!!.id!!))
            uiThread {
                Toast.makeText(context, "$roomName added to ${house!!.name}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateTitle() {
        view?.findViewById<TextView>(R.id.houseTitleText)?.text = ""
        (activity as Homies).supportActionBar?.title = house!!.name
    }

    private fun hideBottomButtons() {
        //hide bottom buttons
        view?.findViewById<LinearLayout>(R.id.noHouseButtons)?.visibility = View.GONE
        //show fab
        fab.show()
    }

}
