package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.smarttoolfactory.dashboard.databinding.ItemChartContainerBinding
import com.smarttoolfactory.dashboard.model.ChartItemModel

class ChartContainerViewHolder(
    private val binding: ItemChartContainerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(chartItem: ChartItemModel) {

        val chart = binding.barChart

        val l: Legend = chart.legend

        // modify the legend ...

//        l.form = LegendForm.LINE
//
//        val xAxis: XAxis = chart.xAxis
//
//        xAxis.setDrawGridLines(false)
//        xAxis.setAvoidFirstLastClipping(true)
//        xAxis.isEnabled = false
//
//        val leftAxis: YAxis = chart.axisLeft
//        leftAxis.setDrawGridLines(false)
//        leftAxis.isEnabled = false
//
//
        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false
        rightAxis.setDrawGridLines(false)

        val entries = chartItem.data.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.toFloat())
        }

        val dataSet = BarDataSet(entries, "Chart")

        val chartData = BarData(dataSet)

        chart.data = chartData
        chart.invalidate()

        binding.executePendingBindings()
    }
}
