package com.example.evan.homies

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import android.support.v7.app.AlertDialog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class JoinHouseDialogFragment : DialogFragment() {

    private lateinit var houseIdField: EditText
    var listener: OnJoinDialogFinishedListener? = null

    companion object {

        @JvmStatic
        fun newInstance() =
            JoinHouseDialogFragment().apply {
                //context?
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)

        // get layout inflater
        val inflater = activity!!.layoutInflater
        // pass null into root because it is going in the dialog layout
        var view = inflater.inflate(R.layout.dialog_fragment_join_house, null)

        houseIdField = view.findViewById(R.id.houseIdField)

        builder.setView(view)
            .setPositiveButton("Join") { _, _ ->
                // can move lambda outside of the parentheses since it is the last param. Gross but conventional
                doAsync {
                    val id = houseIdField.text.toString()
                    //TODO: validate and join house. Create houseUser
                    uiThread {
                        listener?.onJoinDialogFinished(id)
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ -> this@JoinHouseDialogFragment.dialog.cancel() }
        return builder.create()
    }


    interface OnJoinDialogFinishedListener {
        fun onJoinDialogFinished(id: String)
    }

}