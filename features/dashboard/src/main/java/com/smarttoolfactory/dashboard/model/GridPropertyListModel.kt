package com.smarttoolfactory.dashboard.model

import android.os.Parcelable
import com.smarttoolfactory.domain.model.PropertyItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GridPropertyListModel(
    val title: String = "",
    val items: List<PropertyItem>
) : Parcelable
