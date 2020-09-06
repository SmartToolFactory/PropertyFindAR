package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName
import com.smarttoolfactory.data.model.DataTransferObject

data class PropertyListDTO(
    @SerializedName("res")
    val res: List<PropertyDTO>,
    @SerializedName("total")
    val total: Int
) : DataTransferObject
