package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.ViewGroup
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
import com.smarttoolfactory.core.ui.recyclerview.adapter.AbstractItemViewBinder
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.databinding.ItemChartBarBinding
import com.smarttoolfactory.dashboard.model.ChartSectionModel

class BarChartViewBinder(
    private val onChartItemClicked: ((Float) -> Unit)? = null
) : AbstractItemViewBinder<ChartSectionModel, BarChartViewHolder>(
    ChartSectionModel::class.java
) {

    override fun createViewHolder(parent: ViewGroup): BarChartViewHolder {
        return BarChartViewHolder(
            parent.inflate(getItemLayoutResource()),
            onChartItemClicked,
        )
    }

    override fun bindViewHolder(model: ChartSectionModel, viewHolder: BarChartViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource() = R.layout.item_chart_bar

    override fun areItemsTheSame(
        oldItem: ChartSectionModel,
        newItem: ChartSectionModel
    ): Boolean {
        return oldItem.data == newItem.data
    }

    override fun areContentsTheSame(
        oldItem: ChartSectionModel,
        newItem: ChartSectionModel
    ): Boolean {
        return false
    }
}

/**
 * [RecyclerView.ViewHolder] for Bar chart that displays properties and shows details of
 * property when a bar in chart is clicked
 */
class BarChartViewHolder(
    private val binding: ItemChartBarBinding,
    private val onChartItemClicked: ((Float) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chartItem: ChartSectionModel) {

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

        binding.executePendingBindings()
    }
}
