package com.smarttoolfactory.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.smarttoolfactory.data.model.IEntity

/**
 * Properties retrieved via REST is converted to [PropertyEntity] to store
 * in database for offline-first or online-first.
 *
 * * Implements [IEntity] marker interface for mapping this database items from REST DTOs or
 * to UI items
 */
@Entity(tableName = "property", primaryKeys = ["insert_order"])
data class PropertyEntity(

    @ColumnInfo(name = "insert_order")
    var insertOrder: Int = 0,

    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "update")
    val update: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "subject")
    val subject: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "type_id")
    val typeId: Int,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String?,
    @ColumnInfo(name = "thumbnail_big")
    val thumbnailBig: String?,
    @ColumnInfo(name = "image_count")
    val imageCount: Int,
    @ColumnInfo(name = "price")
    val price: String,
    @ColumnInfo(name = "price_period")
    val pricePeriod: String?,
    @ColumnInfo(name = "price_period_raw")
    val pricePeriodRaw: String,
    @ColumnInfo(name = "price_label")
    val priceLabel: String?,
    @ColumnInfo(name = "price_value")
    val priceValue: String?,
    @ColumnInfo(name = "price_value_raw")
    val priceValueRaw: Int,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "featured")
    val featured: Boolean,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "area")
    val area: String,
    @ColumnInfo(name = "poa")
    val poa: Boolean,
    @ColumnInfo(name = "rera_permit")
    val reraPermit: String?,
    @ColumnInfo(name = "bathrooms")
    val bathrooms: String,
    @ColumnInfo(name = "bedrooms")
    val bedrooms: String,
    @ColumnInfo(name = "date_insert")
    val dateInsert: String,
    @ColumnInfo(name = "date_update")
    val dateUpdate: String,
    @ColumnInfo(name = "agent_name")
    val agentName: String,
    @ColumnInfo(name = "broker_name")
    val brokerName: String,
    @ColumnInfo(name = "agent_license")
    val agentLicense: String?,
    @ColumnInfo(name = "location_id")
    val locationId: Int,
    @ColumnInfo(name = "hide_location")
    val hideLocation: Boolean,
    @ColumnInfo(name = "broker")

    val broker: BrokerEntity,
    @ColumnInfo(name = "amenities")
    val amenities: List<String>,
    @ColumnInfo(name = "amenities_keys")
    val amenitiesKeys: List<String>,

    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "premium")
    val premium: Boolean,
    @ColumnInfo(name = "livingrooms")
    val livingrooms: String,
    @ColumnInfo(name = "verified")
    val verified: Boolean,

    @ColumnInfo(name = "gallery")
    val gallery: List<String>?,
    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "lead_email_receivers")
    val leadEmailReceivers: List<String>,

    @ColumnInfo(name = "reference")
    val reference: String
) : IEntity
