package com.smarttoolfactory.dashboard.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.smarttoolfactory.dashboard.databinding.ItemBarChartBinding
import com.smarttoolfactory.dashboard.model.ChartItemListModel

/**
 * [RecyclerView.ViewHolder] for Bar chart that displays properties and shows details of
 * property when a bar in chart is clicked
 */
class BarChartViewHolder(
    private val binding: ItemBarChartBinding,
    private val onChartItemClicked: ((Float) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(chartItem: ChartItemListModel) {

        val chart = binding.barChart

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {

            override fun onValueSelected(entry: Entry, h: Highlight) {

                onChartItemClicked?.let {
                    it(entry.x)
                }
            }

            override fun onNothingSelected() = Unit
        })

        val l: Legend = chart.legend

        // modify the legend ...

//        l.form = LegendForm.LINE
//
        val xAxis: XAxis = chart.xAxis

//        xAxis.setDrawGridLines(false)
//        xAxis.setAvoidFirstLastClipping(true)
//        xAxis.isEnabled = false

        val leftAxis: YAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.isEnabled = false

        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false
        rightAxis.setDrawGridLines(false)

        val entries = chartItem.data.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.price)
        }

        val dataSet = BarDataSet(entries, "Chart")

        val chartData = BarData(dataSet)

        chart.data = chartData
        chart.invalidate()

        binding.executePendingBindings()
    }
}

interface OnChartPositionChangeListener {

    fun onChartPositionChange(position: Int)
}
