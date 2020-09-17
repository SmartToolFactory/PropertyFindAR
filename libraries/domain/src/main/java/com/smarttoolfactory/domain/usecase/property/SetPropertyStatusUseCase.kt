package com.smarttoolfactory.domain.usecase.property

import com.smarttoolfactory.data.repository.FavoritesRepository
import com.smarttoolfactory.domain.mapper.ItemToEntityMapper
import com.smarttoolfactory.domain.model.PropertyItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SetPropertyStatusUseCase @Inject constructor(
    private val favoritesRepo: FavoritesRepository,
    private val mapper: ItemToEntityMapper
) {

    fun updatePropertyStatus(
        userId: Long,
        property: PropertyItem
    ): Flow<Unit> {

        return flow { emit(mapper.map(property)) }
            .map { propertyEntity ->

                favoritesRepo.insertOrUpdateFavorite(
                    userId,
                    propertyEntity,
                    property.viewCount,
                    property.isFavorite
                )
            }
    }
}
