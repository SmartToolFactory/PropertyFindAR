package com.smarttoolfactory.data.mapper

import com.google.common.truth.Truth
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import org.junit.jupiter.api.Test

class PropertyEntityEntityListMapperTest {

    private val propertyDTOResponse by lazy {
        convertToObjectFromJson<PropertyResponse>(getResourceAsText(RESPONSE_JSON_PATH))!!
    }

    @Test
    fun `given a valid propertyDTO list, should map to propertyEntity list`() {

        val propertyDTOList = propertyDTOResponse.res

        // GIVEN
        val mapper = MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()

        // WHEN
        val actual = mapper.map(propertyDTOList)

        // THEN
        actual.forEachIndexed { index, propertyEntity ->
            Truth.assertThat(propertyEntity.id).isEqualTo(propertyDTOList[index].id)
            Truth.assertThat(propertyEntity.update).isEqualTo(propertyDTOList[index].update)
            Truth.assertThat(propertyEntity.price).isEqualTo(propertyDTOList[index].price)
        }
    }
}
