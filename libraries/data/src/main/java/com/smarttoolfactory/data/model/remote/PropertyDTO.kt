package com.smarttoolfactory.data.model.remote

import com.google.gson.annotations.SerializedName
import com.smarttoolfactory.data.model.DataTransferObject

data class PropertyDTO(
    @SerializedName("agent_license")
    val agentLicense: String,
    @SerializedName("agent_name")
    val agentName: String,
    @SerializedName("amenities")
    val amenities: List<String>,
    @SerializedName("amenities_keys")
    val amenitiesKeys: List<String>,
    @SerializedName("area")
    val area: String,
    @SerializedName("bathrooms")
    val bathrooms: String,
    @SerializedName("bedrooms")
    val bedrooms: String,
    @SerializedName("broker")
    val broker: BrokerDTO,
    @SerializedName("broker_name")
    val brokerName: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("date_insert")
    val dateInsert: String,
    @SerializedName("date_update")
    val dateUpdate: String,
    @SerializedName("featured")
    val featured: Boolean,
    @SerializedName("gallery")
    val gallery: List<String>,
    @SerializedName("hide_location")
    val hideLocation: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_count")
    val imageCount: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lead_email_receivers")
    val leadEmailReceivers: List<String>,
    @SerializedName("livingrooms")
    val livingrooms: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("long")
    val long: Double,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("poa")
    val poa: Boolean,
    @SerializedName("premium")
    val premium: Boolean,
    @SerializedName("price")
    val price: String,
    @SerializedName("price_label")
    val priceLabel: String,
    @SerializedName("price_period")
    val pricePeriod: String,
    @SerializedName("price_period_raw")
    val pricePeriodRaw: String,
    @SerializedName("price_value")
    val priceValue: String,
    @SerializedName("price_value_raw")
    val priceValueRaw: Int,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("rera_permit")
    val reraPermit: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("thumbnail_big")
    val thumbnailBig: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("update")
    val update: Int,
    @SerializedName("verified")
    val verified: Boolean
) : DataTransferObject
