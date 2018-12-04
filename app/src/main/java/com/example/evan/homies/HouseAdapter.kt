package com.example.evan.homies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.HouseRoom
import com.example.evan.homies.entities.RoomAllChores
import kotlinx.android.synthetic.main.cardview_room_card.view.*

class HouseAdapter(private var mData: List<RoomAllChores>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //IMPORTANT: this is a RoomAllChores Object which exists for each room and has a list of chores
    // ex.) roomData[0].chores is a list of chores for the first room in the list

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ROOMS = 1

    fun addAll(roomData: List<RoomAllChores>) {
        mData = roomData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mData.isEmpty()) 1 else mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mData.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ROOMS
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_TYPE_ROOMS) {
            v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.cardview_room_card, viewGroup, false)
            vh = ViewHolder(v)
        } else {
            v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.empty_layout, viewGroup, false)
            vh = ViewHolderEmpty(v)
        }

        return vh
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val viewType = getItemViewType(i)
        if (viewType == VIEW_TYPE_EMPTY) {
            //Don't need to do anything
        } else {
            val vh = viewHolder as HouseAdapter.ViewHolder
            val res = viewHolder.itemView.context.resources

            val roomData = mData[i]
            val room: HouseRoom = roomData.room!!
            val chores: List<Chore> = roomData.chores!!

            val completed = chores.count { chore -> chore.completed }
            val open = chores.size - completed

            vh.roomName.text = room.name
            vh.openChores.text = String.format(res.getString(R.string.open_chores_descriptor), open.toString())
            vh.completedChores.text = String.format(res.getString(R.string.completed_chores_descriptor), completed.toString())
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        var roomName: TextView
        var openChores: TextView
        var completedChores: TextView

        init {
            roomName = itemView.roomName
            openChores = itemView.openChores
            completedChores = itemView.completedChores
        }
    }

    inner class ViewHolderEmpty(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    }
}