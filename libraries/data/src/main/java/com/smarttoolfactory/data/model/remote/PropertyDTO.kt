package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName
import com.smarttoolfactory.data.model.DataTransferObject

data class PropertyDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("update")
    val update: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("thumbnail_big")
    val thumbnailBig: String,
    @SerializedName("image_count")
    val imageCount: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("price_period")
    val pricePeriod: String,
    @SerializedName("price_period_raw")
    val pricePeriodRaw: String,
    @SerializedName("price_label")
    val priceLabel: String,
    @SerializedName("price_value")
    val priceValue: String,
    @SerializedName("price_value_raw")
    val priceValueRaw: Int,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("featured")
    val featured: Boolean,
    @SerializedName("location")
    val location: String,
    @SerializedName("area")
    val area: String,
    @SerializedName("poa")
    val poa: Boolean,
    @SerializedName("rera_permit")
    val reraPermit: String,
    @SerializedName("bathrooms")
    val bathrooms: String,
    @SerializedName("bedrooms")
    val bedrooms: String,
    @SerializedName("date_insert")
    val dateInsert: String,
    @SerializedName("date_update")
    val dateUpdate: String,
    @SerializedName("agent_name")
    val agentName: String,
    @SerializedName("broker_name")
    val brokerName: String,
    @SerializedName("agent_license")
    val agentLicense: String,
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("hide_location")
    val hideLocation: Boolean,

    // Broker Object
    @SerializedName("broker")
    val broker: BrokerDTO,

    @SerializedName("amenities")
    val amenities: List<String>,
    @SerializedName("amenities_keys")
    val amenitiesKeys: List<String>,

    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
    @SerializedName("premium")
    val premium: Boolean,
    @SerializedName("livingrooms")
    val livingrooms: String,
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("gallery")

    val gallery: List<String>,
    @SerializedName("phone")
    val phone: String,

    @SerializedName("lead_email_receivers")
    val leadEmailReceivers: List<String>,
    @SerializedName("reference")
    val reference: String
) : DataTransferObject
