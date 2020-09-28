package com.smarttoolfactory.dashboard.model

import android.os.Parcelable
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.android.parcel.Parcelize

/**
 * Model for list that has a title,
 * and data for horizontally scrollable items in RecyclerView
 */
@Parcelize
data class PropertyListModel(
    val title: String = "",
    val items: List<PropertyItem>,
    var seeAll: Boolean = true
) : Model, Parcelable
