object Deps {

    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${PluginVersion.KOTLIN_VERSION}"

    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX_VERSION}"

    // Fragments
    const val ANDROIDX_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX_VERSION}"

    // AppCompat
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT_VERSION}"

    // Material
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL_VERSION}"

    // ConstraintLayout
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT_VERSION}"

    // RecyclerView
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLER_VIEW_VERSION}"

    // ViewPager2
    const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:${Version.VIEWPAGER2_VERSION}"

    // SwipeRefreshLayout
    const val SWIPE_REFRESH_LAYOUT =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Version.SWIPE_REFRESH_LAYOUT_VERSION}"


    // Lifecycle, ViewModel and LiveData

    // ViewModel
    const val LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE_VERSION}"
    // LiveData
    const val LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE_VERSION}"

    // Saved state module for ViewModel
    const val LIFECYCLE_VIEWMODEL_SAVEDSTATE =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.LIFECYCLE_VERSION}"

    // Annotation processor
//    const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Version.LIFECYCLE_VERSION}"

    // alternately - if using Java8, use the following instead of lifecycle-compiler
    const val LIFECYCLE_COMMON_JAVA8 =
        "androidx.lifecycle:lifecycle-common-java8:${Version.LIFECYCLE_VERSION}"

    // optional - helpers for implementing LifecycleOwner in a Service
    const val LIFECYCLE_SERVICE =
        "androidx.lifecycle:lifecycle-service:${Version.LIFECYCLE_VERSION}"

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    const val LIFECYCLE_PROCESS =
        "androidx.lifecycle:lifecycle-process:${Version.LIFECYCLE_VERSION}"

    // optional - ReactiveStreams support for LiveData
    const val LIFECYCLE_REACTIVE_STREAMS =
        "androidx.lifecycle:lifecycle-reactivestreams:${Version.LIFECYCLE_VERSION}"


    // Navigation Components
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Version.NAVIGATION_VERSION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Version.NAVIGATION_VERSION}"
    const val NAVIGATION_RUNTIME =
        "androidx.navigation:navigation-runtime-ktx:${Version.NAVIGATION_VERSION}"

    // Dynamic Feature Module Support
    const val NAVIGATION_DYNAMIC =
        "androidx.navigation:navigation-dynamic-features-fragment:${Version.NAVIGATION_VERSION}"

    // Dagger Core dependencies
    const val DAGGER = "com.google.dagger:dagger:${Version.MATERIAL_VERSION}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Version.DAGGER_VERSION}"
    const val DAGGER_ANNOTATION_PROCESSOR =
        "com.google.dagger:dagger-android-processor:${Version.DAGGER_VERSION}"

    // Dagger Hilt
    const val DAGGER_HILT_ANDROID = "com.google.dagger:hilt-android:${Version.DAGGER_HILT_VERSION}"
    const val DAGGER_HILT_COMPILER =
        "com.google.dagger:hilt-android-compiler:${Version.DAGGER_HILT_VERSION}"

    // Dagger Hilt AndroidX
    const val DAGGER_HILT_VIEWMODEL =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Version.DAGGER_HILT_ANDRIODX}"
    const val DAGGER_HILT_ANDROIDX_HILT_COMPILER =
        "androidx.hilt:hilt-compiler:${Version.DAGGER_HILT_ANDRIODX}"

    // RxJava2
    const val RX_JAVA2 = "io.reactivex.rxjava2:rxjava:${Version.RX_JAVA2_VERSION}"

    // RxJava2 Android
    const val RX_JAVA2_ANDROID =
        "io.reactivex.rxjava2:rxandroid:${Version.RX_JAVA2_ANDROID_VERSION}"

    // RxJava3
    const val RX_JAVA3 = "io.reactivex.rxjava3:rxjava:${Version.RX_JAVA3_VERSION}"

    // RxJava3 Android
    const val RX_JAVA3_ANDROID =
        "io.reactivex.rxjava3:rxandroid:${Version.RX_JAVA3_ANDROID_VERSION}"

    // Coroutines
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES_VERSION}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES_VERSION}"

    // Retrofit
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Version.RETROFIT_VERSION}"
    const val RETROFIT_GSON_CONVERTER =
        "com.squareup.retrofit2:converter-gson:${Version.RETROFIT_VERSION}"
    const val RETROFIT_RX_JAVA2_ADAPTER = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0"
    const val RETROFIT_RX_JAVA3_ADAPTER =
        "com.squareup.retrofit2:adapter-rxjava3:${Version.RETROFIT_RXJAVA3_ADAPTER}"

    // Retrofit change base url runtime
    const val RETROFIT_URL_MANAGER = "me.jessyan:retrofit-url-manager:1.4.0"

    const val OK_HTTP3 = "com.squareup.okhttp3:okhttp:${Version.OK_HTTP3_VERSION}"

    // Gson
    const val GSON = "com.google.code.gson:gson:${Version.GSON_VERSION}"

    // Room
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Version.ROOM_VERSION}"

    // For Kotlin use kapt instead of annotationProcessor
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Version.ROOM_VERSION}"

    // optional - Kotlin Extensions and Coroutines support for Room
    const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM_VERSION}"

    // optional - RxJava support for Room
    const val ROOM_RXJAVA2 = "androidx.room:room-rxjava2:${Version.ROOM_VERSION}"
    const val ROOM_RXJAVA3 = "androidx.room:room-rxjava3:${Version.ROOM_VERSION}"

    // glide
    const val GLIDE = "com.github.bumptech.glide:glide:${Version.GLIDE_VERSION}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Version.GLIDE_VERSION}"

    // Lottie
    const val LOTTIE = "com.airbnb.android:lottie:${Version.LOTTIE_VERSION}"

    // MpChart
    const val MP_CHART = "com.github.PhilJay:MPAndroidChart:${Version.MP_CHART_VERSION}"

    // Preference Manager
    const val PREFERENCE_MANAGER =
        "androidx.preference:preference-ktx:${Version.PREFERENCE_MANAGER_VERSION}"

    // Chucker
    const val CHUCKER_DEBUG = "com.github.chuckerteam.chucker:library:${Version.CHUCKER_VERSION}"
    const val CHUCKER_RELEASE =
        "com.github.chuckerteam.chucker:library-no-op:${Version.CHUCKER_VERSION}"

    // Leak Canary
    const val LEAK_CANARY =
        "com.squareup.leakcanary:leakcanary-android:${Version.LEAK_CANARY_VERSION}"
}

object TestDeps {

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    const val JUNIT5_API = "org.junit.jupiter:junit-jupiter-api:${TestVersion.junit5Version}"
    const val JUNIT5_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${TestVersion.junit5Version}"

    // (Optional) If you need "Parameterized Tests"
    const val JUNIT5_PARAMS = "org.junit.jupiter:junit-jupiter-params:${TestVersion.junit5Version}"

    const val ANDROIDX_CORE_TESTING =
        "androidx.arch.core:core-testing:${TestVersion.archTestingVersion}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${TestVersion.robolectricVersion}"

    // AndroidX Test - JVM testing
    const val ANDROIDX_TEST_CORE_KTX =
        "androidx.test:core-ktx:${TestVersion.androidXTestCoreVersion}"
    const val ANDROIDX_JUNIT =
        "androidx.test.ext:junit:${TestVersion.androidXTestExtKotlinRunnerVersion}"

    const val ANDROIDX_ESPRESSO =
        "androidx.test.espresso:espresso-contrib:${TestVersion.espressoVersion}"

    // Coroutines test
    const val COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${TestVersion.coroutinesTestVersion}"

    // MockWebServer
    const val MOCK_WEB_SERVER =
        "com.squareup.okhttp3:mockwebserver:${TestVersion.mockWebServerVersion}"

    // Gson
    const val GSON = "com.google.code.gson:gson:${Version.GSON_VERSION}"

    // MockK
    const val MOCK_K = "io.mockk:mockk:${TestVersion.mockKVersion}"

    // Truth
    const val TRUTH = "com.google.truth:truth:${TestVersion.truthVersion}"

    // Espresso
//    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${TestVersion.espressoVersion}"

    // Testing Navigation
    const val NAVIGATION_TEST =
        "androidx.navigation:navigation-testing:${Version.NAVIGATION_VERSION}"
}

