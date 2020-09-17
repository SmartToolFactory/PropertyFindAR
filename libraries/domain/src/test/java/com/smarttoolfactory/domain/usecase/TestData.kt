package com.smarttoolfactory.domain.usecase

import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.domain.mapper.PropertyEntityToItemListMapper
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_1
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_2
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText

object TestData {

    /*
        DTOs
     */
    private val propertyResponse = convertToObjectFromJson<PropertyResponse>(
        getResourceAsText(RESPONSE_JSON_PATH)
    )!!

    private val propertyDTOList = propertyResponse.res

    private val propertyResponsePage1 = convertToObjectFromJson<PropertyResponse>(
        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
    )!!

    private val propertyResponsePage2 = convertToObjectFromJson<PropertyResponse>(
        getResourceAsText(RESPONSE_JSON_PATH_PAGE_2)
    )!!

    /*
        Properties
     */
    private val entityMapper = MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()

    val entityList = entityMapper.map(propertyDTOList)
    val entityListPage1 = entityMapper.map(propertyResponsePage1.res)
    val entityListPage2 = entityMapper.map(propertyResponsePage2.res)

    /*
        Items
     */
    val itemMapper = MapperFactory.createListMapper<PropertyEntityToItemListMapper>()

    val itemList = itemMapper.map(entityList)
    val itemListPage1 = itemMapper.map(entityListPage1)
    val itemListPage2 = itemMapper.map(entityListPage2)
}
