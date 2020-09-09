package extension

import Deps
import TestDeps
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Adds required dependencies to app module
 */
fun DependencyHandler.addAppModuleDependencies() {

    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    // Support and Widgets
    implementation(Deps.APPCOMPAT)
    implementation(Deps.MATERIAL)
    implementation(Deps.CONSTRAINT_LAYOUT)
    implementation(Deps.RECYCLER_VIEW)
    implementation(Deps.VIEWPAGER2)
    implementation(Deps.SWIPE_REFRESH_LAYOUT)

    // Views, Animations
    implementation(Deps.LOTTIE)

    // Lifecycle, LiveData, ViewModel
    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)
    implementation(Deps.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Deps.LIFECYCLE_EXTENSIONS)

    // Navigation Components
    implementation(Deps.NAVIGATION_FRAGMENT)
    implementation(Deps.NAVIGATION_UI)
    implementation(Deps.NAVIGATION_RUNTIME)
    implementation(Deps.NAVIGATION_DYNAMIC)

    // Dagger Hilt
    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)
    // Dagger Hilt AndroidX & ViewModel
    implementation(Deps.DAGGER_HILT_VIEWMODEL)
    kapt(Deps.DAGGER_HILT_ANDROIDX_HILT_COMPILER)

    // RxJava
    implementation(Deps.RX_JAVA3)
    // RxAndroid
    implementation(Deps.RX_JAVA3_ANDROID)

    // Coroutines
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)

    // Leak Canary
//    debugImplementation(Deps.LEAK_CANARY)

    // Room
    implementation(Deps.ROOM_RUNTIME)
    // For Kotlin use kapt instead of annotationProcessor
    kapt(Deps.ROOM_COMPILER)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(Deps.ROOM_KTX)
    // optional - RxJava support for Room
    implementation(Deps.ROOM_RXJAVA3)

    // Retrofit
    implementation(Deps.RETROFIT)
    implementation(Deps.RETROFIT_GSON_CONVERTER)
    implementation(Deps.RETROFIT_RX_JAVA3_ADAPTER)
    // change base url runtime
    implementation(Deps.RETROFIT_URL_MANAGER)
    // Gson
    implementation(Deps.GSON)
    implementation(Deps.CHUCKER_DEBUG)

    // Glide
    implementation(Deps.GLIDE)
    kapt(Deps.GLIDE_COMPILER)
}

/**
 * Adds dependencies to core module
 */
fun DependencyHandler.addCoreModuleDependencies() {
    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    // Support and Widgets
    implementation(Deps.APPCOMPAT)
    implementation(Deps.MATERIAL)
    implementation(Deps.CONSTRAINT_LAYOUT)
    implementation(Deps.RECYCLER_VIEW)
    implementation(Deps.VIEWPAGER2)
    implementation(Deps.SWIPE_REFRESH_LAYOUT)

    // Lifecycle, LiveData, ViewModel
    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)
    implementation(Deps.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Deps.LIFECYCLE_EXTENSIONS)

    // Navigation Components
    implementation(Deps.NAVIGATION_FRAGMENT)
    implementation(Deps.NAVIGATION_UI)
    implementation(Deps.NAVIGATION_RUNTIME)
    implementation(Deps.NAVIGATION_DYNAMIC)

    // Dagger
    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)

    // RxJava
    implementation(Deps.RX_JAVA3)
    // RxAndroid
    implementation(Deps.RX_JAVA3_ANDROID)

    // Coroutines
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)
}

/**
 * Adds core dependencies such as kotlin, appcompat, navigation and dagger-hilt to Dynamic
 * Feature modules.
 *
 */
fun DependencyHandler.addBaseDynamicFeatureModuleDependencies() {
    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    // Lifecycle, LiveData, ViewModel
    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)
    implementation(Deps.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Deps.LIFECYCLE_EXTENSIONS)

    // Navigation Components
    implementation(Deps.NAVIGATION_FRAGMENT)
    implementation(Deps.NAVIGATION_UI)
    implementation(Deps.NAVIGATION_RUNTIME)
    implementation(Deps.NAVIGATION_DYNAMIC)

    // Dagger Hilt
    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)
    // Dagger Hilt AndroidX & ViewModel
    implementation(Deps.DAGGER_HILT_VIEWMODEL)
    kapt(Deps.DAGGER_HILT_ANDROIDX_HILT_COMPILER)

    // RxJava
    implementation(Deps.RX_JAVA3)
    // RxAndroid
    implementation(Deps.RX_JAVA3_ANDROID)

    // Coroutines
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)
}

/**
 * Adds Unit test dependencies
 */
fun DependencyHandler.addUnitTestDependencies() {

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation(TestDeps.JUNIT5_API)
    testRuntimeOnly(TestDeps.JUNIT5_ENGINE)

    // (Optional) If you need "Parameterized Tests"
    testImplementation(TestDeps.JUNIT5_PARAMS)

    testImplementation(TestDeps.ANDROIDX_CORE_TESTING)
    testImplementation(TestDeps.ROBOLECTRIC)

    // AndroidX Test - JVM testing
    testImplementation(TestDeps.ANDROIDX_TEST_CORE_KTX)
//    testImplementation(TestDeps.ANDROIDX_JUNIT)

    // Coroutines Test
    testImplementation(TestDeps.COROUTINES_TEST)

    // MockWebServer
    testImplementation(TestDeps.MOCK_WEB_SERVER)

    // Gson
    testImplementation(TestDeps.GSON)

    // MockK
    testImplementation(TestDeps.MOCK_K)
    // Truth
    testImplementation(TestDeps.TRUTH)
}

fun DependencyHandler.addInstrumentationTestDependencies() {

    // AndroidX Test - Instrumented testing
    androidTestImplementation(TestDeps.ANDROIDX_JUNIT)
    androidTestImplementation(TestDeps.ANDROIDX_CORE_TESTING)

    // Espresso
    androidTestImplementation(TestDeps.ANDROIDX_ESPRESSO)

    // Testing Navigation
    androidTestImplementation(TestDeps.NAVIGATION_TEST)

    // Coroutines Test
    androidTestImplementation(TestDeps.COROUTINES_TEST)

    // MockWebServer
    androidTestImplementation(TestDeps.MOCK_WEB_SERVER)
    // Gson
    androidTestImplementation(TestDeps.GSON)

    // MockK
    androidTestImplementation(TestDeps.MOCK_K)
    // Truth
    androidTestImplementation(TestDeps.TRUTH)
}

/*
 * These extensions mimic the extensions that are generated on the fly by Gradle.
 * They are used here to provide above dependency syntax that mimics Gradle Kotlin DSL
 * syntax in module\build.gradle.kts files.
 */
@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

private fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

private fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
    add("testRuntimeOnly", dependencyNotation)

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

private fun DependencyHandler.project(
    path: String,
    configuration: String? = null
): ProjectDependency {
    val notation = if (configuration != null) {
        mapOf("path" to path, "configuration" to configuration)
    } else {
        mapOf("path" to path)
    }

    return uncheckedCast(project(notation))
}

@Suppress("unchecked_cast", "nothing_to_inline", "detekt.UnsafeCast")
private inline fun <T> uncheckedCast(obj: Any?): T = obj as T