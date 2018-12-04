package com.example.evan.homies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.evan.homies.entities.HouseRoom
import kotlinx.android.synthetic.main.cardview_room_card.view.*

class HouseAdapter(private var mData: List<HouseRoom>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ROOMS = 1

    fun addAll(rooms: List<HouseRoom>) {
        mData = rooms
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
            val room = mData[i]
            vh.roomName.text = room.name
            vh.openChores.text = "Open Chores: 2" //TODO: CHANGE THIS BY JOINING TO GET ACTUAL NUMBER
            vh.completedChores.text = "Completed Chores: 10" //TODO: CHANGE THIS BY JOINING TO GET ACTUAL NUMBER
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