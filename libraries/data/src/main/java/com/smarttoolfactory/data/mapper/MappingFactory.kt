package com.smarttoolfactory.data.mapper

import com.smarttoolfactory.data.model.IEntity
import com.smarttoolfactory.data.model.Mappable
import com.smarttoolfactory.data.model.local.BrokerEntity
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.BrokerDTO
import com.smarttoolfactory.data.model.remote.PropertyDTO
import javax.inject.Inject

/**
 * Mapper for transforming objects between REST and database or REST/db and domain
 * as [IEntity]  which are Non-nullable to Non-nullable
 */
interface Mapper<I, O> {
    fun map(input: I): O
}

/**
 * Mapper for transforming objects between REST and database or REST/db and domain
 * as [List] of [IEntity] which are Non-nullable to Non-nullable
 */
interface ListMapper<I, O> : Mapper<List<I>, List<O>>

private class BrokerDTOtoEntityMapper : Mapper<BrokerDTO, BrokerEntity> {

    override fun map(input: BrokerDTO): BrokerEntity {
        return BrokerEntity(
            id = input.id,
            name = input.name,
            address = input.address,
            phone = input.phone,
            phoneExtension = input.phoneExtension,
            email = input.email,
            // Maps String? nullable
            mobile = input.mobile,
            agentPhoto = input.agentPhoto,
            agentName = input.agentName,
            // Maps List<String>
            leadEmailReceivers = input.leadEmailReceivers,
            license = input.license,
            agentId = input.agentId,
            logo = input.logo
        )
    }
}

class PropertyDTOtoEntityListMapper @Inject constructor() :
    ListMapper<PropertyDTO, PropertyEntity> {

    override fun map(input: List<PropertyDTO>): List<PropertyEntity> {

        return input.map { input ->

            PropertyEntity(
                id = input.id,
                update = input.update,
                categoryId = input.categoryId,
                title = input.title,
                subject = input.subject,
                type = input.type,
                typeId = input.typeId,
                thumbnail = input.thumbnail,
                thumbnailBig = input.thumbnailBig,
                imageCount = input.imageCount,
                price = input.price,
                pricePeriod = input.pricePeriod,
                pricePeriodRaw = input.pricePeriodRaw,
                priceLabel = input.priceLabel,
                priceValue = input.priceValue,
                priceValueRaw = input.priceValueRaw,
                currency = input.currency,
                featured = input.featured,
                location = input.location,
                area = input.area,
                poa = input.poa,
                reraPermit = input.reraPermit,
                bathrooms = input.bathrooms,
                bedrooms = input.bedrooms,
                dateInsert = input.dateInsert,
                dateUpdate = input.dateUpdate,
                agentName = input.agentName,
                brokerName = input.brokerName,
                agentLicense = input.agentLicense,
                locationId = input.locationId,
                hideLocation = input.hideLocation,

                // Maps BrokerEntity
                broker =
                    MapperFactory.createMapper<BrokerDTOtoEntityMapper>().map(input.broker),
                // Maps List<String>
                amenities = input.amenities,
                amenitiesKeys = input.amenitiesKeys,

                latitude = input.lat,
                longitude = input.long,
                premium = input.premium,
                livingrooms = input.livingrooms,
                verified = input.verified,

                // Maps List<String>
                gallery = input.gallery,
                phone = input.phone,

                // Maps List<String>
                leadEmailReceivers = input.leadEmailReceivers,
                reference = input.reference,
            )
        }
    }
}

class PropertyDTOtoPagedEntityListMapper @Inject constructor() :
    ListMapper<PropertyDTO, PagedPropertyEntity> {

    override fun map(input: List<PropertyDTO>): List<PagedPropertyEntity> {

        return input.map { input ->
            PagedPropertyEntity(
                id = input.id,
                update = input.update,
                categoryId = input.categoryId,
                title = input.title,
                subject = input.subject,
                type = input.type,
                typeId = input.typeId,
                thumbnail = input.thumbnail,
                thumbnailBig = input.thumbnailBig,
                imageCount = input.imageCount,
                price = input.price,
                pricePeriod = input.pricePeriod,
                pricePeriodRaw = input.pricePeriodRaw,
                priceLabel = input.priceLabel,
                priceValue = input.priceValue,
                priceValueRaw = input.priceValueRaw,
                currency = input.currency,
                featured = input.featured,
                location = input.location,
                area = input.area,
                poa = input.poa,
                reraPermit = input.reraPermit,
                bathrooms = input.bathrooms,
                bedrooms = input.bedrooms,
                dateInsert = input.dateInsert,
                dateUpdate = input.dateUpdate,
                agentName = input.agentName,
                brokerName = input.brokerName,
                agentLicense = input.agentLicense,
                locationId = input.locationId,
                hideLocation = input.hideLocation,

                // Maps BrokerEntity
                broker =
                    MapperFactory.createMapper<BrokerDTOtoEntityMapper>().map(input.broker),
                // Maps List<String>
                amenities = input.amenities,
                amenitiesKeys = input.amenitiesKeys,

                latitude = input.lat,
                longitude = input.long,
                premium = input.premium,
                livingrooms = input.livingrooms,
                verified = input.verified,

                // Maps List<String>
                gallery = input.gallery,
                phone = input.phone,

                // Maps List<String>
                leadEmailReceivers = input.leadEmailReceivers,
                reference = input.reference,
            )
        }
    }
}

/**
 * Create [Mapper] or [ListMapper] using Reflection api and factory pattern
 */
object MapperFactory {

    inline fun <reified T : Mapper<out Mappable, out Mappable>> createMapper(): T {
        return T::class.java.getDeclaredConstructor().newInstance()
    }

    inline fun <reified T : ListMapper<out Mappable, out Mappable>> createListMapper(): T {
        return T::class.java.getDeclaredConstructor().newInstance()
    }
}

/**
 * Generic Mapper for complex objects with Reflection api, but NOT working
 */
object GenericMapper {

    // TODO Build a generic mapper for complex objects
    fun generate(propertyDTO: PropertyDTO, propertyEntity: PropertyEntity) {

        propertyDTO?.run {

            javaClass.declaredFields.forEach {

                it.isAccessible = true

                when (it.type) {
                    Int::class.java,
                    Double::class.java,
                    Float::class.java,
                    String::class.java,
                    Boolean::class.java -> {

                        val entityField = propertyEntity?.javaClass?.getField(it.name)
                        entityField?.isAccessible = true
                        entityField?.set(it.javaClass, it.get(propertyDTO))
                    }
                }
            }
        }
    }
}
