package com.smarttoolfactory.dashboard.adapter.model

import com.smarttoolfactory.domain.model.PropertyItem

data class RecommendedSectionModel(
    val title: String = "",
    val items: List<PropertyItem>
) : Model
