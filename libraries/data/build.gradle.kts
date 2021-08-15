import extension.addInstrumentationTestDependencies
import extension.addUnitTestDependencies

plugins {
    id(Plugins.ANDROID_LIBRARY_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
    id(Plugins.DAGGER_HILT_PLUGIN)
}

android {

    compileSdk= AndroidVersion.COMPILE_SDK_VERSION
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Specifies one flavor dimension. Intend to use both reactive libraries as flavors as project develops

//    flavorDimensions("reactive")
//
//    productFlavors {
//
//        create("rxjava") {
//            dimension = "reactive"
//            applicationIdSuffix = ".rxjava"
//            versionNameSuffix  = "-rxjava"
//        }
//        create("coroutines") {
//            dimension = "reactive"
//            applicationIdSuffix =".coroutines"
//            versionNameSuffix = "-coroutines"
//        }
//    }

    sourceSets {

        val sharedTestDir =
            "${project(Modules.AndroidLibrary.TEST_UTILS).projectDir}/src/test-shared/java"

        getByName("test") {
//            java.srcDir(sharedTestDir)
            resources.srcDir(
                "${project(Modules.AndroidLibrary.TEST_UTILS).projectDir}" +
                    "/src/test/resources"
            )
        }

        getByName("androidTest") {
//            java.srcDir(sharedTestDir)
            resources.srcDir(
                "${project(Modules.AndroidLibrary.TEST_UTILS).projectDir}" +
                    "/src/test/resources"
            )
        }
    }

    configurations.all {
        resolutionStrategy {
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-debug")
        }
    }

    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    // Dagger
    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)

    // Room
    implementation(Deps.ROOM_RUNTIME)
    // For Kotlin use kapt instead of annotationProcessor
    kapt(Deps.ROOM_COMPILER)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(Deps.ROOM_KTX)
    // optional - RxJava support for Room
    implementation(Deps.ROOM_RXJAVA3)

    // RxJava
    implementation(Deps.RX_JAVA3)
    // RxAndroid
    implementation(Deps.RX_JAVA3_ANDROID)

    // Coroutines
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)

    // Retrofit
    implementation(Deps.RETROFIT)
    implementation(Deps.RETROFIT_GSON_CONVERTER)
    implementation(Deps.RETROFIT_RX_JAVA3_ADAPTER)

    // Gson
    implementation(Deps.GSON)
    // Chucker
    implementation(Deps.CHUCKER_DEBUG)

    addUnitTestDependencies()
    testImplementation(project(Modules.AndroidLibrary.TEST_UTILS))

    addInstrumentationTestDependencies()
    androidTestImplementation(project(Modules.AndroidLibrary.TEST_UTILS))
    implementation(kotlin("reflect"))
}
