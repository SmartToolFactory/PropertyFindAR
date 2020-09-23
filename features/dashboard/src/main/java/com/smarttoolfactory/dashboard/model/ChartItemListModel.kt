package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyChartItem

data class ChartItemListModel(val data: List<PropertyChartItem>, val chartTitle: String = "")
