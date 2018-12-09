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
import java.util.ArrayList

class AddChoreDialogFragment: DialogFragment() {
    private lateinit var choreViewModel: ChoreViewModel
    var listener: OnChoreAddDialogFinishedListener? = null
    var users: MutableMap<Long, String> = mutableMapOf()
    var rooms: MutableMap<Long, String> = mutableMapOf()
    var assignee: Long? = null
    var room: Long? = null

    companion object {

        @JvmStatic
        fun newInstance(usersList: MutableMap<Long, String>, roomsList: MutableMap<Long, String>) =
                AddChoreDialogFragment().apply {
                    users = usersList
                    rooms = roomsList
                }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(!users.isEmpty() || !rooms.isEmpty()) {
            outState.putStringArrayList("userVals", ArrayList(users.values.toList()) as ArrayList<String>)
            outState.putIntegerArrayList("userKeys", ArrayList(users.keys.toList()) as ArrayList<Int>)
            outState.putStringArrayList("roomVals", ArrayList(rooms.values.toList()) as ArrayList<String>)
            outState.putIntegerArrayList("roomKeys", ArrayList(rooms.keys.toList()) as ArrayList<Int>)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        choreViewModel = ChoreViewModel(activity?.application!!)
        if (savedInstanceState != null) {
            var vals = savedInstanceState.getStringArrayList("userVals")
            var keys = savedInstanceState.getIntegerArrayList("userKeys")
            var rVals = savedInstanceState.getStringArrayList("roomVals")
            var rKeys = savedInstanceState.getIntegerArrayList("roomKeys")

            for(i in keys.indices) {
                users.put(keys[i].toLong(), vals[i])
            }

            for(i in rKeys.indices) {
                rooms.put(rKeys[i].toLong(), rVals[i])
            }
        }

        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_fragment_add_chore, null)

        getUsersInHouse(view)

        builder.setView(view).setPositiveButton("Add") {_, _ ->
            doAsync {
                val name = view.findViewById<EditText>(R.id.chore_name_editText).text.toString()
                val date = view.findViewById<EditText>(R.id.chore_date_editText).text.toString()

                println("NEW CHORE: $name, $date, $assignee, $room")
                val newChore = Chore(name, date, false, false, assignee!!, room!!)

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
        var userDropdown = view.findViewById<Spinner>(R.id.choreAssigneeDropdown)
        var roomDropdown = view.findViewById<Spinner>(R.id.roomAssigneeDropdown)
        var userMutList = users.values.toMutableList()
        var roomMutList = rooms.values.toMutableList()

        userDropdown.adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item,
                userMutList)
        roomDropdown.adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item,
                roomMutList)

        view.findViewById<Spinner>(R.id.choreAssigneeDropdown).onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(p3 == 0.toLong()) {
                            assignee = users.keys.first()
                        } else {
                            users.forEach { (key, value) ->
                                if(users[key].equals(userMutList[p2])) {
                                    assignee = key
                                }
                            }
                        }
                    }
                }

        view.findViewById<Spinner>(R.id.roomAssigneeDropdown).onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(p3 == 0.toLong()) {
                            room = rooms.keys.first()
                        } else {
                            rooms.forEach { (key, value) ->
                                if(rooms[key].equals(roomMutList[p2])) {
                                    room = key
                                }
                            }
                        }
                    }
                }
    }

    interface OnChoreAddDialogFinishedListener {
        fun onChoreAddDialogFinished(chore: Chore)
    }
}