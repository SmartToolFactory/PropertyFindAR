plugins {
    id(Plugins.ANDROID_LIBRARY_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
}

android {

    compileSdkVersion(AndroidVersion.COMPILE_SDK_VERSION)
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    sourceSets {
//
//        val sharedTestDir = "src/test-shared/java"
//
//        getByName("test") {
//            java.srcDir(sharedTestDir)
//        }
//
//        getByName("androidTest") {
//            java.srcDir(sharedTestDir)
//            resources.srcDir("src/test/resources")
//        }
//    }

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
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    implementation(Deps.RX_JAVA3)
    implementation(Deps.RX_JAVA3_ANDROID)
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)

    // Lifecycle, LiveData, ViewModel
    implementation(Deps.LIFECYCLE_LIVEDATA_KTX)
    implementation(Deps.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Deps.LIFECYCLE_COMMON_JAVA8)

//    implementation(TestDeps.ANDROIDX_CORE_KTX)
//    implementation(TestDeps.ANDROIDX_CORE_TESTING)

    implementation(TestDeps.JUNIT5_API)
    implementation(TestDeps.JUNIT5_ENGINE)

    // GSon
    implementation(Deps.GSON)

    // Coroutines Test
    implementation(TestDeps.COROUTINES_TEST)

    // MockWebServer
    implementation(TestDeps.MOCK_WEB_SERVER)
}
