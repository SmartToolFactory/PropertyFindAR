package com.smarttoolfactory.domain.mapper

import com.smarttoolfactory.data.mapper.ListMapper
import com.smarttoolfactory.data.mapper.Mapper
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.model.local.BrokerEntity
import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.domain.model.BrokerItem
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject

// TODO Create GENERIC MAPPERS FOR Base for a BasePropertyItem class, stop DUPLICATING ITEMS
private class BrokerEntityToItemMapper : Mapper<BrokerEntity, BrokerItem> {

    override fun map(input: BrokerEntity): BrokerItem {
        return BrokerItem(
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

private class BrokerItemToEntityMapper : Mapper<BrokerItem, BrokerEntity> {

    override fun map(input: BrokerItem): BrokerEntity {
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

class PropertyEntityToItemListMapper @Inject constructor() :
    ListMapper<PropertyEntity, PropertyItem> {

    override fun map(input: List<PropertyEntity>): List<PropertyItem> {

        return input.map { input ->

            PropertyItem(
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
                    MapperFactory.createMapper<BrokerEntityToItemMapper>().map(input.broker),
                // Maps List<String>
                amenities = input.amenities,
                amenitiesKeys = input.amenitiesKeys,

                latitude = input.latitude,
                longitude = input.longitude,
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

class PagedEntityToItemListMapper @Inject constructor() :
    ListMapper<PagedPropertyEntity, PropertyItem> {

    override fun map(input: List<PagedPropertyEntity>): List<PropertyItem> {

        return input.map { input ->

            PropertyItem(
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
                    MapperFactory.createMapper<BrokerEntityToItemMapper>().map(input.broker),
                // Maps List<String>
                amenities = input.amenities,
                amenitiesKeys = input.amenitiesKeys,

                latitude = input.latitude,
                longitude = input.longitude,
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

class ItemToEntityMapper @Inject constructor() :
    Mapper<PropertyItem, InteractivePropertyEntity> {

    override fun map(input: PropertyItem): InteractivePropertyEntity {

        return InteractivePropertyEntity(
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
                MapperFactory.createMapper<BrokerItemToEntityMapper>().map(input.broker),
            // Maps List<String>
            amenities = input.amenities,
            amenitiesKeys = input.amenitiesKeys,

            latitude = input.latitude,
            longitude = input.longitude,
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
