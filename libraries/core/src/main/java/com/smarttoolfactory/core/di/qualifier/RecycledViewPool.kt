package com.smarttoolfactory.core.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecycledViewPool(val value: Type) {

    enum class Type {
        PROPERTY_ITEM,
        PROPERTY_HORIZONTAL,
        CHART_ITEM
    }
}
