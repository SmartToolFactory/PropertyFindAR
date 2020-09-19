package com.smarttoolfactory.data.model.local

import androidx.room.ColumnInfo
import com.smarttoolfactory.data.model.IEntity

/**
 * Base Entity class for inheriting same Kotlin properties.
 */
abstract class BasePropertyEntity : IEntity {

    @get:ColumnInfo(name = "id")
    abstract val id: Int

    @get:ColumnInfo(name = "update")
    abstract val update: Int

    @get:ColumnInfo(name = "category_id")
    abstract val categoryId: Int

    @get:ColumnInfo(name = "title")
    abstract val title: String

    @get:ColumnInfo(name = "subject")
    abstract val subject: String

    @get:ColumnInfo(name = "type")
    abstract val type: String

    @get:ColumnInfo(name = "type_id")
    abstract val typeId: Int

    @get:ColumnInfo(name = "thumbnail")
    abstract val thumbnail: String?

    @get:ColumnInfo(name = "thumbnail_big")
    abstract val thumbnailBig: String?

    @get:ColumnInfo(name = "image_count")
    abstract val imageCount: Int

    @get:ColumnInfo(name = "price")
    abstract val price: String

    @get:ColumnInfo(name = "price_period")
    abstract val pricePeriod: String?

    @get:ColumnInfo(name = "price_period_raw")
    abstract val pricePeriodRaw: String

    @get:ColumnInfo(name = "price_label")
    abstract val priceLabel: String?

    @get:ColumnInfo(name = "price_value")
    abstract val priceValue: String?

    @get:ColumnInfo(name = "price_value_raw")
    abstract val priceValueRaw: Int

    @get:ColumnInfo(name = "currency")
    abstract val currency: String

    @get:ColumnInfo(name = "featured")
    abstract val featured: Boolean

    @get:ColumnInfo(name = "location")
    abstract val location: String

    @get:ColumnInfo(name = "area")
    abstract val area: String

    @get:ColumnInfo(name = "poa")
    abstract val poa: Boolean

    @get:ColumnInfo(name = "rera_permit")
    abstract val reraPermit: String?

    @get:ColumnInfo(name = "bathrooms")
    abstract val bathrooms: String

    @get:ColumnInfo(name = "bedrooms")
    abstract val bedrooms: String

    @get:ColumnInfo(name = "date_insert")
    abstract val dateInsert: String

    @get:ColumnInfo(name = "date_update")
    abstract val dateUpdate: String

    @get:ColumnInfo(name = "agent_name")
    abstract val agentName: String

    @get:ColumnInfo(name = "broker_name")
    abstract val brokerName: String

    @get:ColumnInfo(name = "agent_license")
    abstract val agentLicense: String?

    @get:ColumnInfo(name = "location_id")
    abstract val locationId: Int

    @get:ColumnInfo(name = "hide_location")
    abstract val hideLocation: Boolean

    @get:ColumnInfo(name = "broker")

    abstract val broker: BrokerEntity

    @get:ColumnInfo(name = "amenities")
    abstract val amenities: List<String>

    @get:ColumnInfo(name = "amenities_keys")
    abstract val amenitiesKeys: List<String>

    @get:ColumnInfo(name = "latitude")
    abstract val latitude: Double

    @get:ColumnInfo(name = "longitude")
    abstract val longitude: Double

    @get:ColumnInfo(name = "premium")
    abstract val premium: Boolean

    @get:ColumnInfo(name = "livingrooms")
    abstract val livingrooms: String

    @get:ColumnInfo(name = "verified")
    abstract val verified: Boolean

    @get:ColumnInfo(name = "gallery")
    abstract val gallery: List<String>?

    @get:ColumnInfo(name = "phone")
    abstract val phone: String

    @get:ColumnInfo(name = "lead_email_receivers")
    abstract val leadEmailReceivers: List<String>

    @get:ColumnInfo(name = "reference")
    abstract val reference: String
}
