package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyItem

data class RecommendedItemModel(
    val items: List<PropertyItem>
) : Model
