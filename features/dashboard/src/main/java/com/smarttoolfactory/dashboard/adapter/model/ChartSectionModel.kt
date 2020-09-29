package com.smarttoolfactory.dashboard.adapter.model

import com.smarttoolfactory.domain.model.PropertyChartItem

data class ChartSectionModel(
    val data: List<PropertyChartItem>,
    val chartTitle: String = ""
) : Model
