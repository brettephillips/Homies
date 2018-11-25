package com.example.evan.homies

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


class ProfileFragment : Fragment(), OnChartValueSelectedListener {
    private lateinit var chart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onNothingSelected() {
        println("nothing selected")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        var pieEntry = e as PieEntry
        chart.centerText = pieEntry.value.toString() + "%"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.userName).text = arguments!!.getString(ChoresFragment.USER_NAME)

        //PIECHART EXAMPLE
        chart = view.findViewById(R.id.pieChart) as PieChart
        val entries = arrayListOf<PieEntry>()

        entries.add(PieEntry(20f, "Completed Tasks"))
        entries.add(PieEntry(80f, "Incomplete Tasks"))

        val set = PieDataSet(entries, "")
        set.colors = arrayListOf(Color.RED, Color.BLUE)
        chart.setUsePercentValues(true)
        set.setDrawValues(false)
        chart.setDrawEntryLabels(false)
        chart.setDrawCenterText(true)
        chart.setOnChartValueSelectedListener(this)
        chart.animateY(1000, Easing.Linear)
        val data = PieData(set)
        chart.data = data
        chart.data.setValueTextSize(12f)
        chart.legend.yEntrySpace = 20f
        chart.legend.position = Legend.LegendPosition.LEFT_OF_CHART_CENTER
        chart.legend.formSize = 16f
        chart.legend.textSize = 30f
        chart.description.isEnabled = false
        chart.invalidate() // refresh
    }

}
