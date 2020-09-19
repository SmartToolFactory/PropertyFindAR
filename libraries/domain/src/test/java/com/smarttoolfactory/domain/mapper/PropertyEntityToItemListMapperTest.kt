package com.smarttoolfactory.domain.mapper

import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText

class PropertyEntityToItemListMapperTest {

    companion object {

        private val propertyDTOList by lazy {
            convertToObjectFromJson<PropertyResponse>(getResourceAsText(RESPONSE_JSON_PATH))!!.res
        }

        private val entityList =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(propertyDTOList)
    }
}
