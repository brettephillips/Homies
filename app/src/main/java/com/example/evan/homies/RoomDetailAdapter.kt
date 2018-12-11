package com.example.evan.homies

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.RoomAllChores
import kotlinx.android.synthetic.main.rv_chore_list_simple.view.*
import kotlin.text.Typography.bullet

class RoomDetailAdapter(private var roomData: RoomAllChores): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_ROOMS = 1


    fun addAll(data: RoomAllChores) {
        roomData = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (roomData.chores.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ROOMS
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_TYPE_ROOMS) {
            v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.rv_chore_list_simple, viewGroup, false)
            vh = ViewHolder(v)
        } else {
            //empty layout
            v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.empty_layout, viewGroup, false)
            vh = ViewHolderEmpty(v)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return if (roomData.chores.isEmpty()) 1 else roomData.chores.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val viewType = getItemViewType(i)
        if (viewType == VIEW_TYPE_EMPTY) {
            //Don't need to do anything
        } else {
            val vh = viewHolder as RoomDetailAdapter.ViewHolder
            val res = viewHolder.itemView.context.resources

            val chore: Chore = roomData.chores[i]


            vh.choreTitle.text = bullet + " " + chore.name
            vh.choreDate.text = chore.dateDue

        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        var choreTitle: TextView
        var choreDate: TextView

        init {

            choreTitle = itemView.chore_title_simple
            choreDate = itemView.chore_date_simple

        }//init
    }

    inner class ViewHolderEmpty(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    }
}