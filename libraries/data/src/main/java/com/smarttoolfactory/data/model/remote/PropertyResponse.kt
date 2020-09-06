package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName
import com.smarttoolfactory.data.model.DataTransferObject

data class PropertyResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("res")
    val res: List<PropertyDTO>
) : DataTransferObject
