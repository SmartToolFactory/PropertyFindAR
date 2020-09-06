package com.smarttoolfactory.data.mapper

import com.smarttoolfactory.data.model.IEntity
import com.smarttoolfactory.data.model.Mappable
import com.smarttoolfactory.data.model.local.BrokerEntity
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

class BrokerDTOtoEntityMapper : Mapper<BrokerDTO, BrokerEntity> {

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

        return input.map { propertyDTO ->

            PropertyEntity(
                id = propertyDTO.id,
                update = propertyDTO.update,
                categoryId = propertyDTO.categoryId,
                title = propertyDTO.title,
                subject = propertyDTO.subject,
                type = propertyDTO.type,
                typeId = propertyDTO.typeId,
                thumbnail = propertyDTO.thumbnail,
                thumbnailBig = propertyDTO.thumbnailBig,
                imageCount = propertyDTO.imageCount,
                price = propertyDTO.price,
                pricePeriod = propertyDTO.pricePeriod,
                pricePeriodRaw = propertyDTO.pricePeriodRaw,
                priceLabel = propertyDTO.priceLabel,
                priceValue = propertyDTO.priceValue,
                priceValueRaw = propertyDTO.priceValueRaw,
                currency = propertyDTO.currency,
                featured = propertyDTO.featured,
                location = propertyDTO.location,
                area = propertyDTO.area,
                poa = propertyDTO.poa,
                reraPermit = propertyDTO.reraPermit,
                bathrooms = propertyDTO.bathrooms,
                bedrooms = propertyDTO.bedrooms,
                dateInsert = propertyDTO.dateInsert,
                dateUpdate = propertyDTO.dateUpdate,
                agentName = propertyDTO.agentName,
                brokerName = propertyDTO.brokerName,
                agentLicense = propertyDTO.agentLicense,
                locationId = propertyDTO.locationId,
                hideLocation = propertyDTO.hideLocation,

                // Maps BrokerEntity
                broker =
                MapperFactory.createMapper<BrokerDTOtoEntityMapper>().map(propertyDTO.broker),
                // Maps List<String>
                amenities = propertyDTO.amenities,
                amenitiesKeys = propertyDTO.amenitiesKeys,

                lat = propertyDTO.lat,
                long = propertyDTO.long,
                premium = propertyDTO.premium,
                livingrooms = propertyDTO.livingrooms,
                verified = propertyDTO.verified,

                // Maps List<String>
                gallery = propertyDTO.gallery,
                phone = propertyDTO.phone,

                // Maps List<String>
                leadEmailReceivers = propertyDTO.leadEmailReceivers,
                reference = propertyDTO.reference,
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
