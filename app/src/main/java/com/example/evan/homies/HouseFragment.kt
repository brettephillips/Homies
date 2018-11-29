package com.example.evan.homies

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.evan.homies.entities.House
import com.example.evan.homies.viewmodels.HouseViewModel
import kotlinx.android.synthetic.main.dialog_fragment_create_house.*

class HouseFragment : Fragment() {

    //this is the view. Its role is to observe or subscribe to a viewmodel to get data in order to update ui elements
    private lateinit var houseViewModel: HouseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        houseViewModel = HouseViewModel(activity?.application!!)
        houseViewModel.getCurrentHouse().observe(this,
            Observer {house ->
                println("CURRENT HOUSE FROM OBSERVER: " + house)
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_house, container, false)

        //add house dialog
        view.findViewById<Button>(R.id.add_house_btn).setOnClickListener {
            handleFragmentForDialog("CreateHouseDialog")
            val dialog = CreateHouseDialogFragment()
            dialog.show(fragmentManager!!, "CreateHouseDialog")

            dialog.save_house_btn.setOnClickListener { v ->
                val houseName = house_name_editText.text.toString()
                //TODO: get the list of other users provided
                houseViewModel.insertHouse(House(houseName))
            }
        }

        //join house dialog
        view.findViewById<Button>(R.id.join_house_btn).setOnClickListener {
            handleFragmentForDialog("JoinHouseDialog")
            val dialog = JoinHouseDialogFragment()
            dialog.show(fragmentManager!!, "JoinHouseDialog")
        }

        return view
    }

    private fun handleFragmentForDialog (dialogName: String) {
        val ft = fragmentManager!!.beginTransaction()
        val prev = fragmentManager!!.findFragmentByTag(dialogName)

        if(prev !== null){
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        ft.commit()
    }

}
