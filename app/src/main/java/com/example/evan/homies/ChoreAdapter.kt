package com.example.evan.homies

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ChoreAdapter: RecyclerView.Adapter<ChoreAdapter.ViewHolder>() {
    public var taskNames = mutableListOf<String>()
    public var taskDates = mutableListOf<String>()
    public var taskAssignees = mutableListOf<String>()
    public var thumbsUpList = mutableListOf<Boolean>()
    public var completedList = mutableListOf<Boolean>()
    public var listener: ChoreAdapter.OnChoreAction? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var task: TextView
        var date: TextView
        var assignee: TextView
        var thumbsUp: ImageView
        var completedCheck: TextView

        init {
            task = itemView.findViewById(R.id.task_title)
            date = itemView.findViewById(R.id.task_date)
            assignee = itemView.findViewById(R.id.task_assignee)
            thumbsUp = itemView.findViewById(R.id.thumbsImage)
            completedCheck = itemView.findViewById(R.id.task_assignee)

            itemView.setOnClickListener {
                listener!!.onEditChore(adapterPosition)
            }

            itemView.findViewById<TextView>(R.id.task_assignee).setOnClickListener {
                val assigneeTV = itemView.findViewById<TextView>(R.id.task_assignee)

                listener?.onChoreCheckCompleted(adapterPosition, assigneeTV)
            }

            itemView.findViewById<ImageView>(R.id.thumbsImage).setOnClickListener {
                val thumb = itemView.findViewById<ImageView>(R.id.thumbsImage)

                listener?.onChoreThumbsUp(adapterPosition, thumb)
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

        if(thumbsUpList[i]) {
            viewHolder.thumbsUp.setImageResource(R.drawable.ic_thumb_up_fill_24dp)
        } else {
            viewHolder.thumbsUp.setImageResource(R.drawable.ic_thumb_up_outline_24dp)
        }

        if(completedList[i]) {
            viewHolder.completedCheck.text = "\u2713"
        }
    }

    override fun getItemCount(): Int {
        return taskNames.size
    }

    interface OnChoreAction {
        fun onChoreCheckCompleted(choreID: Int, textView: TextView)
        fun onChoreThumbsUp(choreID: Int, imageView: ImageView)
        fun onEditChore(choreID: Int)
    }
}