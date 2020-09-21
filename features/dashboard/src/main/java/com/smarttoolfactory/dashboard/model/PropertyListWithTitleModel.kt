package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyItem

/**
 * Model for list that has a title,
 * and data for either vertically or horizontally scrollable items in RecyclerView
 */
data class PropertyListWithTitleModel(
    val title: String = "",
    val items: List<PropertyItem>
)
