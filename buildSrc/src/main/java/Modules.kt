object Modules {

    const val APP = ":app"

    object AndroidLibrary {
        const val CORE = ":libraries:core"
        const val DATA = ":libraries:data"
        const val DOMAIN = ":libraries:domain"
        const val TEST_UTILS = ":libraries:test-utils"
    }

    /**
     * Dynamic Feature Modules
     */
    object DynamicFeature {
        const val HOME = ":features:home"
        const val PROPERTY_DETAIL = ":features:property_detail"
        const val FAVORITES = ":features:favorites"
        const val NOTIFICATION = ":features:notification"
        const val ACCOUNT = ":features:account"
        const val SEARCH = ":features:search"
    }
}