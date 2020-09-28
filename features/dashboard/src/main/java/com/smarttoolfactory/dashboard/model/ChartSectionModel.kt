package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyChartItem

data class ChartSectionModel(
    val data: List<PropertyChartItem>,
    val chartTitle: String = ""
) : Model
