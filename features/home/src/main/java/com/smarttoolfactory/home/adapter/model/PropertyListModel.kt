package com.smarttoolfactory.home.adapter.model

import android.os.Parcelable
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.android.parcel.Parcelize

/**
 * Model for list that has a title,
 * and data for horizontally scrollable items in RecyclerView
 */
@Parcelize
data class PropertyListModel(val transitionName: String, val items: List<PropertyItem>) : Parcelable
