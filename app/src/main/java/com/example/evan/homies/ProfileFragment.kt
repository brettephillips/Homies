package com.example.evan.homies

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.evan.homies.entities.Chore
import com.example.evan.homies.entities.User
import com.example.evan.homies.viewmodels.ProfileViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(), OnChartValueSelectedListener {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var chart: PieChart
    private val entries = arrayListOf<PieEntry>()
    private var name = ""
    private var numOfChores: Int = 0
    private var completedTasks: Int = 0
    private var totalThumbsUp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewModel = ProfileViewModel(activity?.application!!)

        profileViewModel.profileSetup(arguments!!.getLong(ChoresFragment.USER_ID,0))
            .observe(this, Observer { user ->
                displayUsersName(user!!)
            })

        profileViewModel.getUserTasks(arguments!!.getLong(ChoresFragment.USER_ID,0))
            .observe(this, Observer { choreList ->
                updateChart(choreList!!)
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onNothingSelected() {
        println("nothing selected")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val pieEntry = e as PieEntry
        chart.centerText = pieEntry.value.toString() + "%"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(name.length > 0) {
            userName.text = name
        }

        setupChart()
        setChartStyles()
    }

    fun displayUsersName(user: User) {
        name = getString(R.string.user_name, user.firstName, user.lastName)
        view!!.userName.text = name
    }

    fun setupChart() {
        chart = view!!.findViewById(R.id.pieChart) as PieChart

        if(entries.size > 0) {
            entries.clear()
        }

        entries.add(PieEntry(100f, "No Chores found"))

        val set = PieDataSet(entries, "")
        set.colors = arrayListOf(Color.RED, Color.BLUE)
        set.setDrawValues(false)

        val data = PieData(set)
        chart.data = data
    }

    fun setChartStyles() {
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setDrawEntryLabels(false)
        chart.setDrawCenterText(true)
        chart.description.isEnabled = false
        chart.data.setValueTextSize(12f)
        chart.legend.yEntrySpace = 20f
        chart.legend.position = Legend.LegendPosition.LEFT_OF_CHART_CENTER
        chart.legend.formSize = 16f
        chart.legend.textSize = 30f
        chart.setOnChartValueSelectedListener(this)
        chart.animateY(1000, Easing.Linear)
    }

    fun updateChart(chores: MutableList<Chore>) {
        if(!chores.isEmpty()) {
            numOfChores = chores.size

            for(item in chores) {
                if(item.completed) {
                    completedTasks += 1
                }

                if(item.thumbsUp) {
                    totalThumbsUp += 1
                }
            }
        }

        this.view!!.completedTasksText.text = completedTasks.toString()
        this.view!!.thumbsUpText.text = totalThumbsUp.toString()

        if(numOfChores != 0) {
            entries.clear()
            chart.setTouchEnabled(true)
            entries.add(PieEntry((completedTasks / numOfChores) * 100f, "Completed Tasks"))
            entries.add(PieEntry(((numOfChores - completedTasks) / numOfChores) * 100f, "Incomplete Tasks"))

            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    }
}
