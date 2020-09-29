package com.smarttoolfactory.domain.mapper

import com.smarttoolfactory.data.mapper.ListMapper
import com.smarttoolfactory.data.mapper.Mapper
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.model.local.BrokerEntity
import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.local.PropertyWithFavorites
import com.smarttoolfactory.data.model.remote.BrokerDTO
import com.smarttoolfactory.data.model.remote.PropertyDTO
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

private class BrokerDTOtoItemMapper : Mapper<BrokerDTO, BrokerItem> {

    override fun map(input: BrokerDTO): BrokerItem {
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

class PropertyDTOtoItemListMapper @Inject constructor() :
    ListMapper<PropertyDTO, PropertyItem> {

    override fun map(input: List<PropertyDTO>): List<PropertyItem> {

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
                    MapperFactory.createMapper<BrokerDTOtoItemMapper>().map(input.broker),
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

class FavoriteEntityToItemListMapper @Inject constructor() :
    ListMapper<PropertyWithFavorites, PropertyItem> {

    override fun map(input: List<PropertyWithFavorites>): List<PropertyItem> {

        return input.map { input ->

            PropertyItem(
                id = input.property.id,
                update = input.property.update,
                categoryId = input.property.categoryId,
                title = input.property.title,
                subject = input.property.subject,
                type = input.property.type,
                typeId = input.property.typeId,
                thumbnail = input.property.thumbnail,
                thumbnailBig = input.property.thumbnailBig,
                imageCount = input.property.imageCount,
                price = input.property.price,
                pricePeriod = input.property.pricePeriod,
                pricePeriodRaw = input.property.pricePeriodRaw,
                priceLabel = input.property.priceLabel,
                priceValue = input.property.priceValue,
                priceValueRaw = input.property.priceValueRaw,
                currency = input.property.currency,
                featured = input.property.featured,
                location = input.property.location,
                area = input.property.area,
                poa = input.property.poa,
                reraPermit = input.property.reraPermit,
                bathrooms = input.property.bathrooms,
                bedrooms = input.property.bedrooms,
                dateInsert = input.property.dateInsert,
                dateUpdate = input.property.dateUpdate,
                agentName = input.property.agentName,
                brokerName = input.property.brokerName,
                agentLicense = input.property.agentLicense,
                locationId = input.property.locationId,
                hideLocation = input.property.hideLocation,

                // Maps BrokerEntity
                broker =
                    MapperFactory
                        .createMapper<BrokerEntityToItemMapper>().map(input.property.broker),
                // Maps List<String>
                amenities = input.property.amenities,
                amenitiesKeys = input.property.amenitiesKeys,

                latitude = input.property.latitude,
                longitude = input.property.longitude,
                premium = input.property.premium,
                livingrooms = input.property.livingrooms,
                verified = input.property.verified,

                // Maps List<String>
                gallery = input.property.gallery,
                phone = input.property.phone,

                // Maps List<String>
                leadEmailReceivers = input.property.leadEmailReceivers,
                reference = input.property.reference,
                viewCount = input.viewCount,
                isFavorite = input.liked
            )
        }
    }
}

class PropertyItemToEntityMapper @Inject constructor() :
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
