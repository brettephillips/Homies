package com.example.evan.homies

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import com.example.evan.homies.entities.HouseRoom
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CreateRoomDialogFragment : DialogFragment() {
    private lateinit var roomNameField: EditText
    var listener: OnRoomDialogFinishedListener? = null

    companion object {

        @JvmStatic
        fun newInstance() =
                CreateRoomDialogFragment().apply {

                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)

        val inflater = activity!!.layoutInflater
        var view = inflater.inflate(R.layout.dialog_fragment_create_room, null)

        roomNameField = view.findViewById(R.id.roomNameField)

        builder.setView(view)
            .setPositiveButton("Add Room") {_, _ ->
                doAsync {
                    //TODO: validation
                    val roomName = roomNameField.text.toString()
                    uiThread {
                        listener?.onRoomDialogFinished(roomName)
                    }
                }

            }
            .setNegativeButton("Cancel") {_, _ -> this@CreateRoomDialogFragment.dialog.cancel() }
        return builder.create()
    }

    interface OnRoomDialogFinishedListener {
        fun onRoomDialogFinished(roomName: String)
    }
}