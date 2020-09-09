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

//    @Test
//    fun `Given a valid propertyEntity list, should map to propertyItem list`() {
//
//        // GIVEN
//        val mapper =
//            MapperFactory.createListMapper<PropertyEntityToItemListMapper>()
//
//        // WHEN
//        val actual = mapper.map(entityList)
//
//        // THEN
//        actual.forEachIndexed { index, propertyItem ->
//            Truth.assertThat(propertyItem.id).isEqualTo(entityList[index].id)
//            Truth.assertThat(propertyItem.update).isEqualTo(entityList[index].update)
//            Truth.assertThat(propertyItem.price).isEqualTo(entityList[index].price)
//        }
//    }
}
