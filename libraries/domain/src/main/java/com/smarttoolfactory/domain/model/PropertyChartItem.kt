package com.smarttoolfactory.domain.model

data class PropertyChartItem(
    val id: Int,
    val title: String,
    val price: Float,
    private val beds: String = "",
    private val baths: String = "",
    val location: String
) {

    val bedRooms = try {
        beds.toInt()
    } catch (e: NumberFormatException) {
        -1
    }

    val bathRooms = try {
        baths.toInt()
    } catch (e: NumberFormatException) {
        -1
    }

    var position: Int = 0
}
