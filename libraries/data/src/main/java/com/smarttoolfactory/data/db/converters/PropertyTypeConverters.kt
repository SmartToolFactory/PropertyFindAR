package com.smarttoolfactory.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smarttoolfactory.data.model.local.BrokerEntity

class PropertyTypeConverters {

    @TypeConverter
    fun fromBrokerEntity(data: BrokerEntity?): String? {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toBrokerEntity(json: String?): BrokerEntity? {
        return Gson().fromJson(json, BrokerEntity::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String?>? {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(json, listType)
    }
}
