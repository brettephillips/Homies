package com.example.evan.homies

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.evan.homies.entities.Chore

class ChoreAdapter: RecyclerView.Adapter<ChoreAdapter.ViewHolder>() {
    public var taskNames = mutableListOf<String>()
    public var taskDates = mutableListOf<String>()
    public var taskAssignees = mutableListOf<String>()
    public var listener: ChoreAdapter.OnChoreCheckCompleted? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var task: TextView
        var date: TextView
        var assignee: TextView

        init {
            task = itemView.findViewById(R.id.task_title)
            date = itemView.findViewById(R.id.task_date)
            assignee = itemView.findViewById(R.id.task_assignee)

            itemView.setOnClickListener {
                var position = adapterPosition
                Snackbar.make(it, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }

            itemView.findViewById<TextView>(R.id.task_assignee).setOnClickListener {
                val assigneeTV = itemView.findViewById<TextView>(R.id.task_assignee)
                assigneeTV.text = "\u2713"

                listener?.onChoreCheckCompleted(adapterPosition)
            }

            itemView.findViewById<ImageView>(R.id.thumbsImage).setOnClickListener {
                val thumb = itemView.findViewById<ImageView>(R.id.thumbsImage)
                val tag = thumb.tag

                if(tag == "thumbOutline") {
                    thumb.setImageResource(R.drawable.ic_thumb_up_fill_24dp)
                    thumb.tag = "thumbFill"
                } else {
                    thumb.setImageResource(R.drawable.ic_thumb_up_outline_24dp)
                    thumb.tag = "thumbOutline"
                }
            }
        }
    }

    //creates a view holder object and inflates the view
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_chore_card, viewGroup, false)

        return ViewHolder(v)
    }

    //populates the view holder view
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.task.text = taskNames[i]
        viewHolder.date.text = taskDates[i]
        viewHolder.assignee.text = taskAssignees[i]
    }

    override fun getItemCount(): Int {
        return taskNames.size
    }

    interface OnChoreCheckCompleted {
        fun onChoreCheckCompleted(choreID: Int)
    }
}