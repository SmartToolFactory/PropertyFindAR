package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyItem

data class RecommendedSectionModel(
    val title: String = "",
    val items: List<PropertyItem>
) : Model
