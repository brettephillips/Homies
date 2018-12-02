package com.example.evan.homies

import android.support.v7.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import com.example.evan.homies.entities.House
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CreateHouseDialogFragment : DialogFragment() {

    private lateinit var houseNameField: EditText
    var listener: OnCreateDialogFinishedListener? = null

    companion object {

        @JvmStatic
        fun newInstance() =
                CreateHouseDialogFragment().apply {
                    //context?
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)

        // get layout inflater
        val inflater = activity!!.layoutInflater
        // pass null into root because it is going in the dialog layout
        var view = inflater.inflate(R.layout.dialog_fragment_create_house, null)

        houseNameField = view.findViewById(R.id.houseNameField)

        builder.setView(view)
            .setPositiveButton("Save") { _, _ ->
                // can move lambda outside of the parentheses since it is the last param. Gross but conventional
                doAsync {
                    //TODO: validation
                    val house = House(houseNameField.text.toString())
                    uiThread {
                        listener?.onCreateDialogFinished(house)
                    }
                }
            }
            .setNegativeButton("Cancel") { _, _ -> this@CreateHouseDialogFragment.dialog.cancel() }
        return builder.create()
    }

    interface OnCreateDialogFinishedListener {
        fun onCreateDialogFinished(house: House)
    }

}