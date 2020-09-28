package com.smarttoolfactory.dashboard.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RecycledViewPoolQualifier(val value: Type) {

    enum class Type {
        PROPERTY_ITEM,
        PROPERTY_HORIZONTAL,
        CHART_ITEM
    }
}
