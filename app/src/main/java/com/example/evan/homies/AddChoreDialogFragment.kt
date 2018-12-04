package com.example.evan.homies

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.*
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.viewmodels.ChoreViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddChoreDialogFragment: DialogFragment() {
    private lateinit var choreViewModel: ChoreViewModel
    var listener: OnChoreAddDialogFinishedListener? = null
    var assignee = ""

    companion object {

        @JvmStatic
        fun newInstance() =
                AddChoreDialogFragment().apply {
                    //context?
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        choreViewModel = ChoreViewModel(activity?.application!!)

        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_fragment_add_chore, null)

        getUsersInHouse(view)

        builder.setView(view).setPositiveButton("Add") {_, _ ->
            doAsync {
                val name = view.findViewById<EditText>(R.id.chore_name_editText).text.toString()
                val date = view.findViewById<EditText>(R.id.chore_date_editText).text.toString()

                println("NEW CHORE: $name, $date, $assignee")
                //hard coding userid and houseid for now 1, 1
                val newChore = Chore(name.toString(), date.toString(), false, false, 1, 1)

                uiThread {
                    listener?.onChoreAddDialogFinished(newChore)
                }
            }
        }.setNegativeButton("Cancel") {_, _ ->
            this@AddChoreDialogFragment.dialog.cancel()
        }



        return builder.create()
    }

    fun getUsersInHouse(view: View) {
        var dropdown = view.findViewById<Spinner>(R.id.choreAssigneeDropdown)
        //hard coding list of users in house for now
        //In order to get this I need to get my house ID, then get all the users in that house
        //May need a map to link the id to the user unless I want another query to get the users ID
        var usersList = mutableListOf("Brett Phillips", "Evan Forbes", "Nick Deyette", "Tyler Valentine")
        var userMappings = mapOf<Long, String>(1.toLong() to "Brett Phillips", 2.toLong() to "Evan Forbes",
                3.toLong() to "Nick Deyette", 4.toLong() to "Tyler Valentine")

        dropdown.adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item,
                userMappings.values.toMutableList())

        view.findViewById<Spinner>(R.id.choreAssigneeDropdown).onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        assignee = usersList[p2]
                    }
                }
    }

    interface OnChoreAddDialogFinishedListener {
        fun onChoreAddDialogFinished(chore: Chore)
    }
}