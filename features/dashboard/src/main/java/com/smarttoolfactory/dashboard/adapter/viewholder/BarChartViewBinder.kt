package com.smarttoolfactory.dashboard.adapter.viewholder

import android.view.View
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
import com.github.mikephil.charting.utils.ColorTemplate
import com.smarttoolfactory.core.ui.recyclerview.adapter.MappableItemViewBinder
import com.smarttoolfactory.core.util.executeAfter
import com.smarttoolfactory.core.util.inflate
import com.smarttoolfactory.dashboard.R
import com.smarttoolfactory.dashboard.adapter.model.ChartSectionModel
import com.smarttoolfactory.dashboard.databinding.ItemChartBarBinding

class BarChartViewBinder(
    private val onChartItemClicked: ((Float) -> Unit)? = null
) : MappableItemViewBinder<ChartSectionModel, BarChartViewHolder>(
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
        return oldItem.items == newItem.items
    }

    override fun areContentsTheSame(
        oldItem: ChartSectionModel,
        newItem: ChartSectionModel
    ): Boolean {
        return false
    }

    override fun onViewRecycled(viewHolder: BarChartViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
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

    fun bind(model: ChartSectionModel) {
        binding.executeAfter {
            if (model.items.isNullOrEmpty()) {
                binding.chartLayout.visibility = View.GONE
                binding.cardView.visibility = View.GONE
            } else {
                binding.chartLayout.visibility = View.VISIBLE
                binding.cardView.visibility = View.VISIBLE
                createChart(model)
            }
        }
    }

    private fun createChart(chartItem: ChartSectionModel) {

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

        xAxis.setDrawGridLines(false)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.isEnabled = false

        val leftAxis: YAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.isEnabled = false

        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false
        rightAxis.setDrawGridLines(false)

        val entries = chartItem.items.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.price)
        }

        val dataSet = BarDataSet(entries, "Indices")
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val chartData = BarData(dataSet)

        chart.data = chartData
//        chart.animateXY(500, 500)
    }

    fun onViewRecycled() {
//        binding.barChart.setOnChartValueSelectedListener(null)
//        binding.unbind()
    }
}
