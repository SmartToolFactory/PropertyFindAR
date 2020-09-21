package com.smarttoolfactory.dashboard.model

import com.smarttoolfactory.domain.model.PropertyItem

/**
 * Model for Horizontal list that has a title, and data for horizontally scrollable items
 */
data class HorizontalTitleListModel(
    val title: String = "",
    val items: List<PropertyItem>
)
