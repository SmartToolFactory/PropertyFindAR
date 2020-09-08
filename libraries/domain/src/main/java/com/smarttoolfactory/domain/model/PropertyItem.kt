package com.smarttoolfactory.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * UI item for Properties
 */
@Parcelize
data class PropertyItem(

    val id: Int,
    val update: Int,
    val categoryId: Int,
    val title: String,
    val subject: String,
    val type: String,
    val typeId: Int,
    val thumbnail: String?,
    val thumbnailBig: String?,
    val imageCount: Int,
    val price: String,
    val pricePeriod: String?,
    val pricePeriodRaw: String,
    val priceLabel: String?,
    val priceValue: String?,
    val priceValueRaw: Int,
    val currency: String,
    val featured: Boolean,
    val location: String,
    val area: String,
    val poa: Boolean,
    val reraPermit: String?,
    val bathrooms: String,
    val bedrooms: String,
    val dateInsert: String,
    val dateUpdate: String,
    val agentName: String,
    val brokerName: String,
    val agentLicense: String?,
    val locationId: Int,
    val hideLocation: Boolean,

    val broker: BrokerItem,
    val amenities: List<String>,
    val amenitiesKeys: List<String>,

    val latitude: Double,
    val longitude: Double,
    val premium: Boolean,
    val livingrooms: String,
    val verified: Boolean,

    val gallery: List<String>?,
    val phone: String,

    val leadEmailReceivers: List<String>,

    val reference: String,

    var displayCount: Int = 0,
    var isFavorite: Boolean = false
) : Parcelable
