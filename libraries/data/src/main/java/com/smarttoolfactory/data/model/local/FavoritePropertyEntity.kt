package com.smarttoolfactory.data.model.local

import androidx.room.Entity

@Entity(tableName = "favorite", primaryKeys = ["id"])
data class FavoritePropertyEntity(

//    @ColumnInfo(name = "insert_order")
//    var insertOrder: Int,

    override val id: Int,
    override val update: Int,
    override val categoryId: Int,
    override val title: String,
    override val subject: String,
    override val type: String,
    override val typeId: Int,
    override val thumbnail: String?,
    override val thumbnailBig: String?,
    override val imageCount: Int,
    override val price: String,
    override val pricePeriod: String?,
    override val pricePeriodRaw: String,
    override val priceLabel: String?,
    override val priceValue: String?,
    override val priceValueRaw: Int,
    override val currency: String,
    override val featured: Boolean,
    override val location: String,
    override val area: String,
    override val poa: Boolean,
    override val reraPermit: String?,
    override val bathrooms: String,
    override val bedrooms: String,
    override val dateInsert: String,
    override val dateUpdate: String,
    override val agentName: String,
    override val brokerName: String,
    override val agentLicense: String?,
    override val locationId: Int,
    override val hideLocation: Boolean,

    override val broker: BrokerEntity,

    override val amenities: List<String>,

    override val amenitiesKeys: List<String>,

    override val latitude: Double,
    override val longitude: Double,
    override val premium: Boolean,
    override val livingrooms: String,
    override val verified: Boolean,

    override val gallery: List<String>?,
    override val phone: String,

    override val leadEmailReceivers: List<String>,
    override val reference: String,

    // Property like and display status
    var displayCount: Int = 0,
    var isFavorite: Boolean = false
) : BasePropertyEntity()
