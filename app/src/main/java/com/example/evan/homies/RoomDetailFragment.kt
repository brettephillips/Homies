package com.example.evan.homies


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.evan.homies.entities.RoomAllChores
import com.example.evan.homies.viewmodels.RoomDetailViewModel
import kotlinx.android.synthetic.main.fragment_room_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RoomDetailFragment : Fragment() {

    private var roomID: Long? = null
    private var roomData: RoomAllChores? = null
    var mAdapter: RoomDetailAdapter? = null
    private var recyclerView : RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    var roomDetailViewModel: RoomDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomDetailViewModel = RoomDetailViewModel(activity!!.application)
        mAdapter = RoomDetailAdapter(RoomAllChores())

        if(roomID == null){
            roomID = savedInstanceState!!.getLong("roomID")
        }

        roomDetailViewModel!!.getCurrentRoom().observe(this , Observer {
            data ->
            roomData = data
            mAdapter!!.addAll(roomData ?: RoomAllChores())
            view?.findViewById<EditText>(R.id.roomTitleEditText)?.setText(data!!.room!!.name)
        })

        doAsync {
            //get data for room
            roomDetailViewModel!!.getRoomData(roomID!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_room_details, container, false)
        //Log.d("DETAILS FOR ROOM", roomData!!.room.toString())
        //Log.d("CHORES FOR ROOM", roomData!!.chores.toString())

        recyclerView = view.findViewById(R.id.recycler_view_room_chores) as RecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter

        val title = view.findViewById<EditText>(R.id.roomTitleEditText)
        title.setText(roomData?.room?.name)

        view.findViewById<Button>(R.id.save_btn).setOnClickListener {
            val newName = title.text.toString()
            if (roomData!!.room!!.name == newName) {
                Toast.makeText(context, "You have not changed anything in this room.", Toast.LENGTH_SHORT).show()
            } else {
                val room = roomData!!.room!!
                room.name = newName
                doAsync {
                    roomDetailViewModel!!.updateRoom(room)
                    uiThread {
                        activity!!.supportFragmentManager.popBackStack()
                    }
                }// async
            }// else
        }//listener

        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("roomID", roomID!!)
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long) =
            RoomDetailFragment().apply {
                roomID = id
            }
    }

}
